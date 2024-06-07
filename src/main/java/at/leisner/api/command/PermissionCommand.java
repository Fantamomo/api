package at.leisner.api.command;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.user.CommandSenderUser;
import at.leisner.api.user.User;
import net.minecraft.world.level.GameRules;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PermissionCommand extends Command {
    private final API plugin;
    public PermissionCommand(API plugin) {
        super("perm");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender senderTemp, @NotNull String alias, @NotNull String[] args) {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        Player player = null;
        User user = null;
        if (args.length > 0) {
            player = plugin.getNickManager().getPlayerByInGameName(args[0]);
            user = User.of(player);
        }
        switch (args.length) {
            case 0, 1 -> {
                sender.sendMessage("commands.perm.usage", Key.of("usage", "/perm <player> <set|info> <...>"));
                return true;
            }
            case 2 -> {
                if (args[1].equals("info")) {
                }
            }
            case 3 -> {
                if (args[1].equals("info")) {
                    if (player.isPermissionSet(args[2])) {
                        sender.sendMessage("commands.perm.info-single", Key.of("state", String.valueOf(player.hasPermission(args[2]))), Key.of("player-name", user.getDisplayNameFor(sender.player())));
                    } else {
                        sender.sendMessage("commands.perm.info-single-unset", Key.of("player-name", user.getDisplayNameFor(sender.player())));
                    }
                } else if (args[1].equals("unset")) {
                    String permission = args[2];
                    user.unsetPermission(permission);
                    sender.sendMessage("commands.perm.set",
                            Key.of("player-name", user.getDisplayNameFor(sender.player())),
                            Key.of("perm", permission),
                            Key.of("state", "unset")
                    );
                }
            }
            case 4 -> {
                if (args[1].equals("set")) {
                    String permission = args[2];
                    user.setPermission(permission, Boolean.parseBoolean(args[3]));
                    sender.sendMessage("commands.perm.set",
                            Key.of("player-name", user.getDisplayNameFor(sender.player())),
                            Key.of("perm", permission),
                            Key.of("state", String.valueOf(Boolean.parseBoolean(args[3])))
                    );
                }
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        } else if (args.length == 2) {
            List<String> arg1 = new ArrayList<>();
            if (sender.hasPermission("api.command.perm.set")) arg1.add("set");
            if (sender.hasPermission("api.command.perm.info")) arg1.add("info");
            if (sender.hasPermission("api.command.perm.unset")) arg1.add("unset");
            return arg1;
        } else if (args.length == 3) {
            String input = args[2];
            int dotIndex = input.lastIndexOf('.');
            String prefix = dotIndex == -1 ? input : input.substring(0, dotIndex + 1);

            return Bukkit.getPluginManager().getPermissions().stream()
                    .map(Permission::getName)
                    .filter(permission -> {
                        if (!permission.startsWith(prefix)) {
                            return false;
                        }
                        String suffix = permission.substring(prefix.length());
                        return !suffix.contains(".");
                    }).toList();
        } else if (args.length == 4) {
            if (args[1].equals("set")) {
                return List.of("true", "false");
            }
        }
        return List.of();
    }

    @Override
    public @Nullable String getPermission() {
        return "api.command.perm";
    }
}
