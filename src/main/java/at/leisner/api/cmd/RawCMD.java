package at.leisner.api.cmd;

import at.leisner.api.Api;
import at.leisner.api.mod.MessageManager;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class RawCMD extends Command {
    public RawCMD() {
        super("raw","Raw Command", "/raw <Player> [msg]", new ArrayList<>());
        this.setPermission("cmd.raw");
    }

    @Override
    public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
        if (strings.length < 2) return false;
        Player p = Bukkit.getPlayerExact(strings[0]);
        boolean isFirst = true;
        if (p == null) {
            commandSender.sendMessage("Error Player");
            return false;
        }
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        for (String arg : strings) {
            if (isFirst) {
                isFirst = false;
                continue;
            }
            out.writeUTF(arg);
        }
        Api.getInstance().getMessageManager().sendMessageToPlayer(p,out);

        return false;
    }
}
