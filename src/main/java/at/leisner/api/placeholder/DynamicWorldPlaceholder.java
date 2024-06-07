package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class DynamicWorldPlaceholder extends DynamicPlaceholder {
    public DynamicWorldPlaceholder(String key) {
        super(key);
    }

    public abstract String getValue(World world);

    @Override
    public String getValue(Player player, World world) {
        return getValue(world);
    }
}
