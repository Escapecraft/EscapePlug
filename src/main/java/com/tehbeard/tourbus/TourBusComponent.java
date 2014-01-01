package com.tehbeard.tourbus;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeplug.EscapePlug;

@ComponentDescriptor(name="Tour Bus",slug="tourbus",version="1.00")
public class TourBusComponent extends AbstractComponent implements CommandExecutor {

    private Set<String> tourists = new HashSet<String>();
    private boolean open = false;

    @Override
    public boolean enable(EscapePlug plugin) {
        plugin.getCommand("tour").setExecutor(this);
        return true;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (sender instanceof Player == false) {
            return true;
        }
        //ONLY one argument
        if (args.length != 1) {
            return false;
        }
        Player p = (Player) sender;
        String cm = args[0];
        
        //add to the tour group
        if (cm.equalsIgnoreCase("join")) {
            if (!open) {
                return true;
            }
            p.sendMessage(ChatColor.GOLD + "Joined the tour group.");
            tourists.add(p.getName());
        }
        
        //remove from the tour group
        if (cm.equalsIgnoreCase("leave")) {
            p.sendMessage(ChatColor.GOLD + "Left the tour group.");
            tourists.remove(p.getName());
        }
        
        //Teleport all those in the tour group
        if (cm.equalsIgnoreCase("port")) {
            //check perm node (op default)
            if (p.hasPermission("escapeplug.tourbus.port")) {
                //loop tourists, check online
                for (String t : tourists) {
                    Player pp = Bukkit.getPlayer(t);
                    if (pp==null) {
                        continue;
                    }
                    if (!pp.isOnline()) {
                        continue;
                    }
                    pp.teleport(p);
                }
            }
        }
        
        if (cm.equalsIgnoreCase("clear")) {
            //check perm node (op default)
            if (p.hasPermission("escapeplug.tourbus.port")) {
                //loop tourists, check online
                open = false;
                for (String t : tourists) {
                    Player pp = Bukkit.getPlayer(t);
                    if (pp==null) {
                        continue;
                    }
                    if (!pp.isOnline()) {
                        continue;
                    }
                    pp.sendMessage(ChatColor.GOLD + "Tour group ended");
                }
                tourists.clear();
            }
        }
        
        if (cm.equalsIgnoreCase("open")) {
            //check perm node (op default)
            if (p.hasPermission("escapeplug.tourbus.port")) {
                //loop tourists, check online
                open = true;
            }
        }
        return true;
    }
}
