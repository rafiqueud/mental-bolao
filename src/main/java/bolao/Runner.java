package bolao;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class Runner {

    @SuppressWarnings("rawtypes")
    public static void runExample(Class clazz) {
        final VertxOptions options = new VertxOptions().setBlockedThreadCheckInterval(3600000)
                .setMaxEventLoopExecuteTime(6)
                .setMaxEventLoopExecuteTimeUnit(TimeUnit.SECONDS)
                .setMaxEventLoopExecuteTime(15000);
        Consumer<Vertx> runner = vertx -> vertx.deployVerticle(clazz.getName());
        Vertx vertx = Vertx.vertx(options);
        runner.accept(vertx);
    }

}