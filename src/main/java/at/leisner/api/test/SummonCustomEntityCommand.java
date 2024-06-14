package at.leisner.api.test;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SummonCustomEntityCommand extends Command {
    public SummonCustomEntityCommand() {
        super("summon-custom-entity");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) commandSender;
        switch (args[0]) {
            case "arrow" -> {
                HomingArrow homingArrow = new HomingArrow(new Location(player.getWorld(),Double.parseDouble(args[1]),Double.parseDouble(args[2]),Double.parseDouble(args[3])));
                homingArrow.setTarget(((CraftPlayer) Bukkit.getPlayerExact(args[4])).getHandle());
            }
            case "snowman" -> {

            }
        }
        return true;
    }
}
