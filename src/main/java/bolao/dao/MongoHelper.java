package bolao.dao;

import io.reactivex.Flowable;
import io.vertx.core.AsyncResult;
import io.vertx.core.json.JsonObject;
import java.util.List;
import org.bson.types.ObjectId;

public abstract class MongoHelper {

    public static <T> Flowable<T> getMappedFlowabe(AsyncResult<List<JsonObject>> result, Class<T> classe) {
        return result.map(Flowable::fromIterable).map(jsonFlow -> jsonFlow.map(jsonObject -> {
            final var classesX = jsonObject.mapTo(classe);
            final var casted = (Entity) classesX;
            try {
                casted.setId(new ObjectId(jsonObject.getString("_id")));
            } catch (final Exception ex) {
                System.out.println("ex");
            }
            return (T) casted;
        })).result();
    }

}