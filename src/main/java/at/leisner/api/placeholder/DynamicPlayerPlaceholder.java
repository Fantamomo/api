package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

public abstract class DynamicPlayerPlaceholder extends DynamicPlaceholder {
    public DynamicPlayerPlaceholder(String key) {
        super(key);
    }

    public abstract String getValue(Player player);

    @Override
    public String getValue(Player player, World world) {
        return getValue(player);
    }
}
