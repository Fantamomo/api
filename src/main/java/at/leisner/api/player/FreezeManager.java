package at.leisner.api.player;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDropItemEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class FreezeManager implements Listener {
    public static List<UUID> frozenPlayers = new ArrayList<>();

    public boolean isFrozen(UUID uuid) {
        return frozenPlayers.contains(uuid);
    }
    public void addFrozenPlayer(UUID uuid) {
        boolean b = !frozenPlayers.contains(uuid) && frozenPlayers.add(uuid);
    }
    public void removeFrozenPlayer(Player p) {
        frozenPlayers.remove(p);
    }
    public boolean toggleState(Player p) {
        if (frozenPlayers.contains(p.getUniqueId())) {
            frozenPlayers.remove(p.getUniqueId());
        } else {
            frozenPlayers.add(p.getUniqueId());
        }
        return frozenPlayers.contains(p.getUniqueId());
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (frozenPlayers.contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (frozenPlayers.contains(event.getPlayer().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDropItem(EntityDropItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (frozenPlayers.contains(event.getEntity().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryDragEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (frozenPlayers.contains(event.getWhoClicked().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityPickupItem(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (frozenPlayers.contains(event.getEntity().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (frozenPlayers.contains(event.getWhoClicked().getUniqueId())) event.setCancelled(true);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        if (frozenPlayers.contains(event.getWhoClicked().getUniqueId())) event.setCancelled(true);
    }
}
