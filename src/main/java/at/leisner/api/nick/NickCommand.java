package at.leisner.api.nick;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.rang.Rang;
import at.leisner.api.user.CommandSenderUser;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class NickCommand extends Command {

    private final API plugin;
    private final Language language;
    private List<String> arg0 = new ArrayList<>(List.of(""));

    public NickCommand(API plugin) {
        super("nick");
        this.plugin = plugin;
        language = plugin.getLanguage();
    }

    @Override
    public boolean execute(CommandSender senderTemp, String label, String[] args) {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        if (!sender.controlPermission("api.command.nick", "commands.nick.permission_error")) return true;
        if (args.length == 0) {
            sender.sendMessage("commands.nick.usage", Key.of("usage", "/nick <action> <args...>"));
            return false;
        }
        switch (args[0]) {
            case "set" -> {
                if (!sender.controlPermission("api.command.nick.set", "commands.nick.permission_error")) return true;
                if (senderTemp instanceof Player player) {
                    if (args.length > 3) {
                        sender.sendMessage("commands.nick.usage", Key.of("usage", "/nick set <nickName> [skinURL]"));
                        return false;
                    }
                    String nickName = args[1];
                    String skinUrl = args.length == 3 ? args[2] : null;
                    if (!plugin.getNickManager().isNameFree(nickName)) {
                        sender.sendMessage("commands.nick.not-available");
                        return true;
                    }

                    plugin.getNickManager().nickPlayer(player, nickName, skinUrl);
                    sender.sendMessage("commands.nick.success-self", Key.of("nick-name", nickName));
                    return true;
                }
                sender.sendMessage("commands.nick.need_to_be_player");
            }
            case "list" -> {
                if (!sender.controlPermission("api.command.nick.list", "commands.nick.permission_error")) return true;
                StringBuilder sb = new StringBuilder();
                sb.append(sender.translate("commands.nick.list.start"));
                boolean first = true;
                for (Player p : plugin.getNickManager().getNickedPlayers()) {
                    if (!first) {
                        sb.append(", ");
                    }
                    first = false;
                    sb.append(sender.translate("commands.nick.list.format", false,
                            Key.of("nick-name", p.getName()),
                            Key.of("original-name", User.of(p).getDisplayNameFor(sender.player()))));
                }
                sender.sendMessage(sb.toString());
            }
            case "info" -> {
                if (!sender.controlPermission("api.command.nick.info", "commands.nick.permission_error")) return true;
                if (args.length < 2) {
                    sender.sendMessage("commands.nick.usage", Key.of("usage", "/nick info <name>"));
                    return false;
                }
                if (plugin.getNickManager().getPlayerByInGameName(args[1]) == null) {
                    sender.sendMessage("commands.nick.player_not_found");
                    return true;
                }
                Player player = plugin.getNickManager().getPlayerByInGameName(args[1]);
                User user = User.of(player);
                String nickName = user.getNickName();
                if (nickName == null) {
                    sender.sendMessage("commands.nick.not-nicked-other");
                    return false;
                } else {
                    sender.sendMessage("commands.nick.info", Key.of("original-name", user.getDisplayNameFor(sender.player())), Key.of("nick-name", nickName));
                    return true;
                }
            }
            case "reset" -> {
                if (args.length > 1) {
                    if (!sender.controlPermission("api.command.nick.reset_other", "commands.nick.permission_error"))
                        return true;
                    if (plugin.getNickManager().getPlayerByInGameName(args[1]) == null) {
                        sender.sendMessage("commands.nick.player_not_found");
                        return true;
                    }
                    if (User.of(plugin.getNickManager().getPlayerByInGameName(args[1])).getNickName() == null) {
                        sender.sendMessage("commands.nick.not-nicked-other");
                        return true;
                    }
                    Player toReset = plugin.getNickManager().getPlayerByInGameName(args[1]);
                    plugin.getNickManager().resetNick(toReset);
                    sender.sendMessage("commands.nick.reset-other", Key.of("nick-name", args[1]), Key.of("original-name", User.of(toReset).getDisplayNameFor(sender.player())));
                    return true;
                } else {
                    if (senderTemp instanceof Player player) {
                        if (!sender.controlPermission("api.command.nick.reset", "commands.nick.permission_error"))
                            return true;
                        if (User.of(player).getNickName() == null) {
                            sender.sendMessage("commands.nick.not-nicked-self");
                            return true;
                        }
                        plugin.getNickManager().resetNick(player);
                        sender.sendMessage("commands.nick.reset-self");
                        return true;
                    }
                    sender.sendMessage("commands.nick.need_to_be_player");
                }
            }
            case "set_other" -> {
                if (!sender.controlPermission("api.command.nick.set_other", "commands.nick.permission_error"))
                    return true;
                if (args.length <= 2) {
                    sender.sendMessage("commands.nick.usage", Key.of("usage", "/nick set_other <name> <nickName> [skinURL]"));
                    return false;
                }
                if (plugin.getNickManager().getPlayerByInGameName(args[1]) == null) {
                    sender.sendMessage("commands.nick.player_not_found");
                    return true;
                }
                Player player = plugin.getNickManager().getPlayerByInGameName(args[1]);
                String nickName = args[2];
                String skin = args.length > 3 ? args[3] : null;
                if (!plugin.getNickManager().isNameFree(nickName)) {
                    sender.sendMessage("commands.nick.not-available");
                    return true;
                }
                plugin.getNickManager().nickPlayer(player, nickName, skin);
                sender.sendMessage("commands.nick.success-other",
                        Key.of("original-name", User.of(player).getOriginalName()),
                        Key.of("nick-name", nickName)
                );
                player.sendMessage(User.of(player).getLanguage().translate("commands.nick.success-self", Key.of("nick-name", nickName)));
            }
            case "fake_rang" -> {
                if (!sender.controlPermission("api.command.nick.fake_rang", "commands.nick.permission_error")) return true;
                if (args.length == 2) {
                    String newFakeRang = args[1];
                    Rang rang = plugin.getRangManager().getRang(newFakeRang);
                    if (rang == null) {
                        sender.sendMessage("commands.user.wrong_argument", Key.of("arg", "2"));
                    } else {
                        if (senderTemp instanceof Player player) {
                            User user = User.of(player);
                            user.setFakeRang(rang);
                            user.sendTranslateMessage("commands.nick.set-fake-rang", Key.of("rang", rang.name()));
                        } else {
                            sender.sendMessage("commands.nick.need_to_be_player");
                        }
                    }
                }
            }
            case "set_skin" -> {
                if (!sender.controlPermission("api.command.nick.set_skin", "commands.nick.permission_error")) return true;
                if (!sender.isPlayer()) {
                    sender.sendMessage("commands.nick.need_to_be_player");
                    return true;
                }
                if (args.length == 2) {
                    String newSkin = args[1];
                    if (plugin.getNickManager().isPlayerNicked(sender.player())) {
                        plugin.getNickManager().changeSkin(sender.user().getServerPlayer().gameProfile, newSkin);
                        sender.sendMessage("commands.nick.set-skin", Key.of("skin", newSkin));
                        for (Player p : Bukkit.getOnlinePlayers()) {
                            if (p.canSee(sender.player())) {
                                p.hidePlayer(plugin, sender.player());
                                p.showPlayer(plugin, sender.player());
                            }
                        }
                        return true;
                    }
                    sender.sendMessage("commands.nick.not-nicked-self");
                }
            }
            case "reload_skin" -> {
                if (!sender.controlPermission("api.command.nick.reload_skin", "commands.nick.permission_error")) return true;
                if (!sender.isPlayer()) {
                    sender.sendMessage("commands.nick.need_to_be_player");
                    return true;
                }
                plugin.getNickManager().reloadPlayer((CraftPlayer) sender.player());
                if (plugin.getNickManager().isPlayerNicked(sender.player())) {
                    sender.sendMessage("commands.nick.reload-skin");
                } else {
                    sender.sendMessage("commands.nick.real-skin");
                }
            }
            default -> sender.sendMessage("commands.nick.illegal_argument", Key.of("pos", "1"), Key.of("argument", args[0]));
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender senderTemp, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        if (args.length == 1) {
            return Stream.of("set", "list", "info", "reset", "set_other", "fake_rang", "random", "reload_skin", "set_skin").filter(arg -> sender.hasPermission("api.command.nick."+arg)).toList();
//            List<String> completion = new ArrayList<>();
//            if (sender.hasPermission("api.command.nick.set")) completion.add("set");
//            if (sender.hasPermission("api.command.nick.list")) completion.add("list");
//            if (sender.hasPermission("api.command.nick.info")) completion.add("info");
//            if (sender.hasPermission("api.command.nick.reset")) completion.add("reset");
//            if (sender.hasPermission("api.command.nick.other_reset")) completion.add("reset");
//            if (sender.hasPermission("api.command.nick.set_other")) completion.add("set_other");
//            if (sender.hasPermission("api.command.nick.fake_rang")) completion.add("fake_rang");
//            if (sender.hasPermission("api.command.nick.random")) completion.add("random");
//            if (sender.hasPermission("api.command.nick.reload_skin")) completion.add("reload_skin");
//            if (sender.hasPermission("api.command.nick.set_skin")) completion.add("set_skin");
//            return completion.stream().filter(arg -> arg.startsWith(args[0])).toList();
        }
        if (!sender.hasPermission("api.command.nick." + args[0])) return new ArrayList<>();
        switch (args[0]) {
            case "set":
            case "list":
            case "reload_skin":
                break;
            case "info":
            case "set_other":
                if (args.length > 2) return new ArrayList<>();
                return sender.playersAreSeenName();
//                return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case "reset":
                if (args.length > 2) return new ArrayList<>();
                if (sender.hasPermission("api.command.nick.reset_other"))
                    return sender.playersAreSeenName();
//                    return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
            case "fake_rang":
                if (args.length == 3) return new ArrayList<>();
                return sender.playersAreSeenName();
//                return plugin.getRangManager().availableRanks().stream().filter(arg -> arg.startsWith(args[2])).toList();
        }
        return new ArrayList<>();
    }

    @Override
    public @Nullable String getPermission() {
        return "api.command.nick";
    }
}