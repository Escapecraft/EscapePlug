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

@ComponentDescriptor(name = "Warps", slug = "warps", version = "0.1")
@BukkitCommand(command = { "warp", "setwarp", "remwarp" })
public class WarpComponent extends AbstractComponent implements CommandExecutor {
    private EscapePlug plugin;
    private FlatFile flatFile;
    private Logger log;

    boolean debug = true;
    private boolean silent = false;

    private CommandSender sender;

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
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
                            args = stripArg(args, 1);
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
                        // TODO clean up args
                        if (args.length == 2) {
                            String[] playerStr = args[1].split(",");
                            Player[] players = null;
                            if (!args[1].equalsIgnoreCase("-a")) {
                                int i = 0;
                                for (String str : playerStr) {
                                    try {
                                        players[i] = plugin.getServer()
                                                .getPlayer(str);
                                        i++;
                                    } catch (NullPointerException npe) {
                                        sender.sendMessage("Player "
                                                + players[i]
                                                + "could not be found.");
                                        stripArg(players, i);
                                        i--;
                                    }
                                }
                            }

                            if (args[1].equalsIgnoreCase("-a")) {
                                flatFile.printWarp(((Player) sender), args[0]);
                            } else {
                                for (Player player : players) {
                                    if (!silent) {
                                        player.sendMessage(ChatColor.YELLOW
                                                + "You have been warped to "
                                                + ChatColor.GOLD + args[0]
                                                + ChatColor.YELLOW + " by "
                                                + ChatColor.GOLD
                                                + sender.getName()
                                                + ChatColor.YELLOW + ".");
                                    }
                                    player.teleport(warpLoc);
                                    return true;
                                }
                            }
                            // Standard warp
                        } else if (args.length == 1) {
                            // TODO add coords and world to message
                            sender.sendMessage(ChatColor.YELLOW
                                    + "Warping you to " + ChatColor.GOLD
                                    + args[0] + ChatColor.YELLOW + "("
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
                                + ChatColor.YELLOW + "added to warps!");
                    }
                    return true;
                } else {

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

                }
            }
            return false;
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
        Player[] argNew = new Player[args.length - 1];
        for (int i = index; i < args.length; i++) {
            argNew[i - 1] = args[i];
        }
        return argNew;
    }

    public static String[] stripArg(String[] args, int index) {
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