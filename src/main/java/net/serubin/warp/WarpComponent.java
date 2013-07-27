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

@ComponentDescriptor(name = "Warps", slug = "warps", version = "1.2")
@BukkitCommand(command = { "warp", "setwarp", "remwarp", "warplist" })
public class WarpComponent extends AbstractComponent implements CommandExecutor {
    private EscapePlug plugin;
    private FlatFile flatFile;
    private Logger log;

    boolean debug = false;

    private CommandSender sender;

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        // debug
        debug = plugin.getConfig().getBoolean("plugin.warp.debug");
        if (debug) {
            printDebug("Debug is enabled...");
        }
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
        boolean isPlayer = (sender instanceof Player) ? true : false;

            if (commandLabel.equalsIgnoreCase("warp")) {
                if (sender.hasPermission("escapeplug.warp.tele")) {
                    WarpData warp;
                    Location warpLoc;
                    Player targetPlayer;
                    
                    if (args.length == 0) return false;
                    
                        warp = flatFile.getWarp(args[0]);
                        // checks if warp is valid

                        // yaw-pitch workaround
                        warpLoc = warp.getLoc();
                        warpLoc.setPitch(warp.getPitch());
                        warpLoc.setYaw(warp.getYaw());

                        // checks for second argument
                        if (args.length >= 2) {
                            if (args[0].equalsIgnoreCase("-i") && args.length == 2) {
                                if (flatFile.getWarp(args[1]) == null) {
                                    sender.sendMessage(ChatColor.GOLD + args[0]
                                    + ChatColor.RED
                                    + " is not an existing warp!");
                                } else {
                                flatFile.printWarp(sender, args[1]);
                                }
                            } else {
                                // Processes arguments - player(s)
                                    if (flatFile.getWarp(args[args.length - 1]) == null) {
                                        sender.sendMessage(ChatColor.GOLD + args[0]
                                            + ChatColor.RED + " is not an existing warp!");
                                        return true;
                                    }
                                    
                                    Player[] players;
                                    
                                    if (args.length == 2 && args[0] == "*") {
                                        players = plugin.getServer().getOnlinePlayers();
                                    } else {
                                        String[] playerStr = new String[args.length - 1];
                                        for (int i = 0; i < (args.length - 1); i++) {
                                            playerStr[i] = args[i]; 
                                        }
                                        
                                        players = new Player[playerStr.length];
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
                                        printDebug("Warping "
                                                + player.getName() + " to "
                                                + warp.getName());
                                        player.teleport(warpLoc);
                                        sucessMessage += " " + player.getName();
                                    } catch (NullPointerException NPE) {
                                        NPE.printStackTrace();
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
                            if (!isPlayer) {
                                sender.sendMessage("You cannot warp from the console!");
                                return true;
                            }
                            if (flatFile.getWarp(args[args.length - 1]) == null) {
                                        sender.sendMessage(ChatColor.GOLD + args[0]
                                            + ChatColor.RED + " is not an existing warp!");
                                        return true;
                            }
                            
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

                    return true;
                } else {
                    sender.sendMessage(ChatColor.RED
                            + "You don't have permission!");
                    return true;
                }
            } else if (commandLabel.equalsIgnoreCase("setwarp")) {
                if (sender.hasPermission("escapeplug.warp.edit")) {
                    if (args.length == 0) {
                        return false;
                    }
                    if (!isPlayer) {
                                sender.sendMessage("You cannot  set warps from the console!");
                                return true;
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
                if (sender.hasPermission("escapeplug.warp.edit")) {
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
            } else if (commandLabel.equalsIgnoreCase("warplist")) {
                if (sender.hasPermission("escapeplug.warp")) {
                    if (args.length == 0) {
                        flatFile.printWarps(((Player) sender));
                        return true;
                    } else {
                        return false;
                    }

                } else {
                    sender.sendMessage(ChatColor.RED
                            + "You don't have permission!");
                    return true;
                }

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
