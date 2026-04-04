package me.soapiee.deathholos.utils;

import lombok.Getter;

public enum ConfigPath {

    DEBUG_MODE("debug_mode", false),
    PLUGIN_NOTIF("settings.update_notification", true),
    LANGUAGE("settings.language", "lang_en"),
    DESPAWN_TIME("settings.despawn_time", 10),
    HOLO_HEIGHT("settings.height_above_death_location", 0.5),
    PLAYER_KILL_ONLY("settings.hologram_on_player_kill_only", false),
    MAX_HOLOS("settings.max_hologram_per_player", 5),
    PRIORITISE_HOOK("settings.prioritize_hologram_hook", true),
    HOLOGRAM_SECTION("hologram_groups", "hologram_groups"),
    HOLOGRAM_PRIORITY("hologram_groups.{KEY}.priority", "-1"),
    HOLOGRAM_PERMISSION("hologram_groups.{KEY}.permission", null),
    HOLOGRAM_DESIGN("hologram_groups.{KEY}.design", null);

    @Getter private final String path;
    @Getter private final Object defaultValue;

    ConfigPath(String path, Object defaultValue) {
        this.path = path;
        this.defaultValue = defaultValue;
    }
}
