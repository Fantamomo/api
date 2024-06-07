package at.leisner.api.command;

import at.leisner.api.util.NMSUtils;
import net.minecraft.network.chat.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FakeTeamCommand extends Command {
    public FakeTeamCommand() {
        super("faketeam");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        Player player1 = Bukkit.getPlayerExact(args[0]);
        Player player2 = Bukkit.getPlayerExact(args[1]);
        NMSUtils.setFakePlayerTeam(player1, player2, args.length > 2 ? Component.literal(args[2]) : null, args.length > 3 ? Component.literal(args[3]) : null);
        return false;
    }
}
