package me.soapiee.deathholos.commands.adminCmds;

import lombok.Getter;
import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ReloadSub extends AbstractAdminSub {

    @Getter private final String IDENTIFIER = "reload";

    public ReloadSub(DeathHolos main) {
        super(main, "deathholos.admin.reload", 1, 1);
    }

    // /adh reload
    @Override
    public void execute(CommandSender sender, String label, String[] args) {
        if (!checkRequirements(sender, args, label)) return;

        reload(sender);
    }

    private void reload(CommandSender sender) {
        String progressMessage = (sender instanceof Player ? messageManager.get(Message.RELOADINPROGRESS) : messageManager.getWithoutPrefix(Message.RELOADINPROGRESS));
        sendMessage(sender, progressMessage);

        Message reloadOutcome = Message.RELOADSUCCESS;
        boolean errors = false;

        main.getConfigManager().reload();
        if (!messageManager.reload(sender)) errors = true;
        hologramManager.reload(sender);

        if (errors) reloadOutcome = Message.RELOADERROR;

        if (sender instanceof Player) Utils.consoleMsg(ChatColor.GOLD + sender.getName() + " " + messageManager.getWithoutPrefix(reloadOutcome));

        String outcomeMessage = (sender instanceof Player ? messageManager.get(reloadOutcome) : messageManager.getWithoutPrefix(reloadOutcome));
        sendMessage(sender, outcomeMessage);
    }

    @Override
    public List<String> getTabCompletions(String[] args) {
        return new ArrayList<>();
    }
}
