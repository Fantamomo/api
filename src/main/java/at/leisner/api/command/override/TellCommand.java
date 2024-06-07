package at.leisner.api.command.override;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.user.CommandSenderUser;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TellCommand extends Command {
    private final API plugin;
    public TellCommand(API plugin) {
        super("tell","Send a private message to a player", "/tell <player> <msg...>", List.of("msg", "w"));
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender senderTemp, @NotNull String alias, @NotNull String[] args) {
        String from;
        CommandSenderUser sender = new CommandSenderUser(senderTemp);
        if (args.length < 2) {
            sender.sendMessage("command.usage", Key.of("usage", "/tell <player> <msg...>"));
            return true;
        }

        if (senderTemp instanceof Player player) {
            from = player.getName();
        } else {
            from = "SERVER";
        }

        List<String> temp = new ArrayList<>(List.of(args));
        temp.removeFirst();
        String msg = String.join(" ", temp);

        Player to = plugin.getNickManager().getPlayerExact(args[0]);
        if (to == null) {
            sender.sendMessage("command.player_not_found");
            return true;
        }
        User userTo = User.of(to);
        if (userTo.getNickName() == null) {
            sender.sendMessage("commands.tell.msg-send", Key.of("reviver", userTo.getDisplayNameFor(sender.player())), Key.of("msg", msg));
            userTo.sendTranslateMessage("commands.tell.msg-revive", Key.of("sender", from), Key.of("msg", msg));
        } else if (userTo.getNickName().equals(args[0])) {
            sender.sendMessage("commands.tell.msg-send", Key.of("reviver", to.getName()), Key.of("msg", msg));
            userTo.sendTranslateMessage("commands.tell.msg-revive", Key.of("sender", sender.player() == null ? "SERVER" : User.of(sender.player()).getDisplayNameFor(to)), Key.of("msg", msg));
        } else {
            sender.sendMessage("command.player_not_found");
        }
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Bukkit.getOnlinePlayers().stream().map(Player::getName).toList();
        }
        return List.of();
    }
}
