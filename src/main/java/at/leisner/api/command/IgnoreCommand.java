package at.leisner.api.command;

import at.leisner.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class IgnoreCommand extends Command {
    private final API plugin;
    public IgnoreCommand(API plugin) {
        super("ignore");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        return false;
    }
}
