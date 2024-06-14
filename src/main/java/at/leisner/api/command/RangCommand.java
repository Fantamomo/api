package at.leisner.api.command;

import at.leisner.api.API;
import at.leisner.api.user.CommandSenderUser;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Stream;

public class RangCommand extends Command {
    private final API plugin;
    public RangCommand(API plugin) {
        super("rang");
        this.plugin = plugin;
        setPermission("api.command.rang");
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {

            return true;
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender senderTemp, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        if (args.length == 1) {
            return List.of("set", "info");
        } else if (args.length == 2) {
            return plugin.getRangManager().availableRanks().stream().toList();
        } else if (args.length == 3) {
            if (args[0].equals("set")) {
                return Stream.of("priority", "prefix", "suffix", "chat-msg", "join-msg", "quit-msg", "name", "player-name-color").filter(arg -> sender.hasPermission("api.command.rang.set."+arg)).toList();
            }
        }
        return List.of();
    }
}
