package me.soapiee.deathholos.internals;

import me.soapiee.deathholos.logic.Hologram;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public interface HologramHandler {

    void spawnHologram(Hologram holo);
    void despawn(Hologram holo);

    default void spawn(Hologram holo){
        playSound(holo);
        spawnHologram(holo);
    }

    default void playSound(Hologram holo) {
        Sound sound = holo.getSound();
        if (sound == null) return;

        Location location = holo.getLocation();
        location.getWorld().playSound(location, sound, 1F, 1F);
    }
}
