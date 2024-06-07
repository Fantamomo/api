package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

// Spezialisierte Platzhalter für Spieler
public abstract class BooleanPlayerPlaceholder extends BooleanPlaceholder {
    public BooleanPlayerPlaceholder(String key) {
        super(key);
    }

    public abstract boolean getBooleanValue(Player player);

    @Override
    public boolean getBooleanValue(Player player, World world) {
        return getBooleanValue(player);
    }
}
