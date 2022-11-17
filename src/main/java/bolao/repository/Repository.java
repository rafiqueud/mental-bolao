package bolao.repository;


import bolao.dao.MongoDaoReactive;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;

public abstract class Repository {

    public static MongoDaoReactive mongoDaoReactive;

    public static PebbleTemplateEngine engine;

}
