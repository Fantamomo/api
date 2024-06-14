package at.leisner.api.vanish;

import at.leisner.api.API;
import at.leisner.api.user.User;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VanishManager {
    private final API plugin;
    private final Set<Player> vanishPlayers = new HashSet<>();

    public VanishManager(API plugin) {
        this.plugin = plugin;
    }

    @CanIgnoreReturnValue
    public boolean hidePlayer(Player player) {
        User user = User.of(player);
        if (user.vanishPlayerData.vanish) return false;
        user.vanishPlayerData.vanish = true;
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (user.vanishUsePriority > User.of(p).vanishSeePriority) {
                p.hidePlayer(plugin, player);
            }
        }
        vanishPlayers.add(player);
        return true;
    }

    @CanIgnoreReturnValue
    public boolean toggleVanish(Player player) {
        User user = User.of(player);
        if (user.vanishPlayerData.vanish) {
            showPlayer(player);
        } else {
            hidePlayer(player);
        }
        return user.vanishPlayerData.vanish;
    }
    @CanIgnoreReturnValue
    public boolean showPlayer(Player player) {
        User user = User.of(player);
        if (!user.vanishPlayerData.vanish) return false;
        user.vanishPlayerData.vanish = false;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.showPlayer(plugin, player);
        }
        vanishPlayers.remove(player);
        return true;
    }
    public boolean canSee(Player viewed, Player viewer) {
        return viewer.canSee(viewed);
    }
    public boolean couldSee(Player viewed, Player viewer) {
        return User.of(viewed).vanishUsePriority > User.of(viewer).vanishSeePriority;
    }
    public boolean isVanish(Player player) {
        return vanishPlayers.contains(player);
    }
    public List<Player> getSeenPlayersFor(Player player) {
        return Bukkit.getOnlinePlayers().stream().filter(p -> player == null || p.canSee(player)).map(p -> (Player) p).toList();
    }
    public List<String> getSeenPlayerNamesFor(Player player) {
        return getSeenPlayersFor(player).stream().map(Player::getName).toList();
    }
    public boolean toggleItemPickUp(Player player) {
        User user = User.of(player);
        return user.getVanishPlayerData().canPickUpItems = !user.getVanishPlayerData().canPickUpItems;
    }

    public Set<Player> vanishPlayers() {
        return vanishPlayers;
    }
}
