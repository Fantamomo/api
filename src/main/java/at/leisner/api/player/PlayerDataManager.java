package at.leisner.api.player;

import at.leisner.api.Api;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class PlayerDataManager implements Listener {
    private File configFile;
    private FileConfiguration config;
    private List<PlayerData> playerDataList = new ArrayList<>();

    public PlayerDataManager() {
        Bukkit.getPluginManager().registerEvents(this,Api.getInstance());
        createCustomConfig();
    }

    private void createCustomConfig() {
        Api plugin = Api.getInstance();
        configFile = new File(plugin.getDataFolder(), "player.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdirs();
            plugin.saveResource("player.yml", false);
        }
        config = new YamlConfiguration();
        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }
    public FileConfiguration getConfig() {
        return config;
    }

    public File getFile() {
        return configFile;
    }

    public PlayerData getPlayerData(Player p) {
        String uuid = p.getUniqueId().toString();
        for (PlayerData data : playerDataList) {
            if (data.getUuid().equals(uuid)) {
                return data;
            }
        }
        return null;
    }
    private void loadPlayerData(Player p) {
        Set<String> keys = config.getKeys(false);
        String uuid = p.getUniqueId().toString();
        if (keys.contains(uuid)) {
            String name = config.getString(uuid+".name","");
            boolean isFrozen = config.getBoolean(uuid+".state.frozen",false);
            boolean isVanish = config.getBoolean(uuid+".state.vanish",false);
            boolean isMute = config.getBoolean(uuid+".state.mute",false);
            playerDataList.add(new PlayerData(uuid,name,isVanish,isMute,isFrozen));
            return;
        }
        config.set(uuid+".name",p.getName());
        config.set(uuid+".state.frozen", false);
        config.set(uuid+".state.vanish", false);
        config.set(uuid+".state.mute", false);
        playerDataList.add(new PlayerData(uuid,p.getName(),false,false,false));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent e) {
        loadPlayerData(e.getPlayer());
    }
}
