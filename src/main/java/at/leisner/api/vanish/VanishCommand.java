package at.leisner.api.vanish;

import at.leisner.api.API;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class VanishCommand extends Command {
    private final API plugin;
    public VanishCommand(API plugin) {
        super("vanish", "", "", List.of("v"));
        this.plugin = plugin;
    }
    @Override
    public boolean execute(@NotNull CommandSender senderTemp, @NotNull String label, @NotNull String[] args) {
        switch (args[0]) {
            case "on" -> {
                plugin.getVanishManager().hidePlayer((Player) senderTemp);
            }
            case "off" -> {
                plugin.getVanishManager().showPlayer((Player) senderTemp);
            }
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return List.of("hide", "show", "on", "off", "list", "info").stream().filter(arg -> arg.startsWith(args[0])).toList();
        } else if (args.length == 2) {

        }
        return List.of();
    }
}
