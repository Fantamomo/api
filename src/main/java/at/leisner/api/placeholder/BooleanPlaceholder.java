package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

// Boolescher Platzhalter
public abstract class BooleanPlaceholder extends Placeholder {
    public BooleanPlaceholder(String key) {
        super(key);
    }

    public abstract boolean getBooleanValue(Player player, World world);

    @Override
    public String getValue(Player player, World world) {
        return String.valueOf(getBooleanValue(player, world));
    }
}
