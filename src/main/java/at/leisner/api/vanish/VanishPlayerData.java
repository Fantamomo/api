package at.leisner.api.vanish;

import at.leisner.api.user.User;

public class VanishPlayerData {
    private final User user;
    boolean vanish;
    boolean canPickUpItems;

    public VanishPlayerData(User user, boolean vanish) {
        this.user = user;
        this.vanish = vanish;
    }

    public VanishPlayerData(User user, boolean vanish, boolean canPickUpItems) {
        this.user = user;
        this.vanish = vanish;
        this.canPickUpItems = canPickUpItems;
    }

    public VanishPlayerData(User user) {
        this.user = user;
    }

    public boolean isVanish() {
        return vanish;
    }

    public boolean canPickUpItems() {
        return canPickUpItems;
    }

    public int vanishSeePriority() {
        return user.vanishSeePriority;
    }
    public int vanishUsePriority() {
        return user.vanishUsePriority;
    }

}
