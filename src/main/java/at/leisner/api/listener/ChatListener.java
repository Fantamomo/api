package at.leisner.api.listener;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.rang.Rang;
import at.leisner.api.user.User;
import io.papermc.paper.event.player.AsyncChatEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ChatListener implements Listener {
    private final API plugin;

    public ChatListener(API plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAsyncChat(AsyncChatEvent event) {
        User user = User.of(event.getPlayer());
        Rang rang = user.getCurrentRang();
        Component msg = event.message();
        String message = rang.chatMsg().replace("{msg}", plugin.miniMessage().serialize(msg));
        event.setCancelled(true);

        if (user.mute != null) {
            TextComponent component = Component.text(user.getTranslateMessage("chat.mute-error"))
                    .hoverEvent(HoverEvent.showText(plugin.miniMessage().deserialize(
                            user.getTranslateMessage("chat.hover.mute", Key.of("reason", user.mute)))));
            user.sendMessage(component);
            return;
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(plugin.miniMessage().deserialize(message.replace("{display-name}", user.getDisplayNameFor(player)))
                    .hoverEvent(HoverEvent.showText(plugin.miniMessage().deserialize(user.getTranslateMessage("chat.hover.chat-user-manage"))))
                    .clickEvent(ClickEvent.runCommand("player-info player "+user.getOriginalName()+" chat settings")));
        }
    }
}
