package at.leisner.api.command;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.user.CommandSenderUser;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UserCommand extends Command {
    private final API plugin;

    public UserCommand(API plugin) {
        super("user");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender senderTemp, @NotNull String alias, @NotNull String[] args) {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        switch (args.length) {
            case 0, 1 -> {
                sender.sendMessage("commands.user.usage", Key.of("usage", "/user <set|info> <rang|priority|language> <...>"));
            }
            case 2 -> {
            }
            case 3 -> {
                Player player = plugin.getNickManager().getPlayerByInGameName(args[0]);
                User user = User.of(player);
                if (args[1].equals("set")) {
                    if (args[2].equals("mute")) {
                        user.mute = null;
                        sender.sendMessage("commands.user.mute.reset", Key.of("player-name", player.getName()));
                        return true;
                    }
                }
            }
            case 4 -> {
                Player player = plugin.getNickManager().getPlayerByInGameName(args[0]);
                User user = User.of(player);
                if (args[1].equals("set")) {
                    if (args[2].equals("rang")) {
                        if (plugin.getRangManager().availableRanks().contains(args[3])) {
                            user.setRang(plugin.getRangManager().getRang(args[3]));
                        }
                    }
                }
            }
            case 6 -> {
                Player player = plugin.getNickManager().getPlayerByInGameName(args[0]);
                User user = User.of(player);
                if (args[1].equals("set")) {
                    if (args[2].equals("priority")) {
                        try {
                            int priority = Integer.parseInt(args[5]);
                            if (args[3].equals("see")) {
                                if (args[4].equals("nick")) {
                                    user.nickSeePriority = priority;
                                } else if (args[4].equals("vanish")) {
                                    user.vanishSeePriority = priority;
                                } else {
                                    return true;
                                }
                            } else if (args[3].equals("use")) {
                                if (args[4].equals("nick")) {
                                    user.nickUsePriority = priority;
                                } else if (args[4].equals("vanish")) {
                                    user.vanishUsePriority = priority;
                                } else {
                                    return true;
                                }
                            } else {
                                return true;
                            }
                            sender.sendMessage("commands.user.priority-set", Key.of("var", args[4] + "-" + args[3]), Key.of("value", String.valueOf(priority)));
                        } catch (NumberFormatException nfe) {
                            user.sendTranslateMessage("commands.user.not_a_number", Key.of("arg", "fourth"));
                        }
                    }
                }
            }
        }
        if (args.length > 3) {
            if (args[2].equals("mute")) {
                Player player = plugin.getNickManager().getPlayerByInGameName(args[0]);
                User user = User.of(player);
                List<String> reason = new ArrayList<>(List.of(args));
                reason.removeFirst();
                reason.removeFirst();
                reason.removeFirst();
                user.mute = String.join(" ", reason);
                sender.sendMessage("commands.user.mute.reset", Key.of("player-name", player.getName()), Key.of("reason", user.mute));
                return true;
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        switch (args.length) {
            case 1 -> {
                return Bukkit.getOnlinePlayers().stream().filter(player -> !(sender instanceof Player p) || p.canSee(player)).map(Player::getName).toList();
            }
            case 2 -> {
                return List.of("set", "info");
            }
            case 3 -> {
                return List.of("rang", "priority", "language", "mute");
            }
            case 4 -> {
                if (args[1].equals("set")) {
                    switch (args[2]) {
                        case "rang" -> {
                            return plugin.getRangManager().availableRanks().stream().toList();
                        }
                        case "language" -> {
                            return Language.availableLanguages().stream().toList();
                        }
                    }
                }
                if (args[2].equals("priority")) {
                    return List.of("see", "use");
                }
            }
            case 5 -> {
                if (args[2].equals("priority")) {
                    return List.of("nick", "vanish");
                }
            }
        }
        return List.of();
    }
}
