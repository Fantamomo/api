package at.leisner.api.vanish;

import at.leisner.api.API;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class VanishManager {
    private final API plugin;
    private final Set<Player> vanishPlayers = new HashSet<>();

    public VanishManager(API plugin) {
        this.plugin = plugin;
    }

    public void hidePlayer(Player player) {
        User user = User.of(player);
        user.isVanish = true;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (user.vanishUsePriority > User.of(p).vanishSeePriority) {
                p.hidePlayer(plugin, player);
            }
        }
        vanishPlayers.add(player);
    }
    public void showPlayer(Player player) {
        User user = User.of(player);
        user.isVanish = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
        vanishPlayers.remove(player);
    }
    public boolean canSee(Player viewed, Player viewer) {
        return viewer.canSee(viewed);
    }
    public boolean couldSee(Player viewed, Player viewer) {
        return User.of(viewed).vanishUsePriority > User.of(viewer).vanishSeePriority;
    }
}
