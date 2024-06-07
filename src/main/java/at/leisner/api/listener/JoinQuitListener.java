package at.leisner.api.listener;

import at.leisner.api.API;
import at.leisner.api.user.User;
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
        Player player = event.getPlayer();
        PermissionAttachment attachment = player.addAttachment(plugin);
        plugin.getPermissionManager().getPlayerPermissionAttachmentMap().put(player, attachment);
        User user = User.of(player);

        // Lade die Berechtigungen aus der Datenbank und setze sie für den Spieler
        Map<String, Boolean> permissions = user.getPermissions();
        for (Map.Entry<String, Boolean> entry : permissions.entrySet()) {
            attachment.setPermission(entry.getKey(), entry.getValue());
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
    }

}
