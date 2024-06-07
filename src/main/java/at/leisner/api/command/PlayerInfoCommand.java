package at.leisner.api.command;

import at.leisner.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public class PlayerInfoCommand extends Command {
    private final API plugin;
    public PlayerInfoCommand(API plugin) {
        super("player-info");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        commandSender.sendMessage(Arrays.toString(args));
        return false;
    }
}
