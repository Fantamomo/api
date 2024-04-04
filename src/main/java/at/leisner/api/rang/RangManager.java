package at.leisner.api.rang;

import at.leisner.api.Api;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RangManager {
    private File configFile;
    private FileConfiguration config;
    private static List<Rang> range = new ArrayList<>();
    private static Rang defaultRang = new Rang("default", "Default", "", "", "%display_name%&6: %msg_color%%msg%", "&8%display_name% &7 ist dem Spiel beigetreten", "&8%display_name% &7hat das Spiel verlassen", "&8", "&7", 0, "%prefix%%player_color%%player%%suffix%");

    public RangManager() {
        createCustomConfig();
        loadRang();
    }

    private void createCustomConfig() {
        Api plugin = Api.getInstance();
        configFile = new File(plugin.getDataFolder(), "rang.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("rang.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getCustomConfig() {
        return this.config;
    }

    private void loadRang() {
        Set<String> keys = config.getKeys(false);
        for (String key : keys) {
            String name = config.getString(key + ".name", key + "-name");
            String prefix = config.getString(key + ".prefix", "");
            String suffix = config.getString(key + ".suffix", "");
            String chatFormat = config.getString(key + ".chatMSG", "%prefix%%player_color%%player%%suffix%&6: %msg_color%%msg%");
            String joinMSG = config.getString(key + ".joinMSG", "&8%display_name% &7 ist dem Spiel beigetreten");
            String quitMSG = config.getString(key + ".quitMSG", "&8%display_name% &7hat das Spiel verlassen");
            String playerColor = config.getString(key + ".player_color", "");
            String msgColor = config.getString(key + ".msg_color", "");
            String displayName = config.getString(key + ".displayName", "%prefix%%player_color%%player%%suffix%");
            int prioritiat = config.getInt(key + ".prioritiat", 0);
            range.add(new Rang(key, name, prefix, suffix, chatFormat, joinMSG, quitMSG, playerColor, msgColor, prioritiat, displayName));
        }
    }

    public List<Rang> getRange() {
        return range;
    }

    private static Rang findeHigestRang(Player p) {
        Rang higestRang = getDefault();
        for (Rang rang : range) {
            if (p.hasPermission("rang." + rang.getId())) ;
            if (higestRang.getPrioritiat() < rang.getPrioritiat()) {
                higestRang = rang;
            }
        }
        return higestRang;
    }

    public static Rang getDefault() {
        for (Rang rang : range) {
            if (rang.getId().equals("default")) {
                return rang;
            }
        }
        return defaultRang;
    }

    public Rang getRangByName(String rangName) {
        for (Rang rang : range) {
            if (rang.getId().equals(rangName)) return rang;
        }
        return null;
    }

    public Rang findRangForPlayer(Player p) {
        return findeHigestRang(p);
    }

    public static String getDisplayName(Player p) {
        Rang rang = findeHigestRang(p);
        return ChatColor.translateAlternateColorCodes('&', rang.getPrefix() +" "+ p.getName() + rang.getSuffix());
    }
    public List<String> getAvailableRanks() {
        return range.stream() // Erstelle einen Stream aus der Liste von Objekten
                .map(Rang::getId) // Wandle jedes Objekt in seine ID um
                .collect(Collectors.toList());
    }
}
