package at.leisner.api.lang;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class Key {
    private final String key;
    private final String value;
    private Key(String key, String value) {
        this.key = key;
        this.value = value;
    }

    public String key() {
        return key;
    }

    public String value() {
        return value;
    }

    public static Key of(String key, String value) {
        return new Key(key, value);
    }
}
