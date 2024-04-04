package at.leisner.api.cmd;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TestCMD extends Command {
    public TestCMD() {
        super("test");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length != 2) return false;
        String key = args[0];
        Player p = Bukkit.getPlayerExact(args[1]);
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("must be Player");
            return false;
        }
        Player sender = (Player) commandSender;
        if (p == null) {
            commandSender.sendMessage("Player null");
            return false;
        }
        if (key.equals("add")) {
            sender.listPlayer(p);
            return true;
        }
        if (key.equals("remove")) {
            if (sender.isListed(p)) {
                sender.unlistPlayer(p);
            }
        }
        return false;
    }
}
