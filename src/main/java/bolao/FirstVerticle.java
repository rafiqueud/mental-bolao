package bolao;

import bolao.controllers.DashBoardController;
import bolao.controllers.IndexController;
import bolao.controllers.LoginController;
import bolao.controllers.RegisterAccountHandler;
import bolao.dao.MongoDaoReactive;
import bolao.repository.Repository;
import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.RedirectAuthHandler;
import io.vertx.ext.web.handler.SessionHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.sstore.LocalSessionStore;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;

public class FirstVerticle extends AbstractVerticle {

    public static void main(String[] args) {
        Runner.runExample(FirstVerticle.class);
    }

    @Override
    public void start() throws Exception {
        final var router = Router.router(vertx);
        System.out.println("Router instantiated.");

        Repository.mongoDaoReactive = new MongoDaoReactive(vertx);
        System.out.println("Connected to database with success.");

        // Configura os esquemas de session and Cookies utilizados no site de administracao.
        configureSessionAndCookies(router);
        System.out.println("Cookies/Session handler configured.");

        // inicia os controllers.
        startControllers(router);
        System.out.println("Controllers started.");

        try {
            //Internet Heroku server// utilizado para subir o servidor no Heroku..
            vertx.createHttpServer().requestHandler(router).listen(Integer.getInteger("http.port"), System.getProperty("http.address", "0.0.0.0"));
        } catch (Exception ex) {
            //Local// Utilizado para subir o servidor localmente na porta especificada..
            vertx.createHttpServer().requestHandler(router).listen(8080);
        }

        System.out.println("Application started with success.");
    }

    private void startControllers(final Router router) {
        Repository.engine = PebbleTemplateEngine.create(vertx);

        // Static Handlers
        final var staticHandlerAssets = StaticHandler.create("templates/assets");
        router.get("/assets/*").handler(staticHandlerAssets);
        router.get("/dashboard/assets/*").handler(staticHandlerAssets);
        router.get("/public/assets/*").handler(StaticHandler.create("templates/public/assets"));

        IndexController.handleRoutes(router);

        // Security
        RegisterAccountHandler.handleRoutes(router);
        LoginController.handleRoutes(router);

        DashBoardController.handleRoutes(router);
    }

    private void configureSessionAndCookies(final Router router) {
        router.route().handler(BodyHandler.create(false));
        router.route().handler(SessionHandler.create(LocalSessionStore.create(vertx)));
        router.route("/dashboard/*").handler(RedirectAuthHandler.create(Repository.mongoDaoReactive.getMongoAuth(), "/security/dashboard/login"));
    }

}