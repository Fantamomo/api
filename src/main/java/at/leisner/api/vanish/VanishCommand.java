package at.leisner.api.vanish;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.nick.NickManager;
import at.leisner.api.user.CommandSenderUser;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class VanishCommand extends Command {
    private final API plugin;
    private final VanishManager vanishManager;
    private final NickManager nickManager;
    private final List<String> arg0 = new ArrayList<>(List.of("hide", "show", "on", "off", "list", "info", "items"));
    private final String permission = "api.command.vanish";

    public VanishCommand(API plugin) {
        super("vanish", "", "/", List.of("v"));
        this.plugin = plugin;
        vanishManager = plugin.getVanishManager();
        nickManager = plugin.getNickManager();
    }

    @Override
    public boolean execute(@NotNull CommandSender senderTemp, @NotNull String label, @NotNull String[] args) {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        User user = sender.user();
        Player player = sender.player();
        if (!sender.controlPermission(permission, "commands.vanish.permission_error")) return true;
        if (args.length == 0) {
            if (sender.isPlayer()) {
                if (!sender.controlPermission("api.command.vanish.toggle", "commands.vanish.permission_error")) return true;
                if (vanishManager.toggleVanish(player)) {
                    sender.sendMessage("commands.vanish.on-vanish");
                } else {
                    sender.sendMessage("commands.vanish.off-vanish");
                }
            } else {
                sender.sendMessage("commands.vanish.need_to_be_player");
            }
            return true;
        }
        if (!arg0.contains(args[0]) || !sender.controlPermission("api.command.vanish."+args[0], "commands.vanish.permission_error")) return true;
        switch (args[0]) {
            case "on", "hide" -> {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (sender.canSee(target)) {
                        vanishManager.hidePlayer(target);
                        Language.sendTranslateMessage(target, "commands.vanish.on-vanish");
                        sender.sendMessage("commands.vanish.on-other", Key.of("display-name", User.of(target).getDisplayNameFor(sender.player())));
                    } else {
                        sender.sendMessage("commands.vanish.player_not_found");
                        return true;
                    }
                } else if (sender.isPlayer()) {
                    if (vanishManager.hidePlayer(player)) {
                        sender.sendMessage("commands.vanish.on-vanish");
                        return true;
                    }
                    sender.sendMessage("commands.vanish.already-vanish");
                } else {
                    sender.sendMessage("commands.vanish.need_to_be_player");
                }
            }
            case "off", "show" -> {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (sender.canSee(target)) {
                        vanishManager.showPlayer(target);
                        Language.sendTranslateMessage(target, "commands.vanish.off-vanish");
                        sender.sendMessage("commands.vanish.off-other", Key.of("display-name", User.of(target).getDisplayNameFor(sender.player())));
                    } else {
                        sender.sendMessage("commands.vanish.player_not_found");
                        return true;
                    }
                } else if (sender.isPlayer()) {
                    if (vanishManager.showPlayer(player)) {
                        sender.sendMessage("commands.vanish.off-vanish");
                        return true;
                    }
                    sender.sendMessage("commands.vanish.already-unvanish");
                } else {
                    sender.sendMessage("commands.vanish.need_to_be_player");
                }
            }
            case "items" -> {
                if (args.length == 2) {
                    Player target = Bukkit.getPlayerExact(args[1]);
                    if (sender.canSee(target)) {
                        if (vanishManager.toggleItemPickUp(target)) {
                            sender.sendMessage("commands.vanish.item-pick-up-true-other", Key.of("display-name", User.of(target).getDisplayNameFor(sender.player())));
                        } else {
                            sender.sendMessage("commands.vanish.item-pick-up-false-other", Key.of("display-name", User.of(target).getDisplayNameFor(sender.player())));
                        }
                    } else {
                        sender.sendMessage("commands.vanish.player_not_found");
                        return true;
                    }
                } else if (sender.isPlayer()) {
                    if (vanishManager.toggleItemPickUp(sender.player())) {
                        sender.sendMessage("commands.vanish.item-pick-up-true");
                    } else {
                        sender.sendMessage("commands.vanish.item-pick-up-false");
                    }
                } else {
                    sender.sendMessage("commands.vanish.need_to_be_player");
                }
            }
            case "list" -> {
                String msg = sender.translate("commands.vanish.list");
                String format = sender.translate("commands.vanish.list-format");
                List<String> listet = sender.playersAreSeen()
                        .stream()
                        .map(User::of)
                        .filter(u -> u.vanishPlayerData.isVanish())
                        .map(u -> sender.translate("commands.vanish.list-format", false,
                                Key.of("display-name", u.getDisplayNameFor(player)),
                                Key.of("see", String.valueOf(u.vanishSeePriority)),
                                Key.of("use", String.valueOf(u.vanishUsePriority))
                        )).toList();
                sender.sendMessage(plugin.miniMessage().deserialize(msg + String.join(", ", listet)));
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return arg0.stream().filter(arg -> sender.hasPermission("api.command.vanish." + arg)).filter(arg -> arg.startsWith(args[0])).toList();
        } else if (args.length == 2) {
            switch (args[0]) {
                case "hide", "show", "on", "off" -> {
                    return vanishManager.getSeenPlayerNamesFor(sender instanceof Player ? (Player) sender : null);
                }
            }
        }
        return List.of();
    }

    @Override
    public @Nullable String getPermission() {
        return permission;
    }
}
