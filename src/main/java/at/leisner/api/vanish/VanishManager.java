package at.leisner.api.vanish;

import at.leisner.api.Api;
import at.leisner.api.util.Translator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

public class VanishManager {
    private static Set<Player> vanishPlayers = new HashSet<>();
    private Set<Player> canPickUpItems = new HashSet<>();
    public final String vanishPrefix;
    public VanishManager() {
        vanishPrefix = Translator.translate("vanish.prefix");
    }
    public boolean isVanish(Player p) {
        return vanishPlayers.contains(p);
    }

    public boolean setVanishState(Player p, boolean state) {
        if (isVanish(p) == state) return false;
        if (state) hidePlayer(p);
        else showPlayer(p);
        return true;
    }
    public boolean toggleVanish(Player p) {
        setVanishState(p, !isVanish(p));
        return isVanish(p);
    }
    public static int getVanishPlayersCount() {
        return vanishPlayers.size();
    }
    public void sendVanishMsg(Player to,String msg) {
        to.sendMessage(vanishPrefix+" "+msg);
    }
    public void sendVanishMsg(Player to) {
        to.sendMessage(vanishPrefix+" "+ (isVanish(to) ? ChatColor.GREEN+"Du bist im Vanish modus" : ChatColor.RED+"Du bist nicht mehr im Vanish modus"));
    }

    public void setItemPickup(Player p, boolean state) {
        if (state) canPickUpItems.add(p);
        else canPickUpItems.remove(p);
    }
    public boolean canPickUpItems(Player p) {
        return canPickUpItems.contains(p);
    }

    public void sendVanishMsg(CommandSender to, String msg) {
        to.sendMessage(vanishPrefix+" "+msg);
    }
    private void hidePlayer(Player p) {
        Api plugin = Api.getInstance();
        vanishPlayers.add(p);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == p) continue;
            if (couldSee(online,p)) online.hidePlayer(plugin,p);
        }
    }

    private void showPlayer(Player p) {
        Api plugin = Api.getInstance();
        vanishPlayers.remove(p);
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == p) continue;
            online.showPlayer(plugin,p);
        }
    }

    public Set<Player> getVanishPlayers() {
        return vanishPlayers;
    }

    public List<String> listOfSeenPlayerFrom(Player p) {
        List<String> seenPlayers = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == p) continue;
            if (p.canSee(online)) seenPlayers.add(p.getName());
        }
        return seenPlayers;
    }

    public List<Player> canSee(Player p) {
        List<Player> seenPlayers = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (online == p) continue;
            if (online.canSee(p)) seenPlayers.add(online);
        }
        return seenPlayers;
    }

    public static boolean couldSee(Player viewer, Player viewed) {
        int viewerSeeHighest = highestSeeLevel(viewer);
        int viewedUseHighest = highestUseLevel(viewed);
        return viewerSeeHighest >= viewedUseHighest;
    }
    public void hideOnlyPlayer(Player playerToHide, Player fromPlayer) {
        fromPlayer.hidePlayer(Api.getInstance(), playerToHide);
    }
    public void showOnlyPlayer(Player playerToShow, Player fromPlayer) {
        fromPlayer.showPlayer(Api.getInstance(), playerToShow);
    }
    private static int highestSeeLevel(Player p) {
        int highestLevel = -1;
        for (int level = 0; level <= 10; level++) {
            if (p.hasPermission("vanish.level.see."+level)) highestLevel = level;
        }
        return highestLevel;
    }
    private static int highestUseLevel(Player p) {
        int highestLevel = 0;
        for (int level = 0; level <= 10; level++) {
            if (p.hasPermission("vanish.level.use."+level)) highestLevel = level;
        }
        return highestLevel;
    }
    public static int countOfPlayerWithOutVanish() {
        return Api.getInstance().getServer().getOnlinePlayers().size()-vanishPlayers.size();
    }
    public static List<Player> playersThatAVisibleFrom(Player viewer) {
        List<Player> players = new ArrayList<>();
        for (Player online : Bukkit.getOnlinePlayers()) {
            if (viewer.canSee(online)) {
                players.add(online);
            }
        }
        return players;
    }
}
