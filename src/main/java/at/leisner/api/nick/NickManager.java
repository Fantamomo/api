package at.leisner.api.nick;

import at.leisner.api.API;
import at.leisner.api.user.User;
import at.leisner.api.util.NMSUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.profile.PlayerTextures;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NickManager {
    private final Map<Player, GameProfile> nickedPlayers = new HashMap<>();
    private final API plugin;

    public NickManager(API plugin) {
        this.plugin = plugin;
    }

    public void nickPlayer(Player player, String nickName, String skinUrl) {
        User user = User.of(player);
        user.setNickName(nickName);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        if (!nickedPlayers.containsKey(player)) nickedPlayers.put(player, nmsPlayer.gameProfile);

        GameProfile profile = new GameProfile(UUID.randomUUID(), nickName);
        PropertyMap propertyMap = profile.getProperties();

        String[] skinData = skinUrl != null ? new String[]{skinUrl, "SIGNATURE"} : getSkinFromMojang(nickName);

        if (skinData != null) {
//            propertyMap.put("textures", new Property("textures", skinData[0], skinData[1]));

        }

        nmsPlayer.gameProfile = profile;
        if (skinUrl != null) {
            try {
                player.getPlayerProfile().getTextures().setSkin(new URL(skinUrl));
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
        for (Player p : Bukkit.getOnlinePlayers()) {
            User u = User.of(p);
            if (p.canSee(player)) {
                p.hidePlayer(plugin, player);
                p.showPlayer(plugin, player);
            }
            if (u.nickSeePriority > user.nickUsePriority) {
                NMSUtils.setFakePlayerTeam(player, p, null, Component.literal(" [N]").withColor(0x90EE90));
            }
            ServerGamePacketListenerImpl conn = ((CraftPlayer) p).getHandle().connection;
            conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsPlayer));
        }
    }

    public void resetNick(Player player) {
        User.of(player).setNickName(null);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();

        nmsPlayer.gameProfile = nickedPlayers.get(player);
        nickedPlayers.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.hidePlayer(plugin, player);
                p.showPlayer(plugin, player);
            }
            ServerGamePacketListenerImpl conn = ((CraftPlayer) p).getHandle().connection;
            conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsPlayer));
        }
    }

    private String[] getSkinFromMojang(String playerName) {
        try {
            // Abrufen der UUID des Spielers
            URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
            HttpURLConnection uuidConnection = (HttpURLConnection) uuidUrl.openConnection();
            uuidConnection.setRequestMethod("GET");
            uuidConnection.setRequestProperty("Content-Type", "application/json");
            BufferedReader uuidIn = new BufferedReader(new InputStreamReader(uuidConnection.getInputStream()));
            String uuidResponse = uuidIn.readLine();
            uuidIn.close();

            if (uuidResponse == null) {
                return null;
            }

            JsonObject uuidJson = JsonParser.parseString(uuidResponse).getAsJsonObject();
            String uuid = uuidJson.get("id").getAsString();

            // Abrufen der Textureninformationen des Spielers
            URL textureUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            HttpURLConnection textureConnection = (HttpURLConnection) textureUrl.openConnection();
            textureConnection.setRequestMethod("GET");
            textureConnection.setRequestProperty("Content-Type", "application/json");
            BufferedReader textureIn = new BufferedReader(new InputStreamReader(textureConnection.getInputStream()));
            String textureResponse = textureIn.readLine();
            textureIn.close();

            if (textureResponse == null) {
                return null;
            }

            JsonObject textureJson = JsonParser.parseString(textureResponse).getAsJsonObject();
            JsonArray properties = textureJson.getAsJsonArray("properties");
            JsonObject textureProperty = properties.get(0).getAsJsonObject();

            String value = textureProperty.get("value").getAsString();
            String signature = textureProperty.get("signature").getAsString();

            return new String[]{value, signature};
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public Set<Player> getNickedPlayers() {
        return nickedPlayers.keySet();
    }
    public boolean isPlayerNicked(Player player) {
        return nickedPlayers.containsKey(player);
    }
    public Player getPlayerByInGameName(String name) {
        Player player = Bukkit.getPlayerExact(name);
        if (player != null) return player;
        for (Player player1 : nickedPlayers.keySet()) {
            if (player1.getName().equals(name)) {
                return player1;
            }
        }
        return null;
    }
    public boolean isNameFree(String name) {
        return getPlayerByInGameName(name) == null;
    }

    public Player getPlayerExact(String name) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(name)) {
                return player;
            }
        }
        return null;
    }
}
