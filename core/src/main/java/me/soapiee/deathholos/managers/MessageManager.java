package me.soapiee.deathholos.managers;

import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.utils.ConfigPath;
import me.soapiee.deathholos.utils.Languages;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.entity.EntityDamageEvent;

import java.io.File;

public class MessageManager {

    private final DeathHolos main;

    private final File file;
    private final YamlConfiguration contents;
    private final String language;

    public MessageManager(DeathHolos main) {
        this.main = main;

        String configString = main.getConfig().getString(ConfigPath.LANGUAGE.getPath(), (String) ConfigPath.LANGUAGE.getDefaultValue());
        String languageString = validateLanguage(configString);
        if (languageString == null) language = Languages.LANG_EN.toString().toLowerCase();
        else language = languageString;

        file = new File(main.getDataFolder() + File.separator + "language", language + ".yml");
        contents = new YamlConfiguration();

        load(Bukkit.getConsoleSender());

        if (languageString == null) Utils.consoleMsg(get(Message.INVALIDLANGUAGE));
    }

    private String validateLanguage(String configString) {
        Languages lang;
        try {
            lang = Languages.valueOf(configString.toUpperCase());
        } catch (IllegalArgumentException error) {
            return null;
        }

        return lang.toString().toLowerCase();
    }

    public boolean reload(CommandSender sender) {
        return load(sender);
    }

    private boolean load(CommandSender sender) {
        if (!file.exists()) main.saveResource("language" + File.separator + language + ".yml", false);

        try {
            contents.load(file);
        } catch (Exception ex) {
            if (sender != null) {
//                customLogger.logToPlayer(sender, ex, get(Message.RELOADERROR));
                ex.printStackTrace();
            }
        }
        return true;
    }

    private void save() {
        try {
            contents.save(file);
        } catch (Exception ex) {
            main.getCustomLogger().logToFile(ex, get(Message.LANGUAGEFIELDERROR));
        }
    }

    private String getPrefix(Message messageEnum) {
        if (messageEnum == Message.BLOCK_EXPLOSION || messageEnum == Message.CONTACT
                || messageEnum == Message.CRAMMING || messageEnum == Message.CUSTOM
                || messageEnum == Message.DRAGON_BREATH || messageEnum == Message.DROWNING
                || messageEnum == Message.DRYOUT || messageEnum == Message.ENTITY_ATTACK
                || messageEnum == Message.ENTITY_EXPLOSION || messageEnum == Message.ENTITY_SWEEP_ATTACK
                || messageEnum == Message.FALL || messageEnum == Message.FALLING_BLOCK
                || messageEnum == Message.FIRE || messageEnum == Message.FIRE_TICK
                || messageEnum == Message.FLY_INTO_WALL || messageEnum == Message.HOT_FLOOR
                || messageEnum == Message.LAVA || messageEnum == Message.LIGHTNING
                || messageEnum == Message.MAGIC || messageEnum == Message.MELTING
                || messageEnum == Message.POISON || messageEnum == Message.PROJECTILE
                || messageEnum == Message.STARVATION || messageEnum == Message.SUFFOCATION
                || messageEnum == Message.SUICIDE || messageEnum == Message.THORNS
                || messageEnum == Message.VOID || messageEnum == Message.WITHER
                || messageEnum == Message.FREEZE || messageEnum == Message.SONIC_BOOM
                || messageEnum == Message.KILL || messageEnum == Message.WORLD_BORDER
                || messageEnum == Message.CMDHOLOCAUSE || messageEnum == Message.CMDHOLOKILLER
                || messageEnum == Message.CAMPFIRE || messageEnum == Message.DEATHCAUSEERROR
                || messageEnum == Message.HOOKEDPLACEHOLDERAPI || messageEnum == Message.HOOKEDDECENTHOLOGRAMS
                || messageEnum == Message.NOPERMISSION
                || messageEnum == Message.ADMINHELP
//                || messageEnum == Message.PLAYERHELP
                || messageEnum == Message.LANGUAGEFIELDERROR || messageEnum == Message.INVALIDLANGUAGE
                || messageEnum == Message.LOGGERFILEERROR || messageEnum == Message.LOGGERLOGSUCCESS
                || messageEnum == Message.LOGGERLOGERROR) return "";

        String path = Message.PREFIX.getPath();
        if (contents.isSet(path)) {
            return contents.getString(path).isEmpty() ? "" : contents.getString(path) + " ";
        } else return "";
    }

    public String get(Message messageEnum) {
        String path = messageEnum.getPath();
        String defaultText = messageEnum.getDefaultText();

        if (contents.isSet(path)) {
            String text = ((contents.isList(path)) ? String.join("\n", contents.getStringList(path)) : contents.getString(path));

            return text.isEmpty() ? null : getPrefix(messageEnum) + text;
        } else {
            if (defaultText.contains("\n")) {
                String[] list;
                list = defaultText.split("\n");
                contents.set(path, list);
            } else {
                contents.set(path, defaultText);
            }
            save();
            return getPrefix(messageEnum) + defaultText;
        }
    }

    public String getWithoutPrefix(Message messageEnum) {
        String path = messageEnum.getPath();
        String defaultText = messageEnum.getDefaultText();

        if (contents.isSet(path)) {
            String text = ((contents.isList(path)) ? String.join("\n", contents.getStringList(path)) : contents.getString(path));

            return text.isEmpty() ? null : text;
        } else {
            if (defaultText.contains("\n")) {
                String[] list;
                list = defaultText.split("\n");
                contents.set(path, list);
            } else {
                contents.set(path, defaultText);
            }
            save();
            return defaultText;
        }
    }

    public String getWithPlaceholder(Message messageEnum, String string, boolean isConsole) {
        String message = isConsole ? getWithoutPrefix(messageEnum) : get(messageEnum);

        if (message.contains("%group_identifier%")) message = message.replace("%group_identifier%", string);
        if (message.contains("%cmd_label%")) message = message.replace("%cmd_label%", string);

        return message;
    }

    public String getCause(EntityDamageEvent.DamageCause cause) {
        Message messageEnum;

        try {
            messageEnum = Message.valueOf(cause.toString());
        } catch (IllegalArgumentException e) {
            main.getCustomLogger().logToFile(e, get(Message.DEATHCAUSEERROR));
            messageEnum = Message.CUSTOM;
        }

        return get(messageEnum);
    }
}
