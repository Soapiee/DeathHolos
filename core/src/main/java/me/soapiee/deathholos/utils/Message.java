package me.soapiee.deathholos.utils;

public enum Message {

    //                    --->    GENERAL MESSAGES    <---
    PREFIX("prefix", "#17d5eb&l[DH]&r"), // Replace to "" if you want no prefix
    MUSTBEPLAYERERROR("must_be_player", "&cYou must be a player to use this command"),
    NOPERMISSION("no_permission", "&cYou do not have permission to use this command"),
    LANGUAGEFIELDERROR("language_field_error", "&cCould not add new fields to the language file"),
    INVALIDLANGUAGE("invalid_language", "&cAn invalid language has been configured. Defaulting to \"lang_en\""),
    HOOKEDPLACEHOLDERAPI("hooked_placeholderapi", "&aHooked into PlaceholderAPI"),
    LOGGERFILEERROR("logger_file_error", "&cThe logger.log file could not be created"),
    LOGGERLOGSUCCESS("logger_log_success", "&cAn error was added to the logger.log file"),
    LOGGERLOGERROR("logger_log_error", "&cA new error log failed to be saved"),
    INVALIDHOLOGROUP("invalid_hologram", "&cA hologram with that identifier does not exist"),
    GROUPPRIORITYERROR("invalid_group_priority", "&cInvalid priority for hologram group &e%group_identifier%"),
    GROUPPRIORITYDUPE("duplicate_group_priority", "&cThe priority set for hologram group &e%group_identifier% already exists"),
    GROUPPERMISSIONERROR("invalid_group_permission", "&cInvalid permission for hologram group &e%group_identifier%"),
    GROUPDESIGNERROR("invalid_group_design", "&cInvalid design for hologram group &e%group_identifier%"),
    GROUPCREATED("group_created", "&aGroup &e%group_identifier% &awas successfully created"),
    LEGACYHOLOGRAMS("legacy_holograms_enabled", "&aYour version is below 1.19.4 so the legacy hologram system has been enabled"),
    DEATHCAUSEERROR("death_cause_error", "&cThere was an error with the players cause of death. Send the log to the developer"),

    //                    --->    ADMIN MESSAGES    <---
    UPDATEAVAILABLE("update_available", "&aThere is an update available for DeathHolos"),
    RELOADSUCCESS("reload_success", "&aSuccessfully reloaded DeathHolos"),
    RELOADINPROGRESS("reload_inprogress", "&eReloading configuration..."),
    RELOADERROR("reload_error", "&cError reloading DeathHolos"),
    CMDHOLOCAUSE("admin_command_death_cause", "testing too much"),
    CMDHOLOKILLER("admin_command_killer", "Notch"),
    HOLOSPAWNED("hologram_spawned", "&aYou spawned a hologram at your location"),
    HOLOSPAWNEDMAX("hologram_spawned_max", "&cThe maximum number of holograms for you, has been reached. Please wait"),
    ADMINHELP("admin_help", "#17d5eb--------- Admin Help ---------"
                    + "\n#17d5ebKey: < > = Optional | [ ] = Required"
                    + "\n#17d5eb/%cmd_label% reload &7- Reloads the plugin"
                    + "\n#17d5eb/%cmd_label% spawn <group id> &7- Spawns a hologram at your location, of the specified hologram ID/priority"),

    //                    --->    1.16 MINECRAFT DEATH CAUSES    <---
    BLOCK_EXPLOSION("death_cause_block_explosion", "explosions"),
    CONTACT("death_cause_contact", "plants"),
    CRAMMING("death_cause_cramming", "overcrowding"),
    CUSTOM("death_cause_custom", "custom"),
    DRAGON_BREATH("death_cause_dragon_breath", "dragon fire"),
    DROWNING("death_cause_drowning", "drowning"),
    DRYOUT("death_cause_dryout", "not being under water"),
    ENTITY_ATTACK("death_cause_entity_attack", "an attack"),
    ENTITY_EXPLOSION("death_cause_entity_explosion", "a creeper"),
    ENTITY_SWEEP_ATTACK("death_cause_sweep_attack", "a sweep attack"),
    FALL("death_cause_fall", "fall damage"),
    FALLING_BLOCK("death_cause_falling_block", "falling blocks"),
    FIRE("death_cause_fire", "fire"),
    FIRE_TICK("death_cause_fire_tick", "fire"),
    FLY_INTO_WALL("death_cause_fly_into_wall", "going splat"),
    HOT_FLOOR("death_cause_hot_floor", "magma blocks"),
    LAVA("death_cause_lava", "lava"),
    LIGHTNING("death_cause_lightning", "lightning"),
    MAGIC("death_cause_magic", "potions"),
    MELTING("death_cause_melting", "melting"),
    POISON("death_cause_poison", "poison"),
    PROJECTILE("death_cause_projectile", "a projectile"),
    STARVATION("death_cause_starvation", "starvation"),
    SUFFOCATION("death_cause_suffocation", "suffocation"),
    SUICIDE("death_cause_suicide", "commiting suicide"),
    THORNS("death_cause_thorns", "prickly armor"),
    VOID("death_cause_void", "the void"),
    WITHER("death_cause_wither", "a wither"),
    //                    --->    1.17 MINECRAFT DEATH CAUSES    <---,
    FREEZE("death_cause_freeze", "freezing"),
    //                    --->    1.19 MINECRAFT DEATH CAUSES    <---,
    SONIC_BOOM("death_cause_sonic_boom", "a Warden"),
    //                    --->    1.20 MINECRAFT DEATH CAUSES    <---,
    KILL("death_cause_kill", "the kill command"),
    WORLD_BORDER("death_cause_world_border", "the world border"),
    //                    --->    1.21 MINECRAFT DEATH CAUSES    <---,
    CAMPFIRE("death_cause_campfire", "fire");

    public final String path;
    private final String defaultText;

    Message(String path, String defaultText) {
        this.path = path;
        this.defaultText = defaultText;
    }

    public String getPath() {
        return path;
    }

    public String getDefaultText() {
        return defaultText;
    }
}
