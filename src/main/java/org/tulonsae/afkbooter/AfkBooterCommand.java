package org.tulonsae.afkbooter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * AfkBooter command processor.
 */
public class AfkBooterCommand implements CommandExecutor {

    private final String PERMISSION_CONFIG = "escapeplug.afkbooter.change";

    private AfkBooter afkBooter;
    private EscapePlug plugin;
    Logger log;

    public AfkBooterCommand(AfkBooter afkBooter) {
        this.afkBooter = afkBooter;
        this.plugin = afkBooter.getPlugin();
        this.log = plugin.getLogger();
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        // must have a subcommand
        if (args.length < 1) {
            return false;
        }

        // check perms
        if (!sender.hasPermission(PERMISSION_CONFIG)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to list or modify exempt players.");
            return true;
        }

        // subcommand
        String subCommand = args[0].toLowerCase();

        // subcommand args
        ArrayList<String> subCommandArgs = null;
        if (args.length > 1) {
            subCommandArgs = new ArrayList<String>();
            subCommandArgs.addAll(Arrays.asList(args).subList(1, args.length));
        }

        if(subCommand.equals("list"))
            return handleListExemptCommand(sender);
        else if(subCommand.equals("add"))
            return handleAddExemptPlayerCommand(sender, subCommandArgs);
        else if(subCommand.equals("remove"))
            return handleRemoveExemptPlayerCommand(sender, subCommandArgs);
        else if(subCommand.equals("debugon"))
            return handleChangeDebugModeCommand(sender, true);
        else if(subCommand.equals("debugoff"))
            return handleChangeDebugModeCommand(sender, false);

        return false;
    }

    private boolean handleChangeDebugModeCommand(CommandSender sender, boolean flag) {
        afkBooter.changeDebugMode(sender.getName(), flag);

        if (flag) {
            sender.sendMessage("Turned AfkBooter debug mode on.");
        } else {
            sender.sendMessage("Turned AfkBooter debug mode off.");
        }

        return true;
    }

    private boolean handleListExemptCommand(CommandSender sender) {

        ConcurrentSkipListSet exemptPlayers = afkBooter.getExemptList().getPlayers();

        if (exemptPlayers.size() == 0) {
            sender.sendMessage("Exempt players list is empty.");
        } else {
            sender.sendMessage("Exempt players: " + exemptPlayers.toString());
        }

        return true;
    }

    private boolean handleAddExemptPlayerCommand(CommandSender sender, ArrayList<String> args)
    {
        if (args.size() == 0) {
            sender.sendMessage("You didn't specify any player names.");
            return true;
        }

        ConcurrentSkipListSet exemptPlayers = afkBooter.getExemptList().getPlayers();

        for (String name : args) {
            name = name.trim().toLowerCase();
            if (exemptPlayers.contains(name)) {
                sender.sendMessage("Player '" + name + "' is already on the exempt list.");
                continue;
            }

            afkBooter.getExemptList().addPlayer(name, sender.getName());
            sender.sendMessage("Added player '" + name + "' to the exempt list.");
        }

        return true;
    }

    private boolean handleRemoveExemptPlayerCommand(CommandSender sender, ArrayList<String> args)
    {
        if (args.size() == 0) {
            sender.sendMessage("You didn't specify any player names.");
            return true;
        }

        ConcurrentSkipListSet exemptPlayers = afkBooter.getExemptList().getPlayers();

        for (String name : args) {
            name = name.trim().toLowerCase();
            if (!exemptPlayers.contains(name)) {
                sender.sendMessage("Player '" + name + "' is not on the exempt list.");
                continue;
            }

            afkBooter.getExemptList().removePlayer(name, sender.getName());
            sender.sendMessage("Removed player '" + name + "' from the exempt list.");
        }

        return true;
    }
}
