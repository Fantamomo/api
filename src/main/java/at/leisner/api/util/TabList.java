package at.leisner.api.util;

import at.leisner.api.Api;
import at.leisner.api.rang.RangManager;
import at.leisner.api.vanish.VanishManager;
import io.papermc.paper.event.player.AsyncChatCommandDecorateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class TabList {
    Api plugin = Api.getInstance();

    public TabList() {
        Bukkit.getScheduler().runTaskTimer(plugin, this::update, 0L, 3L);
    }

    private void update() {
        for (Player online : Bukkit.getOnlinePlayers()) {
            String header = ChatColor.GREEN + "minecraft.leisner.at\n" + ChatColor.BLUE + "Online Players: " + ChatColor.GOLD + VanishManager.playersThatAVisibleFrom(online).size();
            String footer = ChatColor.DARK_BLUE + "Dein Rang: " + ChatColor.GRAY + ChatColor.translateAlternateColorCodes('&', plugin.getRangManager().findRangForPlayer(online).getName()) +
                    ChatColor.RESET + ChatColor.RED + "\nPing: " + ChatColor.YELLOW + online.getPing();
            if (online.hasPermission("tablist.info")) {
                header += ChatColor.AQUA + "\nVanish Players: " + ChatColor.DARK_PURPLE + plugin.getVanishManager().getVanishPlayers().size();
                footer += /*ChatColor.RED + "\nTPS: " + ChatColor.GOLD + tps +*/
                        ChatColor.BLUE + "\nKoordinaten: " + ChatColor.GREEN + online.getLocation().getBlockX() + " " + online.getLocation().getBlockY() + " " + online.getLocation().getBlockZ();
            }
            String name = (plugin.getVanishManager().isVanish(online) ? plugin.getVanishManager().vanishPrefix : "") +ChatColor.GRAY+RangManager.getDisplayName(online);
            online.sendPlayerListHeaderAndFooter(Component.text(header),Component.text(footer));
            online.playerListName(Component.text(name));

        }
    }
}
