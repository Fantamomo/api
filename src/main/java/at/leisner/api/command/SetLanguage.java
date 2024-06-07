package at.leisner.api.command;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.user.User;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SetLanguage extends Command {
    private final API plugin;
    public SetLanguage(API plugin) {
        super("language");
        this.plugin = plugin;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(plugin.getLanguage().translate("command.need_to_be_player"));
            return true;
        }
        User user = User.of(player);
        if (args.length != 1) {
            user.sendTranslateMessage("commands.set-language.info", Key.of("lang", user.getLanguage().name));
            return true;
        }
        if (!Language.availableLanguages().contains(args[0])) {
            user.sendTranslateMessage("commands.set-language.not-exist");
            return true;
        }
        user.setLanguage(args[0]);
        user.sendTranslateMessage("commands.set-language.success", Key.of("lang",args[0]));
        return true;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return Language.availableLanguages().stream().toList();
        }
        return new ArrayList<>();
    }
}
