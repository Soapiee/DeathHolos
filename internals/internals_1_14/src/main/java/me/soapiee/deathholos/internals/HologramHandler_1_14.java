package me.soapiee.deathholos.internals;

import me.soapiee.deathholos.logic.Hologram;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.persistence.PersistentDataType;

public class HologramHandler_1_14 implements HologramHandler {

    @Override public void spawnHologram(Hologram holo) {
        Location location = holo.getLocation().clone();
        location.setY((location.getY() - 2.5)); // Only needed for amrour stands

        for (String line : holo.getText()) {
            ArmorStand armourStand = location.getWorld().spawn(location, ArmorStand.class);
            armourStand.setVisible(false);
            armourStand.setGravity(false);
            armourStand.setInvulnerable(true);
            armourStand.getPersistentDataContainer().set(Keys.HOLOGRAMKEY, PersistentDataType.STRING, holo.getKeyID());
            armourStand.setCustomNameVisible(true);
            armourStand.setCustomName(Utils.addColour(line));
            location.subtract(0, 0.25, 0);
        }
    }

    @Override public void despawn(Hologram holo) {
        Location location = holo.getLocation();
        for (Entity entity : location.getWorld().getNearbyEntities(location, 5, 5, 5)) {
            if (entity instanceof ArmorStand && entity.getPersistentDataContainer().has(Keys.HOLOGRAMKEY, PersistentDataType.STRING)) {
                String id = entity.getPersistentDataContainer().get(Keys.HOLOGRAMKEY, PersistentDataType.STRING);
                if (id.equalsIgnoreCase(holo.getKeyID())) entity.remove();
            }
        }
    }
}
