package at.leisner.api.user;

import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.lang.Language;
import at.leisner.api.rang.Rang;
import at.leisner.api.rang.RangManager;
import at.leisner.api.util.NMSUtils;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.*;

public class User implements Serializable {
    private static API plugin;
    private final static Set<User> users = new HashSet<>();
    private transient Player player;
    private transient ServerPlayer serverPlayer;
    private final String originalName;
    private String nickName;
    private Rang fakeRang;
    private int money;
    private Rang rang;
    private final UUID uuid;
    private transient final Set<Player> playersThatSeeNick = new HashSet<>();
    public boolean isVanish = false;
    private Language language;
    private Component status;
    public int nickSeePriority = 0;
    public int nickUsePriority = 0;
    public int vanishSeePriority = 0;
    public int vanishUsePriority = 0;
    public String mute = null;
    private Map<String, Boolean> permissions;
    private GameProfile gameProfile;
//    private PermissionAttachment permissionAttachment;

    private User(Player player) {
        if (plugin == null) plugin = API.getInstance();
        this.player = player;
        this.serverPlayer = ((CraftPlayer) player).getHandle();
        this.originalName = player.getName();
        this.uuid = player.getUniqueId();
        this.language = Language.defaultLang;
        this.rang = RangManager.defaultRang;
        this.gameProfile = serverPlayer.gameProfile;
//        this.permissions = API.getInstance().getPermissionManager().getPermission(uuid);
//        this.permissionAttachment = API.getInstance().getPermissionManager().getPlayerPermissionAttachmentMap().get(player);
        this.fakeRang = RangManager.defaultRang;
    }

    private User(String name, UUID uuid) {
        if (plugin == null) plugin = API.getInstance();
        this.originalName = name;
        this.uuid = uuid;
        this.fakeRang = RangManager.defaultRang;
        this.language = Language.defaultLang;
        this.rang = RangManager.defaultRang;
    }

    @CanIgnoreReturnValue
    public static User of(Player player) {
        for (User user : users) {
            if (user.getPlayer() == player) {
                return user;
            }
        }
        for (User user : users) {
            if (user.getOriginalName().equals(player.getName())) {
                user.player = player;
                user.serverPlayer = ((CraftPlayer) player).getHandle();
                user.gameProfile = user.serverPlayer.gameProfile;
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

    private static @NotNull User of(String name, UUID uuid) {
        User user = null;
        for (User u : users) {
            if (u.getOriginalName().equals(name)) {
                user = u;
                break;
            }
        }
        if (user != null) return user;
        user = new User(name, uuid);
        users.add(user);
        return user;
    }

    public static User fromJson(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();

        String originalName = null;
        UUID uuid = null;

        if (jsonObject.has("originalName")) {
            originalName = jsonObject.get("originalName").getAsString();
        }
        if (jsonObject.has("uuid")) {
            uuid = UUID.fromString(jsonObject.get("uuid").getAsString());
        }

        if (originalName == null || uuid == null) {
            return null;
        }

        User user = User.of(originalName, uuid);

        if (jsonObject.has("nickName")) {
            user.nickName = jsonObject.get("nickName").getAsString();
        }
        if (jsonObject.has("fakeRang")) {
            user.fakeRang = plugin.getRangManager().getRang(jsonObject.get("fakeRang").getAsString());
        }
        if (jsonObject.has("money")) {
            user.money = jsonObject.get("money").getAsInt();
        }
        if (jsonObject.has("rang")) {
            user.rang = plugin.getRangManager().getRang(jsonObject.get("rang").getAsString());
        }
        if (jsonObject.has("isVanish")) {
            user.isVanish = jsonObject.get("isVanish").getAsBoolean();
        }
        if (jsonObject.has("language")) {
            user.language = Language.getLanguage(jsonObject.get("language").getAsString());
        }
        if (jsonObject.has("mute")) {
            user.mute = jsonObject.get("mute").getAsString();
        }

        if (jsonObject.has("priority")) {
            JsonObject priorityObject = jsonObject.getAsJsonObject("priority");

            if (priorityObject.has("see")) {
                JsonObject seeObject = priorityObject.getAsJsonObject("see");
                if (seeObject.has("nick")) {
                    user.nickSeePriority = seeObject.get("nick").getAsInt();
                }
                if (seeObject.has("vanish")) {
                    user.vanishSeePriority = seeObject.get("vanish").getAsInt();
                }
            }

            if (priorityObject.has("use")) {
                JsonObject useObject = priorityObject.getAsJsonObject("use");
                if (useObject.has("nick")) {
                    user.nickUsePriority = useObject.get("nick").getAsInt();
                }
                if (useObject.has("vanish")) {
                    user.vanishUsePriority = useObject.get("vanish").getAsInt();
                }
            }
        }

        return user;
    }

    public static void saveUser(File file) {
        try (Writer writer = new FileWriter(file)) {
            JsonArray jsonArray = new JsonArray();
            for (User user : users) {
                jsonArray.add(user.toJson());
            }
            plugin.getGson().toJson(jsonArray, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public JsonElement toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("originalName", originalName);
        jsonObject.addProperty("nickName", nickName);
        jsonObject.addProperty("fakeRang", fakeRang.id());
        jsonObject.addProperty("money", money);
        jsonObject.addProperty("rang", rang.id());
        jsonObject.addProperty("uuid", uuid.toString());
        jsonObject.addProperty("isVanish", isVanish);
        jsonObject.addProperty("language", language.name);
        jsonObject.addProperty("mute", mute);

        JsonObject priorityObject = new JsonObject();

        JsonObject seeObject = new JsonObject();
        seeObject.addProperty("nick", nickSeePriority);
        seeObject.addProperty("vanish", vanishSeePriority);

        JsonObject useObject = new JsonObject();
        useObject.addProperty("nick", nickUsePriority);
        useObject.addProperty("vanish", vanishUsePriority);

        priorityObject.add("see", seeObject);
        priorityObject.add("use", useObject);

        jsonObject.add("priority", priorityObject);

        return jsonObject;
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
//        permissionAttachment.setPermission(permission, state);
    }
    public void unsetPermission(String permission) {
        permissions.remove(permission);
//        permissionAttachment.unsetPermission(permission);
    }
}
