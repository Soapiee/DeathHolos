package me.soapiee.deathholos.managers;

import lombok.Getter;
import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.utils.ConfigPath;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Set;

public class ConfigManager {

    private final DeathHolos main;
    private FileConfiguration config;

    @Getter private boolean debugMode;
    @Getter private boolean updateNotif;
    @Getter private int despawnTimer;
    @Getter private double heightOffset;
    @Getter private boolean playerKillsOnly;
    @Getter private int maxHolos;
    @Getter private boolean prioritiseHook;
    @Getter private Set<String> groupKeys;

    public ConfigManager(DeathHolos main) {
        this.main = main;

        load();
    }

    private void load() {
        config = main.getConfig();
        debugMode = getBoolean(ConfigPath.DEBUG_MODE);
        updateNotif = getBoolean(ConfigPath.PLUGIN_NOTIF);
        despawnTimer = getInt(ConfigPath.DESPAWN_TIME);
        heightOffset = getDouble(ConfigPath.HOLO_HEIGHT);
        playerKillsOnly = getBoolean(ConfigPath.PLAYER_KILL_ONLY);
        maxHolos = getInt(ConfigPath.MAX_HOLOS);
        prioritiseHook = getBoolean(ConfigPath.PRIORITISE_HOOK);
        groupKeys = config.getConfigurationSection(ConfigPath.HOLOGRAM_SECTION.getPath()).getKeys(false);
    }

    public void reload() {
        main.reloadConfig();
        load();
    }

    private Boolean getBoolean(ConfigPath rawPath) {
        return config.getBoolean(rawPath.getPath(), (Boolean) rawPath.getDefaultValue());
    }

    private int getInt(ConfigPath rawPath) {
        return config.getInt(rawPath.getPath(), (int) rawPath.getDefaultValue());
    }

    private double getDouble(ConfigPath rawPath) {
        return config.getDouble(rawPath.getPath(), (double) rawPath.getDefaultValue());
    }

    public String getStringAndReplace(ConfigPath rawPath, String key) {
        String path = rawPath.getPath().replace("{KEY}", key);

        return config.getString(path, (String) rawPath.getDefaultValue());
    }

    public List<String> getListAndReplace(ConfigPath rawPath, String key) {
        String path = rawPath.getPath().replace("{KEY}", key);

        if (!config.isSet(path)) return null;
        if (!config.isList(path)) return null;
        return config.getStringList(path);
    }

}
