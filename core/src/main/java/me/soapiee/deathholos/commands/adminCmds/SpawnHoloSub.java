package me.soapiee.deathholos.commands.adminCmds;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.logic.Hologram;
import me.soapiee.deathholos.logic.HologramGroup;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpawnHoloSub extends AbstractAdminSub {

    @Getter private final String IDENTIFIER = "spawn";

    public SpawnHoloSub(DeathHolos main) {
        super(main, null, 2, 2);
    }

    // /adh reload
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            sendMessage(sender, Utils.addColour(messageManager.get(Message.MUSTBEPLAYERERROR)));
            return;
        }

        Player player = (Player) sender;

        if (!checkRequirements(player, args, label)) return;

        HologramGroup group = validateGroup(sender, args[1]);
        if (group == null) return;

        spawn(player, group);
    }

    private HologramGroup validateGroup(CommandSender sender, String groupName) {
        HologramGroup group = hologramManager.getGroup(groupName);

        if (group == null) sendMessage(sender, messageManager.get(Message.INVALIDHOLOGROUP));

        return group;
    }

    private void spawn(Player player, HologramGroup group) {
        double heightOffset = configManager.getHeightOffset();
        String keyID = createKeyID(player.getUniqueId());

        if (keyID == null) {
            sendMessage(player, messageManager.get(Message.HOLOSPAWNEDMAX));
            return;
        }

        Location newLocation = player.getLocation().clone().add(0, heightOffset, 0);
        Hologram holo = new Hologram(keyID, newLocation, getText(group.getText(), player));
        hologramHandler.spawn(holo);
        hologramManager.registerHolo(holo);

        sendMessage(player, messageManager.get(Message.HOLOSPAWNED));
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

    private List<String> getText(List<String> defaultText, Player sender) {
        ArrayList<String> updatedText = new ArrayList<>();

        for (String line : defaultText) {
            String newLine = line;

            if (line.contains("%cause%"))
                newLine = newLine.replace("%cause%", messageManager.get(Message.CMDHOLOCAUSE));
            if (line.contains("%killer%"))
                newLine = newLine.replace("%killer%", messageManager.get(Message.CMDHOLOKILLER));
            if (line.contains("%player%")) newLine = newLine.replace("%player%", sender.getName());
            if (main.isPlaceholderAPIHooked())
                if (newLine.contains("%")) newLine = PlaceholderAPI.setPlaceholders(sender, newLine);

            updatedText.add(newLine);
        }

        return updatedText;
    }

    @Override
    public List<String> getTabCompletions(String[] args) {
        return new ArrayList<>();
    }
}
