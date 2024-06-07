package at.leisner.api.permission;

import at.leisner.api.API;
import at.leisner.api.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachment;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public class PermissionManager {
    private final API plugin;
    private final File permissionFile;
    private Map<UUID, Map<String, Boolean>> permissions = new HashMap<>();
    private Map<Player, PermissionAttachment> playerPermissionAttachmentMap = new HashMap<>();
    private static List<Permission> APIPermissions = Stream.of("api.command.nick.set",
            "api.command.nick.reset",
            "api.command.nick.set_other",
            "api.command.nick.list",
            "api.command.nick.other_reset",
            "api.command.nick.info",
            "api.command.nick.random",
            "api.command.nick.fake_rang"
    ).map(Permission::new).toList();

    public PermissionManager(API plugin, File permissionFile) {
        this.plugin = plugin;
        this.permissionFile = permissionFile;
        register();
    }

    private void register() {
        for (Permission permission : APIPermissions) {
            Bukkit.getPluginManager().addPermission(permission);
        }

    }

    public void loadPermissions() {
        if (!permissionFile.exists()) {
            permissions = new HashMap<>();
            savePermissions();
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(permissionFile))) {
            permissions = (Map<UUID, Map<String, Boolean>>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            plugin.getLogger().warning("Failed to load permissions: " + e.getMessage());
        }
    }

    public void savePermissions() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(permissionFile))) {
            oos.writeObject(permissions);
        } catch (IOException e) {
            plugin.getLogger().warning("Failed to save permissions: " + e.getMessage());
        }
    }

    public Map<String, Boolean> getPermission(UUID key) {
        permissions.computeIfAbsent(key, k -> new HashMap<>());
        return permissions.get(key);
    }
    public void setPermission(Player player, String permission, boolean state) {
        User user = User.of(player);
        user.setPermission(permission, state);
    }

    public void unsetPermission(Player player, String permission) {
        User user = User.of(player);
        user.unsetPermission(permission);
    }

    public Map<Player, PermissionAttachment> getPlayerPermissionAttachmentMap() {
        return playerPermissionAttachmentMap;
    }
}
