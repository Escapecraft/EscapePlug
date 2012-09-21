package com.tehbeard.tourbus;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

@ComponentDescriptor(name="Tour Bus",slug="tourbus",version="1.00")
public class TourBusComponent extends AbstractComponent implements CommandExecutor {

    private Set<String> tourists = new HashSet<String>();
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        plugin.getCommand("tour").setExecutor(this);
        return true;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl,
            String[] args) {
        if(sender instanceof Player == false){return true;}
        
        if(args.length!=1){return false;}
        Player p = (Player)sender;
        String cm = args[0];
        if(cm.equalsIgnoreCase("join")){
            p.sendMessage("Joined the tour group.");
            tourists.add(p.getName());
        }
        
        if(cm.equalsIgnoreCase("leave")){
            p.sendMessage("Left the tour group.");
            tourists.remove(p.getName());
        }
        
        if(cm.equalsIgnoreCase("port")){
            if(p.hasPermission("escapeplug.tourbus.port")){
                for(String t : tourists){
                    Player pp = Bukkit.getPlayer(t);
                    if(p==null){continue;}
                    pp.teleport(p);
                }
            }
        }
        return true;
        
        
    }

}
