package bolao.controllers;

import bolao.repository.Repository;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;


public class IndexController {

    public static void handleRoutes(final Router router) {
        router.get("/").handler(IndexController::openIndex);
    }

    private static void openIndex(final RoutingContext rctx) {
        Repository.engine.render(rctx.data(), "templates/public/index.peb",
                res -> rctx.response().end(res.result()));
    }

}