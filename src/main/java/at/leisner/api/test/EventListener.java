package at.leisner.api.test;

import at.leisner.api.API;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

public class EventListener implements Listener {
    private final API plugin;
    private final ResourceLocation arrowKey = new ResourceLocation("customentity", "homing_arrow");

    public EventListener(API plugin) {
        this.plugin = plugin;
    }

//    @EventHandler(ignoreCancelled = true)
//    public void onPlayerLogin(PlayerSwapHandItemsEvent event) {
//        Player player = event.getPlayer();
//        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
//        Location location = player.getLocation();
//        ServerLevel level = ((CraftWorld) player.getWorld()).getHandle();
//        HomingArrow homingArrow = new HomingArrow(level);
//        homingArrow.setTarget(HomingArrow.findTarget(serverPlayer,level));
//        homingArrow.setPos(location.x(), location.y(), location.z());
//        level.addFreshEntity(homingArrow);
//    }
}
