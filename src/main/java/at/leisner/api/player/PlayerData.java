package at.leisner.api.player;

public class PlayerData {
    private String uuid;
    private String name;
    private boolean isVanish;
    private boolean isMute;
    private boolean isFrozen;

    public PlayerData(String uuid, String name, boolean isVanish, boolean isMute, boolean isFrozen) {
        this.uuid = uuid;
        this.name = name;
        this.isVanish = isVanish;
        this.isMute = isMute;
        this.isFrozen = isFrozen;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean isVanish() {
        return isVanish;
    }

    public boolean isMute() {
        return isMute;
    }

    public boolean isFrozen() {
        return isFrozen;
    }

    public void setVanish(boolean vanish) {
        isVanish = vanish;
    }

    public void setMute(boolean mute) {
        isMute = mute;
    }

    public void setFrozen(boolean frozen) {
        isFrozen = frozen;
    }
}
