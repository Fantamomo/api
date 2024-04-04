package at.leisner.api.util;

import at.leisner.api.Api;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Translator {
    private static File configFile;
    private static FileConfiguration config;
    public Translator() {
        createCustomConfig();
    }
    public static String translate(String langPath) {
        String prefix = "";
        String[] strings = langPath.split("\\.");
        String __prefix__ = config.getString(strings[0]+".__prefix__", "");
        List<String> __prefix_at__ = config.getStringList(strings[0]+".__prefix_at__");
        if (__prefix_at__.contains("*") || __prefix_at__.contains(langPath)) prefix = __prefix__;
        if (langPath.endsWith(".prefix")) prefix = "";
        return ChatColor.translateAlternateColorCodes('&',prefix+config.getString(langPath, langPath));
    }
    private void createCustomConfig() {
        Api plugin = Api.getInstance();
        configFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("lang.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
}
