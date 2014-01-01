package com.tehbeard.areablock;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AreaBlockCommands implements CommandExecutor, Listener {

    AreaBlockComponent component;
    SessionStore<AreaBlockSession> session = new SessionStore<AreaBlockSession>();

    public AreaBlockCommands(AreaBlockComponent comp) {
        component = comp;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        if (!sender.hasPermission("escapeplug.areablock")) {
            return true;
        }
        if (sender instanceof Player == false) {
            sender.sendMessage(ChatColor.RED + "CANNOT USE FROM CONSOLE");
            return true;
        }
        if (args.length==0) {
            return false;
        }

        Player p = (Player)sender;
        if (!session.hasSession(p.getName())) {
            session.putSession(p.getName(),new AreaBlockSession());
        }
        AreaBlockSession plysession = session.getSession(p.getName());

        ArgumentPack pack = new ArgumentPack(new String[0], new String[] {"open","close","max","min"}, args);

        String subcmd = args[0];
        String areaName = "";
        if (args.length > 1) {
            areaName = args[1];
        }
        Material close = Material.getMaterial(pack.getOption("close") == null ? 7 : Integer.parseInt(pack.getOption("close")));
        Material open = Material.getMaterial(pack.getOption("open") == null ? 0 : Integer.parseInt(pack.getOption("open")));
        int threshold = pack.getOption("min") == null ? 1 : Integer.parseInt(pack.getOption("min"));
        int max = pack.getOption("max") == null ? -1 : Integer.parseInt(pack.getOption("max"));

        if (subcmd.equalsIgnoreCase("inside")) {
            sender.sendMessage(ChatColor.GOLD + "Areas Found:");
            for (CuboidEntry<GatedArea> entry : component.areas.getEntries(p)) {
                sender.sendMessage(ChatColor.GOLD + entry.getEntry().getName());
            }
            return true;
        }

        if (subcmd.equalsIgnoreCase("tool")) {
            plysession.setToolActive(!plysession.isToolActive());
            sender.sendMessage(ChatColor.GREEN + "tool is now " + (plysession.isToolActive()? ChatColor.GOLD + "active" : ChatColor.RED + "inactive"));
            return true;
        }

        //create name [-min Threshold]
        if (subcmd.equalsIgnoreCase("create")) {

            if (component.areaMap.containsKey(areaName)) {
                sender.sendMessage(ChatColor.RED + "Area already exists");
                return true;
            }

            GatedArea a = new GatedArea(threshold);
            component.areaMap.put(areaName, a);
            sender.sendMessage(ChatColor.GREEN + "Area created");
            return true;
        }

        if (!component.areaMap.containsKey(areaName)) {
            sender.sendMessage(ChatColor.RED + "No area found with that name");
            return true;
        }

        //addcheck name [-max threshold]
        if (subcmd.equalsIgnoreCase("addcheck")) {
            //get and check cuboid
            Cuboid cuboid = plysession.makeCuboid(p.getWorld().getName());
            if (cuboid == null) {
                sender.sendMessage(ChatColor.RED + "Cuboid not defined");
                return true;
            }

            cuboid.maxPly = max;
            //add gate

            component.areaMap.get(areaName).getDetectAreas().add(cuboid);
            component.areas.addEntry(cuboid, component.areaMap.get(areaName));
            sender.sendMessage("Detection Area Added");
            return true;
        }

        
        if (subcmd.equalsIgnoreCase("remove")) {
            GatedArea arena = component.areaMap.get(areaName);
            if (arena == null) {
                sender.sendMessage(ChatColor.RED+"Area not found");
                return true;
            }
            component.areaMap.remove(areaName);
            component.areas.remove(arena);
            component.config.set(areaName, null);
        }
        //info name
        if (subcmd.equalsIgnoreCase("info")) {
            //information
            GatedArea arena = component.areaMap.get(areaName);
            if (arena == null) {
                sender.sendMessage(ChatColor.RED+"Area not found");
                return true;
            }
            sender.sendMessage(ChatColor.GOLD + "threshold: " +ChatColor.WHITE+ arena.threshold);
            sender.sendMessage(ChatColor.GOLD + "Gates:");
            int i = 0;
            for (Gate gate : arena.getGates()) {
                sender.sendMessage("" + i +") " + gate.toString() + " [Size: " + gate.getSize() + "]");
                i++;
            }

            sender.sendMessage(ChatColor.GOLD + "Detection areas:");         
            i = 0;
            for (Cuboid cuboid : arena.getDetectAreas()) {
                sender.sendMessage(ChatColor.GOLD +""+ i +") " +ChatColor.WHITE + cuboid.toString());
                i++;
            }

            return true;
        }

        //areablock addgate name [-close type] [-open type]
        if (subcmd.equalsIgnoreCase("addgate")) {
            //get and check cuboid
            Cuboid cuboid = plysession.makeCuboid(p.getWorld().getName());
            if (cuboid == null) {
                sender.sendMessage(ChatColor.RED + "Cuboid not defined");
                return true;
            }
            if (cuboid.size() > 256) {
                sender.sendMessage(ChatColor.RED + "Cuboid too large (256 max. " + cuboid.size() + " attempted)");
                return true;
            }

            //create gate
            Gate gate = new Gate();
            gate.setArea(cuboid);
            gate.setOpen(open);
            gate.setClose(close);

            //add gate
            if (!component.areaMap.containsKey(areaName)) {
                sender.sendMessage(ChatColor.RED + "No area found with that name");
                return true;
            }
            component.areaMap.get(areaName).getGates().add(gate);
            sender.sendMessage(ChatColor.GREEN + "Gate Added");
            return true;
        }

        if (subcmd.equalsIgnoreCase("removegate")) {
            GatedArea arena = component.areaMap.get(areaName);
            int id = Integer.parseInt(args[2]);
            if (id < 0 || id >=arena.getGates().size()) {
                sender.sendMessage(ChatColor.RED + "ID INVALID");
                return true;
            }
            arena.getGates().remove(id);
            sender.sendMessage("Removed");
        }

        if (subcmd.equalsIgnoreCase("removecheck")) {
            GatedArea arena = component.areaMap.get(areaName);
            int id = Integer.parseInt(args[2]);
            if (id < 0 || id >=arena.getDetectAreas().size()) {
                sender.sendMessage(ChatColor.RED + "ID INVALID");
                return true;
            }
            arena.getDetectAreas().remove(id);
            sender.sendMessage("Removed");
        }

        return true;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("escapeplug.areablock")) {
            return;
        }
        if (!session.hasSession(event.getPlayer().getName())) {
            session.putSession(event.getPlayer().getName(),new AreaBlockSession());
        }
        AreaBlockSession plysession = session.getSession(event.getPlayer().getName());
        if (!plysession.isToolActive()) {
            return;
        }
        if (event.getPlayer().getItemInHand().getType()!=Material.BOOK) {
            return;
        }

        event.setCancelled(true);
        switch (event.getAction()) {
        case LEFT_CLICK_BLOCK:
            plysession.setP1(event.getClickedBlock().getLocation().toVector());
            event.getPlayer().sendMessage("Size: " + plysession.size());
            break;

        case RIGHT_CLICK_BLOCK:
            plysession.setP2(event.getClickedBlock().getLocation().toVector());
            event.getPlayer().sendMessage("Size: " + plysession.size());
            break;
        }
    }
}
