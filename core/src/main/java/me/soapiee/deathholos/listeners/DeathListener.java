package me.soapiee.deathholos.listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.internals.HologramHandler;
import me.soapiee.deathholos.logic.Hologram;
import me.soapiee.deathholos.logic.HologramGroup;
import me.soapiee.deathholos.managers.ConfigManager;
import me.soapiee.deathholos.managers.HologramManager;
import me.soapiee.deathholos.managers.MessageManager;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.projectiles.ProjectileSource;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DeathListener implements Listener {

    private final MessageManager messageManager;
    private final ConfigManager configManager;
    private final HologramHandler hologramHandler;
    private final HologramManager hologramManager;
    private final boolean placeholderAPIEnabled;

    public DeathListener(DeathHolos main) {
        messageManager = main.getMessageManager();
        hologramHandler = main.getInternalsManager().getHologramHandler();
        configManager = main.getConfigManager();
        hologramManager = main.getHoloManager();
        placeholderAPIEnabled = main.isPlaceholderAPIHooked();
    }

//    @EventHandler
//    public void onDeath(PlayerDeathEvent event) {
//        Player whoDied = event.getEntity();
//        if (whoDied.hasPermission("deathholos.silentdeath")) return;
//
//        String keyID = createKeyID(whoDied.getUniqueId());
//        if (keyID == null) return;
//
//        LivingEntity killer = getKiller(whoDied);
//        if (killer != null) handleKillerDeath(whoDied, killer, keyID);
//        if (killer == null && !configManager.isPlayerKillsOnly()) handleEnvironmentDeath(whoDied, keyID);
//    }

    @EventHandler
    public void onDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player)) return;

        Player whoDied = (Player) entity;
        if (whoDied.hasPermission("deathholos.silentdeath")) return;

        String keyID = createKeyID(whoDied.getUniqueId());
        if (keyID == null) return;

        LivingEntity killer = getKiller(whoDied);
        if (killer != null) handleKillerDeath(whoDied, killer, keyID);
        if (killer == null && !configManager.isPlayerKillsOnly()) handleEnvironmentDeath(whoDied, keyID);
    }

    private LivingEntity getKiller(Player deadPlayer) {
        LivingEntity killer = deadPlayer.getKiller();

        // Checks if killer is a player
        if (killer != null) return killer;

        // Checks if killer is an entity
        EntityDamageEvent damageEvent = deadPlayer.getLastDamageCause();
        if (!(damageEvent instanceof EntityDamageByEntityEvent)) return null;

        EntityDamageByEntityEvent entityDamageEvent = (EntityDamageByEntityEvent) damageEvent;
        Entity damager = entityDamageEvent.getDamager();

        // Checks if killer is a living entity
        if (damager instanceof LivingEntity) return (LivingEntity) damager;

        // Checks if killer is a projectile, and if the shooter is a living entity
        if (damager instanceof Projectile) {
            ProjectileSource shooter = ((Projectile) damager).getShooter();
            if (shooter instanceof LivingEntity) return (LivingEntity) shooter;
        }

        return null;
    }

    private String createKeyID(UUID uuid) {
        int number = 1;
        int max = configManager.getMaxHolos();

        for (int i = 1; i <= max; i++) {
            number = i;
            if (!hologramManager.exists(uuid + "_" + i)) break;
        }

        if (number == max && hologramManager.exists(uuid + "_" + number)) return null;

        return uuid + "_" + number;
    }

    private void handleKillerDeath(Player whoDied, Entity killer, String keyID) {
        HologramGroup group = getGroup(whoDied);
        Hologram holo = new Hologram(keyID, getLocation(whoDied.getLocation()), getText(group.getText(), whoDied, killer), group);
        hologramManager.registerHolo(holo);
        hologramHandler.spawn(holo);
    }

    private void handleEnvironmentDeath(Player whoDied, String keyID) {
        EntityDamageEvent.DamageCause cause = whoDied.getLastDamageCause().getCause();

        HologramGroup group = getGroup(whoDied);
        Hologram holo = new Hologram(keyID, getLocation(whoDied.getLocation()), getText(group.getText(), whoDied, cause), group);
        hologramManager.registerHolo(holo);
        hologramHandler.spawn(holo);
    }

    private HologramGroup getGroup(Player player) {
        HologramGroup group = hologramManager.getDefaultGroup();
        int lowestPriority = group.getPriority();

        for (int i = 0; i < lowestPriority; i++) {
            HologramGroup holoGroup = hologramManager.getGroup(i);
            if (holoGroup == null) continue;

            if (player.hasPermission(holoGroup.getPermission())) return holoGroup;
        }

        return group;
    }

    private Location getLocation(Location deathLoc) {
        Location location = deathLoc.clone();

        while (location.getBlock().isLiquid()) {
            location.add(0, 1, 0);

            if (location.getY() == 320) {
                location.setY(deathLoc.getY());
                break;
            }
        }

        double heightOffset = configManager.getHeightOffset();
        return location.add(0, heightOffset, 0);
    }

    private List<String> getText(List<String> defaultText, Player died, Entity killer) {
        ArrayList<String> updatedText = new ArrayList<>();

        for (String line : defaultText) {
            String newLine = line;
            String killerFormated = killer.getType() == EntityType.PLAYER ? killer.getName() : "a " + killer.getType().toString().toLowerCase();

            if (line.contains("%cause%")) newLine = newLine.replace("%cause%", "");
            if (line.contains("%killer%")) newLine = newLine.replace("%killer%", killerFormated);
            if (line.contains("%player%")) newLine = newLine.replace("%player%", died.getName());
            if (placeholderAPIEnabled)
                if (newLine.contains("%")) newLine = PlaceholderAPI.setPlaceholders(died, newLine);

            updatedText.add(newLine);
        }

        return updatedText;
    }

    private List<String> getText(List<String> defaultText, Player died, EntityDamageEvent.DamageCause cause) {
        ArrayList<String> updatedText = new ArrayList<>();

        for (String line : defaultText) {
            String newLine = line;

            if (line.contains("%cause%")) newLine = newLine.replace("%cause%", messageManager.getCause(cause));
            if (line.contains("%killer%")) newLine = newLine.replace("%killer%", "");
            if (line.contains("%player%")) newLine = newLine.replace("%player%", died.getName());
            if (placeholderAPIEnabled)
                if (newLine.contains("%")) newLine = PlaceholderAPI.setPlaceholders(died, newLine);

            updatedText.add(newLine);
        }

        return updatedText;
    }

}
