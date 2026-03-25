package me.soapiee.deathholos.internals;

import lombok.Getter;
import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.managers.MessageManager;
import me.soapiee.deathholos.utils.CustomLogger;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;

public class InternalsManager {

    @Getter private final HologramHandler hologramHandler;

    private final CustomLogger customLogger;
    private final MessageManager messageManager;

    public InternalsManager(DeathHolos main) {
        customLogger = main.getCustomLogger();
        messageManager = main.getMessageManager();

        int majorVersion = Utils.getMajorVersion();
        int minorVersion = Utils.getMinorVersion();

        hologramHandler = getHologramHandler(majorVersion, minorVersion);
    }

    private HologramHandler getHologramHandler(int majorVersion, int minorVersion) {
        HologramHandler hologramHandler;

        try {
            String packageName = InternalsManager.class.getPackage().getName();
            String providerName = "HologramHandlerLegacy";
            if (majorVersion == 19 && minorVersion > 3) providerName = "HologramHandler_1_19_4";
            if (majorVersion > 19) providerName = "HologramHandler_1_19_4";

            hologramHandler = (HologramHandler) Class.forName(packageName + "." + providerName).newInstance();

        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                 ClassCastException | IllegalArgumentException ex) {
            customLogger.logToFile(ex, messageManager.get(Message.LEGACYHOLOGRAMS));
            hologramHandler = new HologramHandlerLegacy();
        }

        return hologramHandler;
    }
}
