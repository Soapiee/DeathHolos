package me.soapiee.deathholos.listeners;

import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.managers.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectListener implements Listener {

    private final DeathHolos main;
    private final ConfigManager configManager;

    public ConnectListener(DeathHolos main) {
        this.main = main;
        configManager = main.getConfigManager();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Bukkit.getScheduler().runTaskLater(main, () -> {
            if (player.hasPermission("deathholos.admin.updatenotif")) updateNotif(player);
        }, 20);
    }

    private void updateNotif(Player player) {
        if (configManager.isUpdateNotif()) main.getUpdateManager().updateAlert(player);
    }
}
