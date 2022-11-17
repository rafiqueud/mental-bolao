package bolao.models;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public abstract class JsonHelper {

    public static JsonObject hash() {
        return new JsonObject(new HashMap<>(3, 1.0f));
    }

    public static JsonObject empty() {
        return new JsonObject(Collections.emptyMap());
    }

    public static JsonArray array() {
        return new JsonArray();
    }

    public static JsonObject elemMatch(JsonObject jsonObject) {
        return hash().put("$elemMatch", jsonObject);
    }

    public static JsonArray array(List<?> list) {
        return new JsonArray(list);
    }

    public static JsonObject or(JsonArray arrayMatches) {
        return hash().put("$or", arrayMatches);
    }

    public static JsonObject or(JsonObject... jsonObjects) {
        return hash().put("$or", new JsonArray(List.of(jsonObjects)));
    }

    public static JsonObject ne(String field, String neValue) {
        var ne = new JsonObject().put("$ne", neValue);
        return new JsonObject().put(field, ne);
    }

    public static JsonObject in(String field, List<String> inValues) {
        JsonArray ja = new JsonArray();
        for (String in : inValues) {
            ja.add(in);
        }
        return hash().put(field, hash().put("$in", ja));
    }

}
