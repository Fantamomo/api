package at.leisner.api.user;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.rang.Rang;
import at.leisner.api.rang.RangManager;
import at.leisner.api.util.NMSUtils;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachment;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.*;

public class User implements Serializable {
    private final static Set<User> users = new HashSet<>();
    private transient final Player player;
    private transient final ServerPlayer serverPlayer;
    private final String originalName;
    private String nickName;
    private Rang fakeRang;
    private int money;
    private Rang rang;
    private final UUID uuid;
    private final Set<Player> playersThatSeeNick = new HashSet<>();
    private boolean isVanish = false;
    private Language language;
    private Component status;
    public int nickSeePriority = 0;
    public int nickUsePriority = 0;
    public int vanishSeePriority = 0;
    public int vanishUsePriority = 0;
    public String mute = null;
    private Map<String, Boolean> permissions;
    private PermissionAttachment permissionAttachment;

    private User(Player player) {
        this.player = player;
        this.serverPlayer = ((CraftPlayer) player).getHandle();
        this.originalName = player.getName();
        this.uuid = player.getUniqueId();
        this.language = Language.defaultLang;
        this.rang = RangManager.defaultRang;
        this.permissions = API.getInstance().getPermissionManager().getPermission(uuid);
        this.permissionAttachment = API.getInstance().getPermissionManager().getPlayerPermissionAttachmentMap().get(player);
        this.fakeRang = RangManager.defaultRang;
    }

    @CanIgnoreReturnValue
    public static User of(Player player) {
        for (User user : users) {
            if (user.getPlayer() == player) {
                return user;
            }
        }
        User user = new User(player);
        users.add(user);
        return user;
    }

    @CanIgnoreReturnValue
    public static User of(String name) {
        for (User user : users) {
            if (user.getPlayer().getName().equals(name)) {
                return user;
            }
        }
        return null;
    }

    public Rang getFakeRang() {
        return fakeRang;
    }

    public Rang getCurrentRang() {
        if (nickName != null) return fakeRang;
        return rang;
    }

    public Player getPlayer() {
        return player;
    }

    public ServerPlayer getServerPlayer() {
        return serverPlayer;
    }

    public String getOriginalName() {
        return originalName;
    }

    public String getNickName() {
        return nickName;
    }

    public int getMoney() {
        return money;
    }

    public Rang getRang() {
        return rang;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getCurrentName() {
        if (nickName == null) return player.getName();
        return nickName;
    }

    public Set<Player> getPlayersThatSeeNick() {
        return playersThatSeeNick;
    }

    public boolean isVanish() {
        return isVanish;
    }

    public void setLanguage(String lang) {
        Language newLang = Language.getLanguage(lang);
        if (newLang == null) {
            return;
        }
        language = newLang;
    }

    public void sendTranslateMessage(String translateKey, Key... keys) {
        player.sendMessage(API.getInstance().miniMessage().deserialize(language.translate(translateKey, keys)));
    }

    public Language getLanguage() {
        return language;
    }

    public String getTranslateMessage(String translateKey, Key... keys) {
        return language.translate(translateKey, keys);
    }

    public void sendMessage(String msg) {
        player.sendMessage(API.getInstance().miniMessage().deserialize(msg));
    }
    public void sendMessage(Component component) {
        player.sendMessage(component);
    }

    public String getDisplayNameFor(Player forPlayer) {
        API plugin = API.getInstance();
        TextComponent.Builder displayName;
        Component prefix = getCurrentRang().prefix().isEmpty() ? Component.empty() : getCurrentRang().getPrefix();
        TextComponent name = NMSUtils.addHoverEvent(Component.text(getCurrentName()).color(getCurrentRang().playerNameColor()), this, User.of(forPlayer));
        if (!forPlayer.hasPermission("api.chat.hover.player-info")) {
            name = Component.text(getCurrentName()).color(getCurrentRang().playerNameColor());
        }
        Component suffix = getCurrentRang().suffix().isEmpty() ? Component.empty() : getCurrentRang().getSuffix();
        TextComponent muteComponent = Component.empty();
        TextComponent vanishComponent = Component.empty();
        TextComponent nickComponent = Component.empty();

        if (isVanish) {
            vanishComponent = Component.text("[V]").color(TextColor.color(0, 255, 255))
                    .hoverEvent(HoverEvent.showText(plugin.miniMessage().deserialize(language.translate("chat.hover.vanish"))));
        }

        if (nickName != null) {
            nickComponent = Component.text("[N]").color(TextColor.color(0, 255, 9))
                    .hoverEvent(HoverEvent.showText(plugin.miniMessage().deserialize(language.translate("chat.hover.nick", Key.of("original-name", getOriginalName())))));
        }

        if (mute != null) {
            muteComponent = Component.text("[M]").color(TextColor.color(255, 0, 0))
                    .hoverEvent(HoverEvent.showText(plugin.miniMessage().deserialize(language.translate("chat.hover.mute", Key.of("reason", mute)))));
        }

        if (forPlayer == null) {
            displayName = Component.text();
            if (!prefix.equals(Component.empty())) {
                displayName.append(prefix);
                displayName.append(Component.space());
            }
            displayName.append(name);
            if (!suffix.equals(Component.empty())) {
                displayName.append(suffix);
            }
            boolean first = true;
            if (!vanishComponent.equals(Component.empty())) {
                first = false;
                displayName.append(Component.space()).append(vanishComponent);
            }
            if (!nickComponent.equals(Component.empty())) {
                if (first) {
                    first = false;
                    displayName.append(Component.space());
                }
                displayName.append(nickComponent);
            }
            if (!muteComponent.equals(Component.empty())) {
                if (first) {
                    displayName.append(Component.space());
                }
                displayName.append(muteComponent);
            }
        } else {
            User user = User.of(forPlayer);
            displayName = Component.text();
            if (!prefix.equals(Component.empty())) {
                displayName.append(prefix);
                displayName.append(Component.space());
            }
            displayName.append(name);
            if (!suffix.equals(Component.empty())) {
                displayName.append(suffix);
            }
            boolean first = true;
            if (isVanish && user.vanishSeePriority > vanishUsePriority) {
                first = false;
                displayName.append(Component.space());
                displayName.append(vanishComponent);
            }
            if (nickName != null && user.nickSeePriority > nickUsePriority) {
                if (first) {
                    first = false;
                    displayName.append(Component.space());
                }
                displayName.append(nickComponent);
            }
            if (mute != null && user.getPlayer().isOp()) {
                if (first) {
                    displayName.append(Component.space());
                }
                displayName.append(muteComponent);
            }
        }

        return plugin.miniMessage().serialize(displayName.build()) + "<reset>";
    }


    public void setRang(Rang rang) {
        this.rang = rang;
    }

    public Map<String, Boolean> getPermissions() {
        return permissions;
    }

    public void setFakeRang(Rang fakeRang) {
        this.fakeRang = fakeRang;
    }

    public void setPermission(Map<String, Boolean> permission) {
        this.permissions = permission;
    }
    public void setPermission(String permission, boolean state) {
        permissions.put(permission, state);
        permissionAttachment.setPermission(permission, state);
    }
    public void unsetPermission(String permission) {
        permissions.remove(permission);
        permissionAttachment.unsetPermission(permission);
    }
}
