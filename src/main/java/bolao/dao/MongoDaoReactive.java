package bolao.dao;

import bolao.models.JsonHelper;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.mongo.MongoAuthentication;
import io.vertx.ext.auth.mongo.MongoAuthenticationOptions;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.MongoClientUpdateResult;
import io.vertx.ext.mongo.UpdateOptions;
import java.util.HashMap;
import java.util.List;


public class MongoDaoReactive {

    private final MongoClient mongoClient;
    private MongoAuthentication mongoAuthProvider;

    public MongoDaoReactive(final Vertx vertx) {
        final var vals = new HashMap<String, Object>(2, 1.0F);
        //TODO colocar URL de Login
        vals.put("connection_string", "AQUI_URL_DE_LOGIN");
        this.mongoClient = MongoClient.create(vertx, new JsonObject(vals));
    }

    public MongoClient getMongoClient() {
        return mongoClient;
    }

    public MongoAuthentication getMongoAuth() {
        if (this.mongoAuthProvider == null) {
            this.mongoAuthProvider = MongoAuthentication.create(mongoClient, new MongoAuthenticationOptions());
        }
        return mongoAuthProvider;
    }

    /**
     * @param objeto - need extends respective Entity
     *               Funcao Generica para inserir no banco de dados. Ele salva com o nome
     *               completo, exemplo: br.edu.unoesc.modelo.Funcionario seria a collection de Funcionario, se já existe, ele da update.:)
     *               É utilizado os getters para pegar os valores, caso nao existam = PAU !!!!!
     */
    public void save(final Entity objeto) {
        saveAndDo(objeto, event -> System.out.println("Saved " + event.succeeded()));
    }


    @SuppressWarnings("rawtypes")
    public void update(final Class classe, JsonObject query, JsonObject whatUpdate, UpdateOptions updateOptions, Handler<AsyncResult<MongoClientUpdateResult>> whatDo) {
        mongoClient.updateCollectionWithOptions(getCollectionName(classe), query, whatUpdate, updateOptions, whatDo);
    }


    @SuppressWarnings("rawtypes")
    public void whatDoWithThatList(final Class classe, JsonObject CamposToFind, Handler<AsyncResult<List<JsonObject>>> whatDo) {
        mongoClient.find(getCollectionName(classe), CamposToFind, whatDo);
    }

    @SuppressWarnings("rawtypes")
    public void whatDoWithAllThatList(final Class classe, Handler<AsyncResult<List<JsonObject>>> whatDo) {
        mongoClient.find(getCollectionName(classe), JsonHelper.empty(), whatDo);
    }


    @SuppressWarnings("rawtypes")
    public void count(final Class classe, JsonObject camposToFind, Handler<AsyncResult<Long>> whatDo) {
        mongoClient.count(getCollectionName(classe), camposToFind, whatDo);
    }

    @SuppressWarnings("rawtypes")
    private String getCollectionName(final Class classe) {
        final var className = classe.getName();
        final var splitted = className.split("\\.");
        return splitted[splitted.length - 1].toLowerCase();
    }

    public void saveAndDo(final Entity objeto, Handler<AsyncResult<String>> resultHandler) {
        final JsonObject entries = JsonObject.mapFrom(objeto);
        if (objeto.getId() == null) {
            entries.remove("_id");
            entries.remove("objectId");
        }
        if (objeto.getId() != null) {
            entries.remove("id");
            entries.put("_id", objeto.getId().toString());
        }
        mongoClient.save(getCollectionName(objeto.getClass()), entries, resultHandler);
    }

}
