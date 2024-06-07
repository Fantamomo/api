package at.leisner.api.command;

import at.leisner.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RangCommand extends Command {
    private final API plugin;
    public RangCommand(API plugin) {
        super("rang");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 0) {

            return true;
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("set", "info");
        } else if (args.length == 2) {
            return plugin.getRangManager().availableRangs().stream().toList();
        } else if (args.length == 3) {
            if (args[0].equals("set")) {
                return List.of("priority", "prefix", "suffix", "chat-msg", "join-msg", "quit-msg", "name", "player-name-color");
            }
        }
        return List.of();
    }
}
