package at.leisner.api.rang;

import at.leisner.api.API;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;

public class Rang {
    private final String id;
    private String name;
    private int priority;
    private Component prefix;
    private Component suffix;
    private Component joinMsg;
    private Component quitMsg;
    private Component chatMsg;
    private TextColor playerNameColor;
    private final API plugin;

    public Rang(String id, String name, int priority, Component prefix, Component suffix, Component joinMsg, Component quitMsg, Component chatMsg, TextColor playerNameColor) {
        this.id = id;
        this.name = name;
        this.priority = priority;
        this.prefix = prefix;
        this.suffix = suffix;
        this.joinMsg = joinMsg;
        this.quitMsg = quitMsg;
        this.chatMsg = chatMsg;
        this.playerNameColor = playerNameColor;
        plugin = API.getInstance();
    }

    public String name() {
        return name;
    }

    public int priority() {
        return priority;
    }

    public String prefix() {
        return plugin.miniMessage().serialize(prefix);
    }

    public String suffix() {
        return plugin.miniMessage().serialize(suffix);
    }

    public String joinMsg() {
        return plugin.miniMessage().serialize(joinMsg);
    }

    public String quitMsg() {
        return plugin.miniMessage().serialize(quitMsg);
    }

    public String chatMsg() {
        return plugin.miniMessage().serialize(chatMsg);
    }

    public TextColor playerNameColor() {
        return playerNameColor;
    }

    public String getName() {
        return name;
    }

    public int getPriority() {
        return priority;
    }

    public Component getPrefix() {
        return prefix;
    }

    public Component getSuffix() {
        return suffix;
    }

    public Component getJoinMsg() {
        return joinMsg;
    }

    public Component getQuitMsg() {
        return quitMsg;
    }

    public Component getChatMsg() {
        return chatMsg;
    }

    public TextColor getPlayerNameColor() {
        return playerNameColor;
    }
    public String id() {
        return id;
    }
}
