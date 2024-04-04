package at.leisner.api;

import at.leisner.api.chat.ChatEventListener;
import at.leisner.api.cmd.FreezeCMD;
import at.leisner.api.cmd.RawCMD;
import at.leisner.api.cmd.SendCMD;
import at.leisner.api.cmd.TestCMD;
import at.leisner.api.mod.MessageManager;
import at.leisner.api.mod.ModPlayerManager;
import at.leisner.api.player.FreezeManager;
import at.leisner.api.player.PlayerDataManager;
import at.leisner.api.rang.RangManager;
import at.leisner.api.util.Formater;
import at.leisner.api.util.TabList;
import at.leisner.api.util.Translator;
import at.leisner.api.vanish.VanishCMD;
import at.leisner.api.vanish.VanishEvents;
import at.leisner.api.vanish.VanishManager;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class Api extends JavaPlugin {
    private static Api plugin;
    private RangManager rangManager;
    private PlayerDataManager playerDataManager;
    private VanishManager vanishManager;
    private CommandMap commandMap;
    private MessageManager messageManager;
    private FreezeManager freezeManager;
    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();
        plugin = this;
        new Translator();
        commandMap = getServer().getCommandMap();
        rangManager = new RangManager();
        playerDataManager = new PlayerDataManager();
        vanishManager = new VanishManager();
        messageManager = new MessageManager();
        freezeManager = new FreezeManager();
        onStart();
        new TabList();

    }
    private void onStart() {
        getServer().getPluginManager().registerEvents(new Formater(),this);
        getServer().getPluginManager().registerEvents(new ModPlayerManager(),this);
        getServer().getPluginManager().registerEvents(freezeManager, this);
        getServer().getPluginManager().registerEvents(new ChatEventListener(), this);
        getServer().getPluginManager().registerEvents(new VanishEvents(), this);
        commandMap.register("vanish","api",new VanishCMD());
        commandMap.register("raw","api",new RawCMD());
        commandMap.register("send","api",new SendCMD());
        commandMap.register("freeze","api", new FreezeCMD());
        commandMap.register("test","api", new TestCMD());
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        try {
            playerDataManager.getConfig().save(playerDataManager.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.saveConfig();

    }

    public static Api getInstance() {
        return plugin;
    }

    public RangManager getRangManager() {
        return rangManager;
    }

    public PlayerDataManager getPlayerDataManager() {
        return playerDataManager;
    }

    public VanishManager getVanishManager() {
        return vanishManager;
    }

    public CommandMap getCommandMap() {
        return commandMap;
    }

    public MessageManager getMessageManager() {
        return messageManager;
    }

    public FreezeManager getFreezeManager() {
        return freezeManager;
    }
}
