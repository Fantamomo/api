package at.leisner.api.mod;

import at.leisner.api.Api;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MessageManager implements PluginMessageListener {
    private Api plugin;
    private String channel = "fanta:mod";

    public MessageManager() {
        plugin = Api.getInstance();
        plugin.getServer().getMessenger().registerOutgoingPluginChannel(plugin, channel);
        plugin.getServer().getMessenger().registerIncomingPluginChannel(plugin, channel, this);
    }

    public void sendMessageToPlayer(Player p, ByteArrayDataOutput msg) {
        p.sendPluginMessage(plugin, channel, msg.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals(this.channel)) return;
//        ByteArrayDataInput in = ByteStreams.newDataOutput(message);
//        try {
//            String cmd = in.();
//        } catch (Exception e) {
//            plugin.getLogger().info("Exception class: "+e.getClass());
//            e.printStackTrace();
//        }
//        while (true) {
//            try {
//                msg.add(in.readUTF());
//            } catch (Exception e) {
//                plugin.getLogger().info("Exception class: "+e.getClass());
//                e.printStackTrace();
//                break;
//            }
//        }
        String[] msg = new String(message).split("<\n>");


        // Erstellt einen ByteArrayInputStream und DataInputStream, um die Daten zu lesen
        plugin.getLogger().info("From Client \"" + player.getName() + "\": " + Arrays.toString(msg));

    }
}
