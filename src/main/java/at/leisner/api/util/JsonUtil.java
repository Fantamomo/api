package at.leisner.api.util;

import at.leisner.api.user.User;
import com.google.gson.*;

public abstract class JsonUtil<T> {
    public static final JsonUtil<User> USER = new JsonUtil<>() {
        @Override
        public JsonElement serializer(User user) {
            return user.toJson();
        }

        @Override
        public User deserializer(JsonElement jsonElement) {
            return User.fromJson(jsonElement);
        }
    };

    private JsonUtil() {
    }
    public abstract JsonElement serializer(T object);
    public abstract T deserializer(JsonElement jsonElement);

}
