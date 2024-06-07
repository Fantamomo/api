package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

// Spezialisierte Platzhalter für Welten
public abstract class BooleanWorldPlaceholder extends BooleanPlaceholder {
    public BooleanWorldPlaceholder(String key) {
        super(key);
    }

    public abstract boolean getBooleanValue(World world);

    @Override
    public boolean getBooleanValue(Player player, World world) {
        return getBooleanValue(world);
    }
}
