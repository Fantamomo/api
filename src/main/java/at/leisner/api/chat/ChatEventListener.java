package at.leisner.api.chat;

import at.leisner.api.Api;
import at.leisner.api.util.Formater;
import at.leisner.api.vanish.VanishManager;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static at.leisner.api.player.FreezeManager.frozenPlayers;

public class ChatEventListener implements Listener {
    private ChatManager chatManager;
    private final Api plugin;
    private final VanishManager vanishManager;


    public ChatEventListener() {
        this.plugin = Api.getInstance();
        this.vanishManager = plugin.getVanishManager();
    };

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player sender = event.getPlayer();
        String message = event.getMessage();
//        message = Format.formatPlayer(message,sender);
        event.setCancelled(true);
        if (frozenPlayers.contains(sender.getUniqueId())) {
            if (message.startsWith("!pass freeze ")) {
                String pass = message.replace("!pass freeze ","");
                if (pass.equals(plugin.getConfig().getString("freeze.pass"))) {
                    sender.sendMessage(ChatColor.GREEN + "Richtiges Passwort. Du bist frei!");
                    frozenPlayers.remove(sender.getUniqueId());
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (player.hasPermission("cmd.freeze.notify")) {
                            player.sendMessage(ChatColor.BLUE+ "[FREEZ] "+ ChatColor.GOLD + Api.getInstance().getRangManager().getDisplayName(sender) + ChatColor.GREEN +  " ist frei!");
                        }
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "Falsches Passwort!");
                    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                        if (player.hasPermission("cmd.freez.notify")) {
                            player.sendMessage(ChatColor.BLUE+ "[FREEZ] "+ ChatColor.GOLD + Api.getInstance().getRangManager().getDisplayName(sender) + ChatColor.RED +  " hat das falsche Passwort eingegeben!");
                        }
                    }
                }
                return;

            }
        }
        if (vanishManager.getVanishPlayers().contains(sender)) {
            if (message.startsWith("#")) {
                message = message.replaceFirst("#","");
                if (message.isEmpty()) return;
            } else if (sender.hasPermission("cmd.vanish.chat")) {
                sender.sendMessage(Api.getInstance().getVanishManager().vanishPrefix+" "+ChatColor.RED+"Du bist im vanish modus! Um eine Nachricht zu sender musst du ein '#' vor deine Nachricht setzen!");
                return;
            }
        }

        if (sender.hasPermission("chat.mute")) {
            sender.sendMessage(ChatColor.RED + "You are muted! You cannot send a chat message!");
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                if (player.hasPermission("chat.mute.notify")) {
                    String formattedMessage = ChatColor.RED+"[MUTE]"+ Formater.formatMessage(sender, message);
                    TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(formattedMessage));
                    if (player.hasPermission("chat.info")) {
                        String hoverText = chatManager.getPlayerInfoForHover(sender);
                        messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
//                        if (player.hasPermission("chat.click")) {
//                            ClickEvent clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND,"say test");
//                            messageComponent.setClickEvent(clickEvent);
//                        }
                    }

                    player.spigot().sendMessage(messageComponent);
                }
            }
            return;
        }
        String consolFormattedMessage = Formater.formatMessage(sender, message);
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        // Nachricht an die Konsole mit Farbe senden
        console.sendMessage(ChatColor.GREEN + "[CHAT] " + consolFormattedMessage);

        for (Player recipient : sender.getServer().getOnlinePlayers()) {
            String formattedMessage = Formater.formatMessage(sender, message);
            TextComponent messageComponent = new TextComponent(TextComponent.fromLegacyText(formattedMessage));

            if (recipient.hasPermission("chat.info")) {
                String hoverText = chatManager.getPlayerInfoForHover(sender);
                messageComponent.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(hoverText).create()));
            }

            recipient.spigot().sendMessage(messageComponent);
        }
    }
}
