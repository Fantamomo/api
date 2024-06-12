package at.leisner.api.util;


import at.leisner.api.API;
import at.leisner.api.lang.Key;
import at.leisner.api.user.User;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.entity.Player;
import net.minecraft.network.protocol.game.ClientboundSetPlayerTeamPacket;
import net.minecraft.network.chat.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class NMSUtils {
    private static final Map<UUID, PlayerTeam> playerTeamMap = new HashMap<>();
    public static void sendFakePlayerTeam(Player player, Player viewer, Component prefix, Component suffix) {
        ServerPlayer serverPlayer = ((CraftPlayer) viewer).getHandle();
        PlayerTeam playerTeam = new PlayerTeam(serverPlayer.getScoreboard(), "fake_team_"+player.getUniqueId());
        playerTeam.setPlayerPrefix(prefix);
        playerTeam.setPlayerSuffix(suffix);
        playerTeam.getPlayers().add(player.getName());
        playerTeamMap.put(player.getUniqueId(), playerTeam);
        serverPlayer.connection.sendPacket(ClientboundSetPlayerTeamPacket.createAddOrModifyPacket(playerTeam, true));
    }
    public static void sendRemoveFakeTeam(Player player, UUID uuid) {
        ServerPlayer serverPlayer = ((CraftPlayer) player).getHandle();
        serverPlayer.connection.sendPacket(ClientboundSetPlayerTeamPacket.createRemovePacket(playerTeamMap.get(uuid)));
    }
    public static TextComponent addHoverEvent(TextComponent name, User user, User forUser) {
        return name.hoverEvent(HoverEvent.showText(API.getInstance().miniMessage().deserialize(forUser.getTranslateMessage(
                "chat.hover.info",
                Key.of("player-name", user.getOriginalName()),
                Key.of("player-uuid", String.valueOf(user.getPlayer().getUniqueId())),
                Key.of("player-pos", ((int) user.getPlayer().getX()) +" "+ ((int) user.getPlayer().getY())+" "+ ((int) user.getPlayer().getZ()))
        ))));
    }

    public static PlayerTeam removeTeam(UUID key) {
        return playerTeamMap.remove(key);
    }
}
