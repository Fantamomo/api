package at.leisner.api.cmd;

import at.leisner.api.Api;
import at.leisner.api.player.FreezeManager;
import at.leisner.api.rang.RangManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class FreezeCMD extends Command {
    public FreezeCMD() {
        super("freeze");
        this.setPermission("cmd.freeze");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] args) {
        if (args.length != 1) {
            commandSender.sendMessage(ChatColor.GOLD+"["+ChatColor.AQUA+"FREEZE"+ChatColor.GOLD+"]"+ChatColor.RED+" Use \"/freeze <player>\"");
            return false;
        }
        Player player = Bukkit.getPlayerExact(args[0]);
        if (player == null) {
            commandSender.sendMessage(ChatColor.GOLD+"["+ChatColor.AQUA+"FREEZE"+ChatColor.GOLD+"]"+ChatColor.RED+" Ungültiger Spieler");
            return false;
        }
        FreezeManager freezeManager = Api.getInstance().getFreezeManager();
        if (freezeManager.toggleState(player)) {
            commandSender.sendMessage(ChatColor.GOLD+"["+ChatColor.AQUA+"FREEZE"+ChatColor.GOLD+"]"+ChatColor.RED+" "+ RangManager.getDisplayName(player)+ChatColor.RED+" is nun gefreezed!");
            return true;
        } else {
            commandSender.sendMessage(ChatColor.GOLD+"["+ChatColor.AQUA+"FREEZE"+ChatColor.GOLD+"]"+ChatColor.GREEN+" "+ RangManager.getDisplayName(player)+ChatColor.GREEN+" is nun endfreezed!");
            return true;
        }
    }
}
