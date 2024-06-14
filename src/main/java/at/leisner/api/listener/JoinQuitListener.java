package at.leisner.api.listener;

import at.leisner.api.API;
import at.leisner.api.user.User;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.permissions.PermissionAttachment;

import java.util.Map;
import java.util.UUID;

public class JoinQuitListener implements Listener {
    private final API plugin;

    public JoinQuitListener(API plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player joinedPlayer = event.getPlayer();
        User user = User.of(joinedPlayer);
        event.joinMessage(null);
//        PermissionAttachment attachment = player.addAttachment(plugin);
//        plugin.getPermissionManager().getPlayerPermissionAttachmentMap().put(player, attachment);

//        // Lade die Berechtigungen aus der Datenbank und setze sie für den Spieler
//        Map<String, Boolean> permissions = user.getPermissions();
//        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
//            attachment.setPermission(entry.getKey(), entry.getValue());
//        }+
        if (user.vanishPlayerData.isVanish()) {
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (User.of(onlinePlayer).vanishSeePriority <= user.vanishUsePriority) {
                    onlinePlayer.hidePlayer(plugin, joinedPlayer);
                }
            }
        }
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            User u = User.of(onlinePlayer);
            if (u.vanishPlayerData.isVanish()) {
                if (user.vanishSeePriority < u.vanishUsePriority) {
                    joinedPlayer.hidePlayer(plugin, onlinePlayer);
                }
            }
        }
        if (!user.vanishPlayerData.isVanish()) {
            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                String rawJoinMsg = user.getCurrentRang().joinMsg().replace("{display-name}", user.getDisplayNameFor(onlinePlayer));
                onlinePlayer.sendMessage(plugin.miniMessage().deserialize(rawJoinMsg));
            });
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        User user = User.of(player);
        event.quitMessage(null);
        Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
            String rawQuitMsg = user.getCurrentRang().quitMsg().replace("{display-name}", user.getDisplayNameFor(onlinePlayer));
            onlinePlayer.sendMessage(plugin.miniMessage().deserialize(rawQuitMsg));
        });
    }

}
