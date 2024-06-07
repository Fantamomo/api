package at.leisner.api.lang;

import at.leisner.api.API;
import at.leisner.api.util.Util;
import org.bukkit.ChatColor;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Language {
    public static Language defaultLang;
    private static Map<String, Language> languageMap = new HashMap<>();
    private Map<String, Object> yamlData;
    public final String name;

    public Language(String lang, File file) throws FileNotFoundException {
        this(lang, new FileInputStream(file));
    }
    public Language(String lang, InputStream inputStream) {
        languageMap.put(lang, this);
        name = lang;
        if (defaultLang == null) defaultLang = this;
        Yaml yaml = new Yaml(new Constructor(Map.class, new LoaderOptions()));
        yamlData = yaml.load(inputStream);
    }

    public String get(String key) {
        return get(key, key);
    }

    public String get(String key, String defaultValue) {
        String[] keys = key.split("\\.");
        Map<String, Object> currentMap = yamlData;
        Object value = null;

        for (String k : keys) {
            if (currentMap == null) {
                if (this != defaultLang) {
                    return defaultLang.get(key, defaultValue);
                }
                return defaultValue;
            }
            value = currentMap.get(k);
            if (value instanceof Map) {
                currentMap = (Map<String, Object>) value;
            } else {
                currentMap = null;
            }
        }
        if (value == null && this != defaultLang) {
            return defaultLang.get(key, defaultValue);
        }
        return value != null ? value.toString() : defaultValue;
    }
    public String translate(String key, Key... keys) {
        return translateWithPrefixIfAvailable(key, true, keys);
    }
    public String translateWithPrefixIfAvailable(String key, boolean withPrefix, Key... keys) {
        String msg = get(key);
        for (Key member : keys) {
            msg = msg.replace("{"+member.key()+"}", member.value());
        }
        String key2 = Util.removeAfterLastChar(key, '.') + ".prefix";
        if (withPrefix) {
            return (get(key2, null) == null ? "" : get(key2)) + msg;
        }
        return msg;
    }
    public static Language getLanguage(String lang) {
        return languageMap.get(lang);
    }
    public static Set<String> availableLanguages() {
        return languageMap.keySet();
    }
}
