package bolao.controllers;

import bolao.repository.Repository;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.FormLoginHandler;

public class LoginController {

    public static void handleRoutes(final Router router) {

        router.get("/security/dashboard/login").handler(LoginController::renderizarPagina);

        router.post("/security/dashboard/login")
                .handler(FormLoginHandler.create(Repository.mongoDaoReactive.getMongoAuth()).setDirectLoggedInOKURL("/dashboard"))
                .failureHandler(rctx -> {
                    rctx.put("loginNotFound", 1);
                    renderizarPagina(rctx);
                });

        logoutHandler(router);
    }

    private static void renderizarPagina(final RoutingContext ctx) {
        if (ctx.user() != null && ctx.user().principal().getString("username") != null) {
            DashBoardController.renderDashboardPage(ctx);
            return;
        }
        ctx.clearUser();
        Repository.engine.render(ctx.data(), "templates/login/index.peb", res -> ctx.response().end(res.result()));
    }

    public static void abrirLogin(final RoutingContext ctx) {
        Repository.engine.render(ctx.data(), "templates/login/index.peb", res -> ctx.response().end(res.result()));
    }

    private static void logoutHandler(final Router router) {
        router.get("/logout").handler(context -> {
            context.clearUser();
            // Redirect back to the index page
            context.response().putHeader("location", "/").setStatusCode(302).end();
        });
    }
}