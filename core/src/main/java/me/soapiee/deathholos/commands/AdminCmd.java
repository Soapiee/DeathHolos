package me.soapiee.deathholos.commands;

import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.commands.adminCmds.ReloadSub;
import me.soapiee.deathholos.managers.HologramManager;
import me.soapiee.deathholos.managers.MessageManager;
import me.soapiee.deathholos.utils.Message;
import me.soapiee.deathholos.utils.Utils;
import org.bukkit.block.CommandBlock;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AdminCmd implements CommandExecutor, TabCompleter {

    private final MessageManager messageManager;
    private final HologramManager hologramManager;

    private final String PERMISSION = "deathholos.admin";
    private final Map<String, SubCmd> subCommands = new HashMap<>();

    public AdminCmd(DeathHolos main) {
        messageManager = main.getMessageManager();
        hologramManager = main.getHoloManager();

        register(new ReloadSub(main));
    }

    private void register(SubCmd cmd) {
        subCommands.put(cmd.getIDENTIFIER(), cmd);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof CommandBlock) return true;
        if (!hasPermission(sender)) return true;

        if (args.length == 0) {
            sendMessage(sender, messageManager.getWithPlaceholder(Message.ADMINHELP, "adh", (sender instanceof ConsoleCommandSender)));
            return true;
        }

        SubCmd cmd = subCommands.get(args[0]);
        if (cmd == null) {
            sendMessage(sender, messageManager.getWithPlaceholder(Message.ADMINHELP, label, (sender instanceof ConsoleCommandSender)));
            return true;
        }

        cmd.execute(sender, label, args);
        return true;
    }

    private boolean hasPermission(CommandSender sender) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        if (!player.hasPermission(PERMISSION))
            sendMessage(player, messageManager.get(Message.NOPERMISSION));

        return player.hasPermission(PERMISSION);
    }

    private void sendMessage(CommandSender sender, String message) {
        if (message == null) return;

        if (sender instanceof Player) sender.sendMessage(Utils.addColour(message));
        else Utils.consoleMsg(message);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        final List<String> results = new ArrayList<>();

        switch (args.length) {
            case 1:
                results.add("help");
//                results.add("spawn");

                if (sender instanceof Player && sender.hasPermission("deathholos.admin")) {
                    results.add("reload");
                }
                break;

            case 2:
                if (args[0].equalsIgnoreCase("reload")) break;

//                if (args[0].equalsIgnoreCase("spawn")) {
//                    hologramManager.getGroups().forEach(group -> results.add(group.getName()));
//                    break;
//                }

                break;

        }
        return results.stream()
                .filter(completion -> completion.toLowerCase().startsWith(args[args.length - 1].toLowerCase()))
                .collect(Collectors.toList());
    }
}
