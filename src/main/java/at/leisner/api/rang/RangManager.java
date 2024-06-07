package at.leisner.api.rang;

import at.leisner.api.API;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RangManager {
    public static Rang defaultRang;
    private Map<String, Rang> rangMap = new HashMap<>();
    private final API plugin;

    public RangManager(API plugin) {
        this.plugin = plugin;
    }

    public void loadRangs(Reader reader) {
        FileConfiguration config = YamlConfiguration.loadConfiguration(reader);

        for (String key : config.getKeys(false)) {
            String name = config.getString(key + ".name");
            int priority = config.getInt(key + ".priority", 0);
            String prefix = config.getString(key + ".prefix", "");
            String suffix = config.getString(key + ".suffix", "");
            String joinMsg = config.getString(key + ".join-msg", "{display-name} joined the game");
            String quitMsg = config.getString(key + ".quit-msg", "{display-name} quit the game");
            String chatMsg = config.getString(key + ".chat-msg", "{display-name}&9: {msg}");
            String playerNameColor = config.getString(key + ".player-name-color", "<#FFFFFF>");

            Rang rang = new Rang(name,
                    priority,
                    plugin.miniMessage().deserialize(prefix),
                    plugin.miniMessage().deserialize(suffix),
                    plugin.miniMessage().deserialize(joinMsg),
                    plugin.miniMessage().deserialize(quitMsg),
                    plugin.miniMessage().deserialize(chatMsg),
                    TextColor.fromHexString(playerNameColor.replace(">", "").replace("<", "")));
            if (priority == -1) defaultRang = rang;
            rangMap.put(key, rang);
        }
    }

    public Rang getRang(String id) {
        return rangMap.get(id);
    }

    public Set<String> availableRangs() {
        return rangMap.keySet();
    }
}
