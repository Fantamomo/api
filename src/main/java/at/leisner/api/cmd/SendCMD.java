package at.leisner.api.cmd;

import at.leisner.api.Api;
import at.leisner.api.util.Formater;
import at.leisner.api.chat.ChatManager;
import at.leisner.api.rang.RangManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SendCMD extends Command {
    private RangManager rangManager;

    public SendCMD() {
        super("send");
        this.rangManager = Api.getInstance().getRangManager();
        this.setPermission("cmd.send");
    }

    public boolean onCommand(CommandSender sender, String label, String[] args) {
        if (args.length < 3) {
            sender.sendMessage(ChatColor.RED + "Benutzung: /send <Spieler> <Rang> <Nachricht...> [JSON]");
            return true;
        }

        String playerName = args[0];
        String rangName = args[1];
        String jsonStr = args[args.length - 1];
        JsonObject json = null;

        // Versuche, den letzten Parameter als JSON zu parsen, falls es wie JSON aussieht
        if (jsonStr.startsWith("{") && jsonStr.endsWith("}")) {
            try {
                json = new Gson().fromJson(jsonStr, JsonObject.class);
                // Entferne den JSON-String von den Nachrichtenargumenten, falls erfolgreich geparst
                args = Arrays.copyOf(args, args.length - 1);
            } catch (JsonParseException e) {
                // Fehler beim Parsen, behandele den letzten Teil als Teil der Nachricht
                json = null; // Stelle sicher, dass json null ist, wenn das Parsen fehlschlägt
            }
        }
        String message = String.join(" ", Arrays.copyOfRange(args, 2, args.length));

        // Prüfe, ob der Ziel-Spieler die Berechtigung "chat.sendas.bypass" hat
        Player targetPlayer = Bukkit.getPlayerExact(playerName);
        if ((targetPlayer != null && targetPlayer.hasPermission("chat.sendas.bypass")) && !(sender.hasPermission("chat.sendas.all"))) {
            sender.sendMessage(ChatColor.RED + "Du kannst keine Nachrichten als dieser Spieler senden.");
            return true;
        }
        if (targetPlayer != null) {
            message = Formater.formatPlayer(message,targetPlayer);
        } else {
            message = Formater.formatString(message);
        }

        // Formatierung der Basisnachricht unter Berücksichtigung des angegebenen Rangs
        String baseFormattedMessage = Formater.formatMessageForRang(playerName, rangName, message);

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            boolean shouldShowFake = player.hasPermission("chat.fake");
            String prefix = shouldShowFake ? ChatColor.RED + "[FAKE] " : "";
            String hoverText = shouldShowFake ? ChatColor.RED + "Diese Nachricht wurde tatsächlich von " + ChatColor.GOLD + ((sender instanceof Player) ? Api.getInstance().getRangManager().getDisplayName((Player) sender) : sender.getName()) + ChatColor.RED + " gesendet." : "";

            // Erstelle die Basis-Nachricht, die an alle gesendet wird
            String formattedMessage = prefix + baseFormattedMessage;
            TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(formattedMessage));

            if (shouldShowFake && sender instanceof Player) {
                // Setze das Hover-Event nur für Spieler, die "chat.fake" haben
                messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
            } else if (player.hasPermission("chat.info") && json != null) {
                // Für Spieler ohne "chat.fake", aber mit "chat.info", zeige die Fake-Daten an
                String fakeHoverText = createHoverTextFromJson(json);
                messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(fakeHoverText).create()));
            }

            player.spigot().sendMessage(messageComponent);
        }

// Konsole Nachricht
        Bukkit.getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[CHAT] " + ChatColor.RED + "[FAKE<" + ChatColor.GOLD + sender.getName() + ChatColor.RED + ">]" + baseFormattedMessage);


        return true;
    }

    private String createHoverTextFromJson(JsonObject json) {
        StringBuilder hoverTextBuilder = new StringBuilder();
        if (json.has("name")) {
            hoverTextBuilder.append(ChatColor.GOLD + "Display Name: " + ChatColor.GREEN).append(json.get("name").getAsString()).append("\n");
        }
        if (json.has("leben")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Leben: "+ChatColor.RED).append(json.get("leben").getAsFloat()).append("\n");
        }
        if (json.has("welt")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Welt: "+ChatColor.GREEN).append(json.get("welt").getAsString()).append("\n");
        }
        if (json.has("gamemode")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Gamemode: "+ChatColor.GREEN).append(json.get("gamemode").getAsString()).append("\n");
        }
        if (json.has("pos")) {
            JsonObject pos = json.getAsJsonObject("pos").getAsJsonObject();
            int x = pos.has("x") ? pos.get("x").getAsInt() : 0;
            int y = pos.has("y") ? pos.get("y").getAsInt() : 0;
            int z = pos.has("z") ? pos.get("z").getAsInt() : 0;
            hoverTextBuilder.append(ChatColor.GOLD+"Position: "+ChatColor.GREEN+"X: ").append(x).append(ChatColor.GREEN+" Y: ").append(y).append(ChatColor.GREEN+" Z: ").append(z).append("\n");
        }
        if (json.has("ping")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Ping: "+ChatColor.GREEN).append(json.get("ping").getAsInt()).append("\n");
        }
        if (json.has("saturation")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Saturation: "+ChatColor.GREEN).append(json.get("saturation").getAsFloat()).append("\n");
        } else if (json.has("s")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Saturation: "+ChatColor.GREEN).append(json.get("s").getAsFloat()).append("\n");
        }
        if (json.has("tode")) {
            hoverTextBuilder.append(ChatColor.GOLD+"Tode: "+ChatColor.GREEN).append(json.get("tode").getAsInt()).append("\n");
        }
//        if (json.has("rang")) {
//            hoverTextBuilder.append(ChatColor.GOLD+"Rang: "+ChatColor.GREEN).append(json.get("rang").getAsString()).append("\n");
//        }

        hoverTextBuilder.replace(hoverTextBuilder.length()-1,hoverTextBuilder.length(),"");
        return hoverTextBuilder.toString();

    }
    public List<String> onTabComplete(CommandSender sender, String alias, String[] args) {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream()
                    .filter(player -> !player.hasPermission("chat.sendas.bypass"))
                    .map(Player::getName)
                    .collect(Collectors.toList());
        } else if (args.length == 2) {
            return new ArrayList<>(rangManager.getAvailableRanks());
        }

        return new ArrayList<>();
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return onCommand(commandSender,s,strings);
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        return onTabComplete(sender, alias, args);
    }
}
