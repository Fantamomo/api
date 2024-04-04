package at.leisner.api.util;

import at.leisner.api.Api;
import at.leisner.api.rang.Rang;
import at.leisner.api.vanish.VanishManager;
import com.destroystokyo.paper.event.server.AsyncTabCompleteEvent;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Formater implements Listener {
    private static List<String> PLACEHOLDERS = new ArrayList<>();
    private static List<String> KEYS = new ArrayList<>();
    private static List<String> CUSTOM_PLACEHOLDER = new ArrayList<>();

    static {
        PLACEHOLDERS.add("%player%");
        PLACEHOLDERS.add("%health%");
        PLACEHOLDERS.add("%food%");
        PLACEHOLDERS.add("%gamemode%");
        PLACEHOLDERS.add("%host_name%");
        PLACEHOLDERS.add("%client_name%");
        PLACEHOLDERS.add("%ping%");
        PLACEHOLDERS.add("%player_x%");
        PLACEHOLDERS.add("%player_y%");
        PLACEHOLDERS.add("%player_z%");
        PLACEHOLDERS.add("%world_name%");
        PLACEHOLDERS.add("%world_seed%");
        PLACEHOLDERS.add("%world_player_count%");
        PLACEHOLDERS.add("%player_item_on_cursor%");
        PLACEHOLDERS.add("%player_xp%");
        PLACEHOLDERS.add("%player_uuid%");
        PLACEHOLDERS.add("%motd%");

        KEYS.add("isSneaking");
        KEYS.add("isVanish");
        KEYS.add("isFlying");
        KEYS.add("canFly");
        KEYS.add("isSprinting");
        KEYS.add("isFrozen");
        KEYS.add("permission/");
        KEYS.add("isOp");
        KEYS.add("isWhitelisted");
        for (String s : KEYS) PLACEHOLDERS.add("%" + (s.contains("/") ? s.split("/")[0]+"/[args]" : s) + ":[true]:[false]%");

//        CUSTOM_PLACEHOLDER.add("rainbow");
//        for (String s : KEYS) PLACEHOLDERS.add("%" + s + ":[string]%");
    }

    private static boolean checkCondition(String key, Player p) throws IllegalArgumentException{
        switch (key) {
            case "isSneaking":
                return p.isSneaking();
            case "isVanish":
                return Api.getInstance().getVanishManager().isVanish(p);
            case "isFlying":
                return p.isFlying();
            case "canFly":
                return p.getAllowFlight();
            case "isSprinting":
                return p.isSprinting();
            case "isFrozen":
                return Api.getInstance().getFreezeManager().isFrozen(p.getUniqueId());
            case "isOp":
                return p.isOp();
            case "isWhitelisted":
                return p.isWhitelisted();
        }
        if (key.startsWith("permission/")) {
            String permission = key.replace("permission/","");
            return p.hasPermission(permission);
        }
        throw new IllegalArgumentException();
    }
    public static String formatPlayer(String msg, Player p) {
        msg = msg.replace("%player%", p.getName())
                .replace("%health%", String.valueOf(Math.round(p.getHealth())))
                .replace("%food%", String.valueOf(p.getFoodLevel()))
                .replace("%gamemode%", p.getGameMode().toString())
                .replace("%host_name%", Objects.requireNonNull(p.getAddress()).getHostName())
                .replace("%client_name%", Objects.requireNonNull(p.getClientBrandName()))
                .replace("%ping%", String.valueOf(p.getPing()))
                .replace("%player_uuid%", String.valueOf(p.getUniqueId()))
                .replace("%player_x%", String.valueOf(p.getX()))
                .replace("%player_y%", String.valueOf(p.getY()))
                .replace("%player_z%", String.valueOf(p.getZ()))
                .replace("%player_xp%", String.valueOf(p.getExp()))
                .replace("%player_item_on_cursor%", p.getItemOnCursor().displayName().toString())
        ;
        return formatWorld(formatRe(msg, p), p.getWorld());
    }

    public static String formatWorld(String msg, World w) {
        msg = msg.replace("%world_name%", w.getName())
                .replace("%world_seed%", String.valueOf(w.getSeed()))
                .replace("%world_player_count%", String.valueOf(w.getPlayerCount()));
        return msg;
    }

    public static String formatString(String s) {
        Api plugin = Api.getInstance();
        return s.replace("%server_name%", plugin.getServer().getName())
                .replace("%bukkit_version%", plugin.getServer().getBukkitVersion())
                .replace("%server_ip%", plugin.getServer().getIp())
                .replace("%version%", plugin.getServer().getVersion())
                .replace("%motd%", plugin.getServer().getMotd())
                .replace("%plugin_name%", plugin.getName())
                .replace("%vanish_player_count%", Integer.toString(VanishManager.getVanishPlayersCount()))
                ;

    }

    public static String formatMessage(Player player, String message) {
        Rang rang = Api.getInstance().getRangManager().findRangForPlayer(player);
        String formattedMessage;
        if (rang != null) {
            // Wenn der Spieler einen Rang hat, verwende das Format des Rangs
            formattedMessage = formatString(rang.getChatFormat())
                    .replace("%prefix%", rang.getPrefix())
                    .replace("%player_color%", rang.getPlayerColor())
                    .replace("%player%", player.getName())
                    .replace("%suffix%", rang.getSuffix())
                    .replace("%msg_color%", rang.getMsgColor())
                    .replace("%msg%", message);
        } else {
            // Standardformat, wenn der Spieler keinen Rang hat
            formattedMessage = player.getName() + ": &7" + message;
        }
        // Übersetze die Farbcodes und gebe die formatierte Nachricht zurück
        return ChatColor.translateAlternateColorCodes('&', player.hasPermission("chat.placeholder.format") ? formatPlayer(formattedMessage, player) : formattedMessage);
    }

    public static String formatRe(String msg, Player p) {
        Pattern pattern = Pattern.compile("%(.*?):(.*?):(.*?)%");
        Matcher matcher = pattern.matcher(msg);

        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String key = matcher.group(1);
            if (KEYS.stream().noneMatch(key::startsWith)) continue;
            String platzhalter1 = matcher.group(2);
            String platzhalter2 = matcher.group(3);
            try {
                matcher.appendReplacement(sb, checkCondition(key, p) ? platzhalter1 : platzhalter2);
            } catch (IllegalArgumentException ignored) {}
        }
        matcher.appendTail(sb);
        msg = sb.toString();

//        pattern = Pattern.compile("%(.*?):(.*?)%");
//        matcher = pattern.matcher(msg);
//
//        sb = new StringBuffer();
//        while (matcher.find()) {
//            String key = matcher.group(1);
//            if (!CUSTOM_PLACEHOLDER.contains(key)) continue;
//            String platzhalter1 = matcher.group(2);
//
//            matcher.appendReplacement(sb, customPlaceholder(key, platzhalter1));
//        }
//        matcher.appendTail(sb);
//        msg = sb.toString();

        return msg;
    }
//    private static String customPlaceholder(String key, String s1) {
//        switch (key) {
//            case "rainbow":
//                Random random = new Random();
//                int start = random.nextInt(16)+1;
//        }
//        return "";
//    }

    public static String formatMessageForRang(String playerName, String rangName, String message) {
        Rang rang = Api.getInstance().getRangManager().getRangByName(rangName);
        if (rang != null) {
            String formattedMessage = Formater.formatString(rang.getChatFormat())
                    .replace("%prefix%", rang.getPrefix())
                    .replace("%player_color%", rang.getPlayerColor())
                    .replace("%player%", playerName)
                    .replace("%msg_color%", rang.getMsgColor())
                    .replace("%msg%", message);
            return ChatColor.translateAlternateColorCodes('&', formattedMessage);
        }
        return message;
    }

//    @EventHandler
//    public void onTabComplete(TabCompleteEvent event) {
//        if (!event.getSender().hasPermission("chat.placeholder.TabComplete")) return;
//        String buffer = event.getBuffer().trim();
//        String[] words = buffer.split(" ");
//        String lastWord = words[words.length - 1];
//
//        // Prüfen, ob das letzte Wort mit einem "%" beginnt oder ein Teil eines Platzhalters ist
//        if (lastWord.startsWith("%") || (PLACEHOLDERS.stream().anyMatch(ph -> ph.startsWith(lastWord)) && lastWord.startsWith("%"))) {
//            List<String> completions = new ArrayList<>();
//            for (String placeholder : PLACEHOLDERS) {
//                if (placeholder.startsWith(lastWord)) {
//                    completions.add(placeholder);
//                }
//            }
//            event.setCompletions(completions);
//        }
//    }

    @EventHandler(ignoreCancelled = true /*, priority = EventPriority.HIGHEST*/)
    public void onAsyncTabComplete(AsyncTabCompleteEvent event) {
        if (!event.getSender().hasPermission("chat.placeholder.TabComplete")) return;
        String buffer = event.getBuffer().trim();
        String[] words = buffer.split(" ");
        String lastWord = words[words.length - 1];

        // Prüfen, ob das letzte Wort mit einem "%" beginnt oder ein Teil eines Platzhalters ist
        if (lastWord.startsWith("%") || (PLACEHOLDERS.stream().anyMatch(ph -> ph.startsWith(lastWord)) && lastWord.startsWith("%"))) {
            List<String> completions = new ArrayList<>();
            for (String placeholder : PLACEHOLDERS) {
                if (placeholder.startsWith(lastWord)) {
                    completions.add(placeholder);
                }
            }
            event.setCompletions(completions);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        if (!event.getPlayer().hasPermission("chat.placeholder.format")) return;
        if (!event.getMessage().endsWith(" -p")) return;
        event.setMessage(formatPlayer(event.getMessage().substring(0,event.getMessage().length()-3), event.getPlayer()));
    }

}
