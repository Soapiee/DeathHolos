package me.soapiee.deathholos.logic;

import me.soapiee.deathholos.DeathHolos;
import me.soapiee.deathholos.managers.MessageManager;
import me.soapiee.deathholos.utils.CustomLogger;
import me.soapiee.deathholos.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.ArrayList;
import java.util.List;

public class GroupFactory {

    private final CustomLogger customLogger;
    private final MessageManager messageManager;

    public GroupFactory(DeathHolos main) {
        messageManager = main.getMessageManager();
        customLogger = main.getCustomLogger();
    }

    public HologramGroup create(CommandSender sender, String name, String inputPriority, String inputPermission, List<String> inputList) {
        int priority = validatePriority(sender, inputPriority, name);
        if (priority <= -1) return null;

        String permission = validatePermission(inputPermission, name);
        if (permission == null) {
            customLogger.logToPlayer(sender, null, messageManager.getWithPlaceholder(Message.GROUPPERMISSIONERROR, name, (sender instanceof ConsoleCommandSender)));
            return null;
        }

        List<String> text = validateText(sender, inputList, name);
        if (text == null) return null;

        return new HologramGroup(name, priority, permission, text);
    }

    private int validatePriority(CommandSender sender, String priorityInput, String groupName) {
        int priority = -1;

        try {
            priority = Integer.parseInt(priorityInput);
        } catch (NumberFormatException ignored) {
        }

        if (priority <= -1)
            customLogger.logToPlayer(sender, null, messageManager.getWithPlaceholder(Message.GROUPPRIORITYERROR, groupName, (sender instanceof ConsoleCommandSender)));

        return priority;
    }

    private String validatePermission(String permissionInput, String groupName) {
        if (permissionInput == null) return "deathholos.group." + groupName;

        //Check input contains only letters
        if (permissionInput.matches(".+[0-9].+")) return null;

        //Check input starts with "deathholos."
        String[] start = permissionInput.split("\\.");
        if (start.length <= 1) return null;
        if (!start[0].equalsIgnoreCase("deathholos")) return null;

        return permissionInput;
    }

    private List<String> validateText(CommandSender sender, List<String> list, String groupName) {
        if (list != null && list.isEmpty()) list = null;
        if (list == null)
            customLogger.logToPlayer(sender, null, messageManager.getWithPlaceholder(Message.GROUPDESIGNERROR, groupName, (sender instanceof ConsoleCommandSender)));

        if (list != null) list = checkNewLineFunction(list);

        return list;
    }

    private List<String> checkNewLineFunction(List<String> list) {
        boolean functionExists = false;

        for (String line : list) {
            if (line.contains("\n")) {
                functionExists = true;
                break;
            }
        }

        if (!functionExists) return list;

        List<String> newList = new ArrayList<>();
        for (String line : list) {
            if (line.contains("\n")) {
                String[] additionalLines = line.split("\n");
                for (String string : additionalLines) {
                    newList.add(string.trim());
                }
                continue;
            }

            newList.add(line);
        }

        return newList;
    }
}
