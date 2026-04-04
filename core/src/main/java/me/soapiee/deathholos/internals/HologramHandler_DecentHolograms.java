package me.soapiee.deathholos.internals;

import eu.decentsoftware.holograms.api.DHAPI;
import me.soapiee.deathholos.logic.Hologram;

public class HologramHandler_DecentHolograms implements HologramHandler {

    @Override public void spawn(Hologram holo) {
        DHAPI.createHologram(holo.getKeyID(), holo.getLocation(), holo.getText());
    }

    @Override public void despawn(Hologram holo) {
        DHAPI.removeHologram(holo.getKeyID());
    }
}
