package at.leisner.api.nick;

import at.leisner.api.API;
import at.leisner.api.user.User;
import at.leisner.api.util.MojangApi;
import at.leisner.api.util.NMSUtils;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoRemovePacket;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraft.network.protocol.game.ClientboundRespawnPacket;
import net.minecraft.network.protocol.game.CommonPlayerSpawnInfo;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class NickManager {
    private final Map<Player, GameProfile> nickedPlayers = new HashMap<>();
    private final API plugin;

    public NickManager(API plugin) {
        this.plugin = plugin;
        for (Player player : Bukkit.getOnlinePlayers()) {
            User user = User.of(player);
            if (user.getNickName() != null && !user.getNickName().isBlank()) {
                nickPlayer(player, user.getNickName(), null);
            }
        }
    }

    public void nickPlayer(Player player, String nickName, String skinUrl) {
        User user = User.of(player);
        user.setNickName(nickName);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        if (!nickedPlayers.containsKey(player)) nickedPlayers.put(player, nmsPlayer.gameProfile);

        GameProfile profile = new GameProfile(UUID.randomUUID(), nickName);
//        String[] skinData = skinUrl != null ? new String[]{skinUrl, "SIGNATURE"} : getSkinFromMojang(nickName);

//        if (skinData != null) {
////            propertyMap.put("textures", new Property("textures", skinData[0], skinData[1]));
//
//        }
//        if (skinUrl != null) {
            changeSkin(profile, nickName);
//        }
        nmsPlayer.gameProfile = profile;
        if (skinUrl != null) {
            try {
                nmsPlayer.gameProfile.getProperties();
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
                NMSUtils.sendFakePlayerTeam(player, p, null, Component.literal(" [N]").withColor(0x90EE90));
            }
            ServerGamePacketListenerImpl conn = ((CraftPlayer) p).getHandle().connection;
            conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsPlayer));
        }
    }

    public void resetNick(Player player) {
        User.of(player).setNickName(null);
        CraftPlayer craftPlayer = (CraftPlayer) player;
        ServerPlayer nmsPlayer = craftPlayer.getHandle();
        UUID uuid = player.getUniqueId();

        nmsPlayer.gameProfile = nickedPlayers.remove(player);
//        nickedPlayers.remove(player);

        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.canSee(player)) {
                p.hidePlayer(plugin, player);
                p.showPlayer(plugin, player);
            }
            NMSUtils.sendRemoveFakeTeam(p, uuid);
            ServerGamePacketListenerImpl conn = ((CraftPlayer) p).getHandle().connection;
            conn.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsPlayer));
        }
        NMSUtils.removeTeam(uuid);
    }
    public void changeSkin(GameProfile profile, String newPlayerName) {
        String uuid = MojangApi.getUUID(newPlayerName);
        if (uuid == null) {
            plugin.getLogger().warning("Could not fetch UUID for player: " + newPlayerName);
            return;
        }

        MojangApi.SkinData skinData = MojangApi.getSkinData(uuid);
        if (skinData == null) {
            plugin.getLogger().warning("Could not fetch skin data for UUID: " + uuid);
            return;
        }

        applySkin(profile, skinData.value(), skinData.signature());
    }
    public void reloadPlayer(CraftPlayer craftPlayer) {
        ServerPlayer serverPlayer = craftPlayer.getHandle();
        ServerGamePacketListenerImpl connection = serverPlayer.connection;
        Level world = serverPlayer.level();

        connection.send(new ClientboundPlayerInfoRemovePacket(List.of(serverPlayer.getUUID())));
        connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));

        boolean isDebug = world.isDebug();
        boolean isFlat = false;
        connection.send(new ClientboundRespawnPacket(new CommonPlayerSpawnInfo(Holder.direct(world.dimensionType()),
                world.dimension(),
                craftPlayer.getWorld().getSeed(),
                serverPlayer.gameMode.getGameModeForPlayer(),
                serverPlayer.gameMode.getPreviousGameModeForPlayer(),
                isDebug,
                isFlat,
                serverPlayer.getLastDeathLocation(),
                serverPlayer.getPortalCooldown()), ClientboundRespawnPacket.KEEP_ALL_DATA));

        serverPlayer.onUpdateAbilities();
        serverPlayer.sendServerStatus(((CraftServer) plugin.getServer()).getHandle().getServer().getStatus());
        craftPlayer.updateScaledHealth();
        craftPlayer.getHandle().connection.teleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.yHeadRot, serverPlayer.xRotO);
    }

    private void applySkin(GameProfile profile, String skinValue, String skinSignature) {
        profile.getProperties().removeAll("textures");
        profile.getProperties().put("textures", new Property("textures", skinValue, skinSignature));
    }
//    private String[] getSkinFromMojang(String playerName) {
//        try {
//            // Abrufen der UUID des Spielers
//            URL uuidUrl = new URL("https://api.mojang.com/users/profiles/minecraft/" + playerName);
//            HttpURLConnection uuidConnection = (HttpURLConnection) uuidUrl.openConnection();
//            uuidConnection.setRequestMethod("GET");
//            uuidConnection.setRequestProperty("Content-Type", "application/json");
//            BufferedReader uuidIn = new BufferedReader(new InputStreamReader(uuidConnection.getInputStream()));
//            String uuidResponse = uuidIn.readLine();
//            uuidIn.close();
//
//            if (uuidResponse == null) {
//                return null;
//            }
//
//            JsonObject uuidJson = JsonParser.parseString(uuidResponse).getAsJsonObject();
//            String uuid = uuidJson.get("id").getAsString();
//
//            // Abrufen der Textureninformationen des Spielers
//            URL textureUrl = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
//            HttpURLConnection textureConnection = (HttpURLConnection) textureUrl.openConnection();
//            textureConnection.setRequestMethod("GET");
//            textureConnection.setRequestProperty("Content-Type", "application/json");
//            BufferedReader textureIn = new BufferedReader(new InputStreamReader(textureConnection.getInputStream()));
//            String textureResponse = textureIn.readLine();
//            textureIn.close();
//
//            if (textureResponse == null) {
//                return null;
//            }
//
//            JsonObject textureJson = JsonParser.parseString(textureResponse).getAsJsonObject();
//            JsonArray properties = textureJson.getAsJsonArray("properties");
//            JsonObject textureProperty = properties.get(0).getAsJsonObject();
//
//            String value = textureProperty.get("value").getAsString();
//            String signature = textureProperty.get("signature").getAsString();
//
//            return new String[]{value, signature};
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
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
