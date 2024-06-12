package at.leisner.api.user;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class CommandSenderUser {
    private final CommandSender sender;
    private final User user;
    private final Player player;

    public CommandSenderUser(CommandSender sender) {
        this.sender = sender;
        if (sender instanceof Player player) {
            this.user = User.of(player);
            this.player = player;
        } else {
            user = null;
            player = null;
        }

    }

    public void sendMessage(String translateKey, Key... keys) {
        if (user != null) {
            user.sendTranslateMessage(translateKey, keys);
        } else {
            sender.sendMessage(API.getInstance().miniMessage().deserialize(Language.defaultLang.translate(translateKey, keys)));
        }
    }
    public String translate(String translateKey, Key... keys) {
        return translate(translateKey, true, keys);
    }
    public String translate(String translateKey, boolean withPrefix, Key... keys) {
        if (user != null) {
            return user.getLanguage().translateWithPrefixIfAvailable(translateKey, withPrefix, keys);
        }
        return Language.defaultLang.translateWithPrefixIfAvailable(translateKey, withPrefix, keys);
    }
    public void sendMessage(Component component) {
        sender.sendMessage(component);
    }

    public Player player() {
        return player;
    }
    public User user() {
        return user;
    }
    public String getNameFor(Player forPlayer) {
        if (player != null) return user.getDisplayNameFor(forPlayer);
        return "SERVER";
    }
    public boolean hasPermission(String permission) {
        if (player == null) return true;
        return player.hasPermission(permission);
    }
    public boolean controlPermission(String permission, String translateKey) {
        if (hasPermission(permission)) return true;
        sendMessage(translateKey, Key.of("perm", permission));
        return false;
    }
    public boolean isPlayer() {
        return sender instanceof Player;
    }

    public List<Player> playersAreSeen() {
        return (List<Player>) Bukkit.getOnlinePlayers().stream().filter(player -> !(sender instanceof Player p) || p.canSee(player)).toList();
    }
    public List<String> playersAreSeenName() {
        return playersAreSeen().stream().map(Player::getName).toList();
    }
    public boolean canSee(Player p) {
        if (!isPlayer()) return true;
        return playersAreSeen().contains(p);
    }
}
