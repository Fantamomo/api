package at.leisner.api.chat;

import at.leisner.api.Api;
import at.leisner.api.rang.Rang;
import at.leisner.api.util.Formater;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

public class ChatManager {
    public static String getPlayerInfoForHover(Player player) {
        String hoverText = ChatColor.GOLD + "Display Name: " + ChatColor.GREEN + player.getDisplayName();
        hoverText += "\n" + ChatColor.GOLD + "Leben: " + ChatColor.RED + player.getHealth();
        hoverText += "\n" + ChatColor.GOLD + "Welt: " + ChatColor.GREEN + player.getWorld().getName();
        hoverText += "\n" + ChatColor.GOLD + "Gamemode: " + ChatColor.GREEN + player.getGameMode().name();
        hoverText += "\n" + ChatColor.GOLD + "Position: " + ChatColor.GREEN + "X: " + player.getLocation().getBlockX() + " Y: " + player.getLocation().getBlockY() + " Z: " + player.getLocation().getBlockZ();
        hoverText += "\n" + ChatColor.GOLD + "Ping: " + ChatColor.GREEN + player.getPing();
        //hoverText += "\n" + ChatColor.GOLD + "Hunger: " + ChatColor.GREEN + player.get();
        hoverText += "\n" + ChatColor.GOLD + "Saturation: " + ChatColor.GREEN + player.getSaturation();
        hoverText += "\n" + ChatColor.GOLD + "Tode: " + ChatColor.GREEN + player.getStatistic(Statistic.DEATHS);

        return hoverText;
    }


}
