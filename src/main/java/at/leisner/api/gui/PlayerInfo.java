package at.leisner.api.gui;

import at.leisner.api.API;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class PlayerInfo implements InventoryHolder {

    private final Inventory inventory;
    private final API plugin;

    public PlayerInfo(API plugin) {
        // Create an Inventory with 9 slots, `this` here is our InventoryHolder.
        this.inventory = plugin.getServer().createInventory(this, 9);
        this.plugin = plugin;
    }

    @Override
    public Inventory getInventory() {
        return this.inventory;
    }

}
