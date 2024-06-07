package at.leisner.api;

import at.leisner.api.command.*;
import at.leisner.api.command.override.TellCommand;
import at.leisner.api.lang.Language;
import at.leisner.api.listener.ChatListener;
import at.leisner.api.listener.JoinQuitListener;
import at.leisner.api.nick.NickCommand;
import at.leisner.api.nick.NickManager;
import at.leisner.api.permission.PermissionManager;
import at.leisner.api.rang.RangManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class API extends JavaPlugin {
    private static API plugin;
    private NickManager nickManager;
    private RangManager rangManager;
    private Language language;
    private List<String> languages = List.of("en", "de");
    private MiniMessage miniMessage;
    private PermissionManager permissionManager;


    @Override
    public void onEnable() {
        plugin = this;
        miniMessage = MiniMessage.miniMessage();
        manageFile();
        register();
    }

    private void manageFile() {
        getDataFolder().mkdir();
        saveResource("lang_en.yml", true);
        saveResource("rang.yml", true);

        languages.forEach(name -> saveResource("lang_" + name + ".yml", true));
        language = new Language("en", getResource("lang_en.yml"));
        Map<String, File> langFiles = Arrays.stream(getDataFolder().listFiles()).filter((file) -> !file.isDirectory() &&
                file.getName().startsWith("lang_") &&
                file.getName().endsWith(".yml") &&
                file.getName().length() - 9 == 2 &&
                !file.getName().equals("lang_en.yml")
        ).collect(Collectors.toMap(
                (file) -> file.getName().replace("lang_", "").replace(".yml", ""),
                file -> file
        ));
        langFiles.forEach(
                (lang, file) -> {
                    try {
                        new Language(lang, file);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        );
    }

    private void register() {
        CommandMap commandMap = getServer().getCommandMap();
        commandMap.registerAll("api", List.of(
                new NickCommand(this),
                new FakeTeamCommand(),
                new SetLanguage(this),
                new TellCommand(this),
                new RangCommand(this),
                new UserCommand(this),
                new PermissionCommand(this),
                new IgnoreCommand(this),
                new ReportCommand(this),
                new PlayerInfoCommand(this)
        ));

        nickManager = new NickManager(this);
        rangManager = new RangManager(this);
        rangManager.loadRangs(getTextResource("rang.yml"));
        permissionManager = new PermissionManager(this, new File(getDataFolder(), "permission.dat"));
        permissionManager.loadPermissions();

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new ChatListener(this), this);
        pluginManager.registerEvents(new JoinQuitListener(this), this);
    }

    @Override
    public void onDisable() {
        permissionManager.savePermissions();
    }

    public static API getInstance() {
        return plugin;
    }

    public NickManager getNickManager() {
        return nickManager;
    }

    public RangManager getRangManager() {
        return rangManager;
    }

    public Language getLanguage() {
        return language;
    }

    public MiniMessage miniMessage() {
        return miniMessage;
    }

    public PermissionManager getPermissionManager() {
        return permissionManager;
    }
}
