package me.soapiee.deathholos.internals;

import me.soapiee.deathholos.logic.Hologram;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public class HologramHandler_Legacy implements HologramHandler {

    @Override public void spawnHologram(Hologram holo) {
        Location location = holo.getLocation().clone();
        location.setY((location.getY() - 2.5)); // Only needed for amrour stands

        for (String line : holo.getText()) {
            ArmorStand armourStand = location.getWorld().spawn(location, ArmorStand.class);
            armourStand.setVisible(false);
            armourStand.setGravity(false);
//            armourStand.setInvulnerable(true);
            armourStand.setMetadata("hologram", new FixedMetadataValue(Bukkit.getPluginManager().getPlugin("DeathHolos"), holo.getKeyID()));
            armourStand.setCustomNameVisible(true);
            armourStand.setCustomName(Utils.addColour(line));
            location.subtract(0, 0.25, 0);
        }
    }

    @Override
    public void despawn(Hologram holo) {
        Location location = holo.getLocation();
        for (Entity entity : location.getChunk().getEntities()) {

            if (entity instanceof ArmorStand && entity.hasMetadata("hologram")) {
                List<MetadataValue> metaData = entity.getMetadata("hologram");

                for (MetadataValue metadataValue : metaData) {
                    if (!(metadataValue instanceof FixedMetadataValue)) continue;

                    FixedMetadataValue fixedMetadataValue = (FixedMetadataValue) metadataValue;
                    if (fixedMetadataValue.value().equals(holo.getKeyID())) {
                        entity.remove();
                    }
                }
            }
        }
    }
}
