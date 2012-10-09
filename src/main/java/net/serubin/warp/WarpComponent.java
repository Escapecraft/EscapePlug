package net.serubin.warp;

import java.util.logging.Logger;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name = "Warps", slug = "warps", version = "1.1")
@BukkitCommand(command = { "warp", "setwarp", "remwarp" })
public class WarpComponent extends AbstractComponent implements CommandExecutor {
    private EscapePlug plugin;
    private FlatFile flatFile;
    private Logger log;

    boolean debug = false;
    private boolean silent = false;

    private CommandSender sender;

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        // debug
        debug = plugin.getConfig().getBoolean("plugin.warp.debug");
        printDebug("Debug is enabled...");
        plugin.getComponentManager().registerCommands(this);
        flatFile = new FlatFile(plugin, this);
        return true;
    }

    @Override
    public void disable() {
        // flatFile.pushData();
        log.info("[Warps] Saved warps file.");

    }

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        this.sender = sender;
        if (sender instanceof Player) {
            if (commandLabel.equalsIgnoreCase("warp")) {
                if (sender.hasPermission("escapeplug.warp.tele")
                        || sender.hasPermission("escapeplug.warp.edit")
                        || sender.isOp()) {
                    WarpData warp;
                    Location warpLoc;
                    silent = false;
                    if (args.length == 0) {
                        flatFile.printWarps(((Player) sender));
                        return true;
                    } else {
                        if (args[0].equalsIgnoreCase("-s")) {
                            silent = true;
                            args = stripArg(args, 0);
                            for (String str : args) {
                                printDebug(str);
                            }
                        }
                        warp = flatFile.getWarp(args[0]);
                        // checks if warp is valid
                        if (warp == null) {
                            sender.sendMessage(ChatColor.GOLD + args[0]
                                    + ChatColor.RED
                                    + " is not a existing warp!");
                            return true;
                        }

                        // yaw-pitch workaround
                        warpLoc = warp.getLoc();
                        warpLoc.setPitch(warp.getPitch());
                        warpLoc.setYaw(warp.getYaw());

                        // checks for second argument
                        if (args.length == 2) {
                            if (args[1].equalsIgnoreCase("-a")) {
                                flatFile.printWarp(((Player) sender), args[0]);
                            } else {
                                // Processes arguemtns - player
                                String[] playerStr = args[1].split(",");
                                Player[] players = new Player[playerStr.length];
                                if (!args[1].equalsIgnoreCase("-a")) {
                                    int i = 0;
                                    for (String str : playerStr) {
                                        players[i] = plugin.getServer()
                                                .getPlayer(str);
                                        if (players[i] == null) {
                                            sender.sendMessage(ChatColor.RED
                                                    + "Player "
                                                    + ChatColor.GOLD + str
                                                    + ChatColor.RED
                                                    + " could not be found.");
                                            stripArg(players, i);
                                        } else {
                                            i++;
                                        }
                                    }
                                }
                                // Sends player message and teleports for each
                                // player
                                String sucessMessage = "";
                                for (Player player : players) {
                                    try {
                                        if (!silent) {
                                            player.sendMessage(ChatColor.YELLOW
                                                    + "You have been warped to "
                                                    + ChatColor.GOLD + args[0]
                                                    + ChatColor.YELLOW + " by "
                                                    + ChatColor.GOLD
                                                    + sender.getName()
                                                    + ChatColor.YELLOW + ".");
                                        }
                                        printDebug("Warping "
                                                + player.getName() + " to "
                                                + warp.getName());
                                        player.teleport(warpLoc);
                                        sucessMessage += " " + player.getName();
                                    } catch (NullPointerException NPE) {

                                    }
                                }
                                String wasWere = "";
                                if (sucessMessage.isEmpty()) {
                                    sucessMessage = "No one";
                                } else if (players.length == 1) {
                                    wasWere = " was";
                                } else {
                                    wasWere = " were";
                                }
                                sender.sendMessage(ChatColor.GOLD
                                        + sucessMessage + ChatColor.YELLOW
                                        + wasWere + " warped");
                                return true;
                            }
                            // Standard warp
                        } else if (args.length == 1) {
                            // TODO add coords and world to message
                            sender.sendMessage(ChatColor.YELLOW
                                    + "Warping you to " + ChatColor.GOLD
                                    + args[0] + ChatColor.YELLOW + " ("
                                    + warpLoc.getWorld().getName() + ", "
                                    + warp.getLoc().getBlockX() + ", "
                                    + warp.getLoc().getBlockY() + ", "
                                    + warp.getLoc().getBlockZ() + ")");

                            ((Player) sender).teleport(warpLoc);
                            return true;
                        }
                    }

                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED
                            + "You don't have permission!");
                    return true;
                }
            } else if (commandLabel.equalsIgnoreCase("setwarp")) {
                if (sender.hasPermission("escapeplug.warp.edit")
                        || sender.isOp()) {
                    if (args.length == 0) {
                        return false;
                    }
                    if (flatFile.addWarp(args[0],
                            ((Player) sender).getLocation(), ((Player) sender))) {
                        sender.sendMessage(ChatColor.GOLD + args[0]
                                + ChatColor.YELLOW + " added to warps!");
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED
                            + "You don't have permission!");
                    return true;
                }

            } else if (commandLabel.equalsIgnoreCase("remwarp")) {
                if (sender.hasPermission("escapeplug.warp.edit")
                        || sender.isOp()) {
                    if (args.length == 0) {
                        return false;
                    }
                    if (flatFile.rmWarp(args[0])) {
                        sender.sendMessage(ChatColor.GOLD + args[0]
                                + ChatColor.YELLOW + " removed from warps!");
                    }
                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED
                            + "You don't have permission!");
                    return true;
                }
            }

        } else {
            if (commandLabel.equalsIgnoreCase("warp") && args.length == 0) {
                flatFile.printWarps(sender);
            } else if (commandLabel.equalsIgnoreCase("warp")
                    && args.length == 1) {
                flatFile.printWarp(sender, args[0]);
            } else {
                sender.sendMessage("This command cannot be used via console.");
            }
            return true;
        }
        return false;
    }

    /**
     * Strip arg from Player array
     * 
     * @param args
     *            Player[] array
     * @param index
     *            int what to strip
     * @return new array
     */
    public static Player[] stripArg(Player[] args, int index) {
        index++;
        Player[] argNew = new Player[args.length - 1];
        for (int i = index; i < args.length; i++) {
            argNew[i - 1] = args[i];
        }
        return argNew;
    }

    public static String[] stripArg(String[] args, int index) {
        index++;
        String[] argNew = new String[args.length - 1];
        for (int i = index; i < args.length; i++) {
            argNew[i - 1] = args[i];
        }
        return argNew;
    }

    /**
     * Print if debug is true
     * 
     * @param line
     *            String
     */
    public void printDebug(String line) {
        if (debug) {
            log.info("[Warps][Debug]" + line);
        }
    }

    /**
     * Print to last sender
     * 
     * @param line
     *            String
     */
    public void printPlayer(String line) {
        sender.sendMessage(line);
    }
}
