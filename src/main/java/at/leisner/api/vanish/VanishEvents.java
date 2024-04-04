package at.leisner.api.vanish;

import at.leisner.api.Api;
import at.leisner.api.rang.RangManager;
import at.leisner.api.util.Formater;
import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import io.papermc.paper.event.player.PlayerPickItemEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class VanishEvents implements Listener {
    private Api plugin;
    private VanishManager vanishManager;
    public VanishEvents() {
        plugin = Api.getInstance();
        vanishManager = plugin.getVanishManager();
    }

    @EventHandler
    public void onPlayerPickItem(PlayerPickItemEvent event) {
        if (vanishManager.isVanish(event.getPlayer())) event.setCancelled(true);
    }
    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("cmd.vanish.VanishOnJoin") || Api.getInstance().getPlayerDataManager().getPlayerData(player).isVanish()){
            vanishManager.setVanishState(player,true);
            event.joinMessage(Component.empty());
            player.sendMessage(vanishManager.vanishPrefix+ ChatColor.GOLD+"Du bist dem Spiel leise beigetreten!");
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (VanishManager.couldSee(onlinePlayer, player) && onlinePlayer.hasPermission("cmd.vanish.joinNotify")) {
                    onlinePlayer.sendMessage(vanishManager.vanishPrefix+ RangManager.getDisplayName(player)+ChatColor.YELLOW+" ist dem Spiel leise beigetreten!");
                }
            }

        } else {
            event.joinMessage(Component.text(Formater.formatMessage(player, plugin.getRangManager().findRangForPlayer(player).getJoinMSG())));
//            Rang rang = chatManager.getRang(event.getPlayer());
//            if (rang == null) event.setJoinMessage(ChatColor.YELLOW+player.getName()+ChatColor.GRAY+" ist dem Spiel beigetreten!");
//            else {
//                String joinMsg = rang.getJoinMSG();
//                joinMsg = joinMsg.replace("%player%", player.getName())
//                        .replace("%prefix", rang.getPrefix())
//                        .replace("%world%", player.getWorld().getName());
//                event.setJoinMessage(ChatColor.translateAlternateColorCodes('&',joinMsg));
        }

        if (!vanishManager.getVanishPlayers().isEmpty()) {
            for (Player vanishPlayer : vanishManager.getVanishPlayers()) {
                if (!VanishManager.couldSee(event.getPlayer(), vanishPlayer)) {
                    vanishManager.hideOnlyPlayer(event.getPlayer(), vanishPlayer);
                }
            }
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (vanishManager.isVanish(player)) {
            event.quitMessage(Component.empty());
            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                if (VanishManager.couldSee(onlinePlayer, player) && onlinePlayer.hasPermission("cmd.vanish.quitNotify")) {
                    onlinePlayer.sendMessage(vanishManager.vanishPrefix+ RangManager.getDisplayName(player)+ChatColor.YELLOW+" hat das Spiel leise verlassen!");
                }
            }
        } else {
            event.quitMessage(Component.text(Formater.formatMessage(player, plugin.getRangManager().findRangForPlayer(player).getQuitMSG())));
//            Rang rang = chatManager.getRang(event.getPlayer());
//            if (rang == null) event.setQuitMessage(ChatColor.YELLOW+player.getName()+ChatColor.GRAY+" hat das Spiel verlassen!");
//            else {
//                String quitMSG = rang.getQuitMSG();
//                quitMSG = Format.formatPlayer(quitMSG,player);
//                event.setQuitMessage(ChatColor.translateAlternateColorCodes('&',quitMSG));
//            }
        }
    }
    @EventHandler
    public void onItemGet(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!vanishManager.canPickUpItems(((Player) event.getEntity())) && vanishManager.isVanish(((Player) event.getEntity()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (vanishManager.isVanish(((Player) event.getEntity()))) event.setCancelled(true);
    }

    @EventHandler
    public void onAreaEffectCloudApply(AreaEffectCloudApplyEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (vanishManager.isVanish(((Player) event.getEntity()))) event.setCancelled(true);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        if (!(event.getTarget() instanceof Player)) return;
        if (vanishManager.isVanish(((Player) event.getTarget()))) event.setCancelled(true);
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (vanishManager.isVanish(((Player) event.getEntity()))) event.setCancelled(true);
    }

    @EventHandler
    public void onPaperServerListPing(PaperServerListPingEvent event) {
        event.setNumPlayers(VanishManager.countOfPlayerWithOutVanish());
//        if (!event.getAddress().getHostName().equals("127.0.0.1"));
    }

}
