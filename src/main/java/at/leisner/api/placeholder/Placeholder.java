package at.leisner.api.placeholder;

import org.bukkit.entity.Player;
import org.bukkit.World;

// Basisklasse für Platzhalter
public abstract class Placeholder {
    protected String key;

    public Placeholder(String key) {
        this.key = key;
    }

    public abstract String getValue(Player player, World world);
}

