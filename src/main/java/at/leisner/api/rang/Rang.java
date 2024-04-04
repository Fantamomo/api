package at.leisner.api.rang;

public class Rang {
    private String name;
    private String id;
    private String prefix;
    private String suffix;
    private String chatFormat;
    private String joinMSG;
    private String quitMSG;
    private String playerColor;
    private String msgColor;
    private String displayName;
    private int prioritiat;

    public Rang(String id, String name, String prefix, String suffix, String chatFormat, String joinMSG, String quitMSG, String playerColor, String msgColor, int prioritiat, String displayName) {
        this.id = id;
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.chatFormat = chatFormat;
        this.joinMSG = joinMSG;
        this.quitMSG = quitMSG;
        this.playerColor = playerColor;
        this.msgColor = msgColor;
        this.prioritiat = prioritiat;
        this.displayName = displayName;
    }

    public String getName() {
        return name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getChatFormat() {
        return chatFormat;
    }

    public String getJoinMSG() {
        return joinMSG;
    }

    public String getQuitMSG() {
        return quitMSG;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public String getMsgColor() {
        return msgColor;
    }

    public String getId() {
        return id;
    }

    public int getPrioritiat() {
        return prioritiat;
    }
}
