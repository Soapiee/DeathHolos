package me.soapiee.deathholos;

import lombok.Getter;
import me.soapiee.deathholos.commands.AdminCmd;
import me.soapiee.deathholos.internals.InternalsManager;
import me.soapiee.deathholos.listeners.ConnectListener;
import me.soapiee.deathholos.listeners.DeathListener;
import me.soapiee.deathholos.logic.GroupFactory;
import me.soapiee.deathholos.managers.ConfigManager;
import me.soapiee.deathholos.managers.HologramManager;
import me.soapiee.deathholos.managers.MessageManager;
import me.soapiee.deathholos.managers.UpdateManager;
import me.soapiee.deathholos.utils.CustomLogger;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class DeathHolos extends JavaPlugin {

    @Getter private MessageManager messageManager;
    @Getter private CustomLogger customLogger;
    @Getter private InternalsManager internalsManager;
    @Getter private ConfigManager configManager;
    @Getter private HologramManager holoManager;
    @Getter private boolean placeholderAPIHooked;
    @Getter private boolean decentHologramsHooked;
    @Getter private UpdateManager updateManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        messageManager = new MessageManager(this);
        customLogger = new CustomLogger(this);
        new Metrics(this, 30432);

        registerManagers();
//        registerHooks(); // Moved to registerManagers() method
        registerListeners();
        registerCommands();

        updateManager = new UpdateManager(this, 133766);
        updateManager.updateAlert(Bukkit.getConsoleSender());
    }

    @Override
    public void onDisable() {
        holoManager.clearAllHolos();
    }

    private void registerManagers() {
        configManager = new ConfigManager(this);
        registerHooks();
        internalsManager = new InternalsManager(this);
        GroupFactory groupFactory = new GroupFactory(this);
        holoManager = new HologramManager(this, groupFactory);
    }

    private void registerHooks() {
        placeholderAPIHooked = false;
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            if (!getServer().getPluginManager().getPlugin("PlaceholderAPI").isEnabled()) return;
            placeholderAPIHooked = true;
            if (configManager.isDebugMode()) Utils.consoleMsg(messageManager.get(Message.HOOKEDPLACEHOLDERAPI));
        }

        decentHologramsHooked = false;
        if (getServer().getPluginManager().getPlugin("DecentHolograms") != null) {
            if (!getServer().getPluginManager().getPlugin("DecentHolograms").isEnabled()) return;
            if (!configManager.isPrioritiseHook()) return;
            decentHologramsHooked = true;
            Utils.consoleMsg(messageManager.get(Message.HOOKEDDECENTHOLOGRAMS));
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ConnectListener(this), this);
        getServer().getPluginManager().registerEvents(new DeathListener(this), this);
    }

    private void registerCommands() {
        getCommand("adeathholos").setExecutor(new AdminCmd(this));
        // TODO:
//        getCommand("deathholos").setExecutor(new PlayerCmd(this));
    }
}
