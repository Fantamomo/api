package at.leisner.api.placeholder;

import org.bukkit.World;
import org.bukkit.entity.Player;

public class StaticPlaceholder extends Placeholder {
    private final String value;

    public StaticPlaceholder(String key, String value) {
        super(key);
        this.value = value;
    }

    @Override
    public String getValue(Player player, World world) {
        return value;
    }
}
