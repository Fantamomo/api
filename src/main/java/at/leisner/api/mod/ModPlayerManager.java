package at.leisner.api.mod;

import at.leisner.api.Api;
import at.leisner.api.player.PlayerDataManager;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ModPlayerManager implements Listener {

//    @EventHandler
//    public void onPlayerJoinEvent(PlayerJumpEvent event) {
//        MessageManager playerDataManager = Api.getInstance().getMessageManager();
//        ByteArrayDataOutput out = ByteStreams.newDataOutput();
//        out.writeUTF("Forward");
//        out.writeUTF("ALL"); // This is the target server. "ALL" will message all servers apart from the one sending the message
//        out.writeUTF("SecretInternalChannel"); // This is the channel.
//        // The response will be handled in onPluginMessageReceived
//        playerDataManager.sendMessageToPlayer(event.getPlayer(),out);
//    }
}
