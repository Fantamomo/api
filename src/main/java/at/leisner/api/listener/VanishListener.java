package at.leisner.api.listener;

import at.leisner.api.API;
import at.leisner.api.user.User;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import com.destroystokyo.paper.network.StandardPaperServerListPingEventImpl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.server.ServerListPingEvent;

public class VanishListener implements Listener {
    private final API plugin;

    public VanishListener(API plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!User.of(player).vanishPlayerData.isVanish()) return;
        if (!User.of(player).getVanishPlayerData().canPickUpItems()) event.setCancelled(true);
    }

    @EventHandler(ignoreCancelled = true)
    public void onServerListPing(ServerListPingEvent event) {
    }

    @EventHandler(ignoreCancelled = true)
    public void onPaperServerListPing(PaperServerListPingEvent event) {
    }

    @EventHandler(ignoreCancelled = true)
    public void onStandardPaperServerListPingImpl(StandardPaperServerListPingEventImpl event) {
    }
}
