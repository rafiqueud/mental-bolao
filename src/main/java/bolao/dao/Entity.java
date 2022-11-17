package bolao.dao;

import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

public abstract class Entity {

    @BsonId
    protected ObjectId id;

    public ObjectId getId() {
        return id;
    }

    public void setId(final ObjectId id) {
        this.id = id;
    }


}