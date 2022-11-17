package bolao.controllers;


import bolao.dao.MongoHelper;
import bolao.models.User;
import bolao.repository.Repository;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoUserUtil;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.commons.lang3.StringUtils;

import static bolao.repository.Repository.engine;

public class RegisterAccountHandler {

    public static void handleRoutes(final Router router) {
        router.get("/register").handler(RegisterAccountHandler::renderPage);

        router.post("/register").handler(rctx -> {

            final var password = rctx.request().getParam("password");
            final var username = rctx.request().getParam("email").toLowerCase();

            final var user = new JsonObject()
                    .put("username", username);

            if (validate(user)) {
                Repository.mongoDaoReactive.whatDoWithAllThatList(User.class, res -> {
                    MongoHelper.getMappedFlowabe(res, User.class)
                            .toList()
                            .subscribe(users -> {
                                final var exist = users.stream().anyMatch(usr -> {
                                    final var start = username.split("@")[0];
                                    return usr.getUsername().toLowerCase().startsWith(start);
                                });
                                if (!exist) {
                                    MongoUserUtil.create(Repository.mongoDaoReactive.getMongoClient())
                                            .createUser(username, password, event -> LoginController.abrirLogin(rctx));
                                } else {
                                    erroPagina(rctx);
                                }
                            });

                });
            } else {
                erroPagina(rctx);
            }
        });
    }

    private static boolean validate(final JsonObject user) {
        final var password = user.getString("password");
        final var username = user.getString("username");
        if (StringUtils.isNoneBlank(password, username)) {
            return false;
        }

        if (!username.contains("@")) {
            return false;
        }

        if (username.contains("+")) {
            return false;
        }

        return username.contains("contaquanto.com.br") || username.contains("quan.to") || username.contains("quanto.com");
    }

    private static void erroPagina(final RoutingContext rctx) {
        rctx.put("errorRegister", 1);
        renderPage(rctx);
    }

    private static void renderPage(final RoutingContext rctx) {
        engine.render(rctx.data(), "templates/register/index.peb", res -> rctx.response().end(res.result()));
    }

}