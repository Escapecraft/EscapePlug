package en.tehbeard.areablock;

import me.tehbeard.utils.cuboid.Cuboid;
import me.tehbeard.utils.session.SessionStore;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class AreaBlockCommands implements CommandExecutor, Listener{

    AreaBlockComponent component;
    SessionStore<AreaBlockSession> session = new SessionStore<AreaBlockSession>();

    public AreaBlockCommands(AreaBlockComponent comp){
        component = comp;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String lbl,
            String[] args) {
        // TODO Auto-generated method stub
        if(!sender.hasPermission("escapeplug.areablock")){return true;}
        Player p = (Player)sender;
        if(!session.hasSession(p.getName())){
            session.putSession(p.getName(),new AreaBlockSession());
        }
        AreaBlockSession plysession = session.getSession(p.getName());

        String subcmd = args[0];
        if(args.length < 1){sender.sendMessage("invalid arg length");return false;}

        if(subcmd.equalsIgnoreCase("tool")){
            plysession.setToolActive(!plysession.isToolActive());
            sender.sendMessage("tool is now " + (plysession.isToolActive()? "active" : "inactive") );
            return true;
        }

        if(args.length < 2){sender.sendMessage("invalid arg length");return false;}
        String areaName = args[1];

        //check for arena
        if(!component.areaMap.containsKey(areaName)){
            sender.sendMessage(ChatColor.RED + "No area found with that name");return true;
        }

        if(subcmd.equalsIgnoreCase("addcheck")){


            //get and check cuboid
            Cuboid cuboid = plysession.makeCuboid(p.getWorld().getName());
            if(cuboid == null){
                sender.sendMessage(ChatColor.RED + "Cuboid not defined");return true;
            } 
            //add gate

            component.areaMap.get(areaName).getDetectAreas().add(cuboid);
            component.areas.addEntry(cuboid, component.areaMap.get(areaName));

            return true;
        }

        if(subcmd.equalsIgnoreCase("info")){
            //information
            GatedArea arena = component.areaMap.get(areaName);
            sender.sendMessage("threshold: " + arena.threshold);
            sender.sendMessage("Gates");
            int i = 0;
            for(Gate gate : arena.getGates()){
                sender.sendMessage("" + i +") " + gate.toString());
                i++;
            }
            
            sender.sendMessage("Detection areas");         
            i = 0;
            for(Cuboid cuboid : arena.getDetectAreas()){
                sender.sendMessage("" + i +") " + cuboid.toString());
                i++;
            }

            return true;
        }


        if(args.length < 3){sender.sendMessage("invalid arg length");return false;}



        //areablock create name threshold (3)
        if(subcmd.equalsIgnoreCase("create")){

            int threshold = Integer.parseInt(args[2]);
            if(threshold < 1){threshold = 1;}
            if(component.areaMap.containsKey(areaName)){
                sender.sendMessage(ChatColor.RED + "Area already exists");return true;
            }
            component.areaMap.put(areaName, new GatedArea(threshold));
            return true;
        }

        //areablock addgate name close [open] (4)
        if(subcmd.equalsIgnoreCase("addgate")){
            //get and check cuboid
            Cuboid cuboid = plysession.makeCuboid(p.getWorld().getName());
            if(cuboid == null){
                sender.sendMessage(ChatColor.RED + "Cuboid not defined");return true;
            }
            if(cuboid.size() > 256){
                sender.sendMessage(ChatColor.RED + "Cuboid too large (256 max. " + cuboid.size() + " attempted)");return true;
            }

            //set materials
            Material close = Material.getMaterial(args[2]);
            Material open  = Material.AIR;
            if(args.length == 4){
                open = Material.getMaterial(args[3]);
            }

            //create gate
            Gate gate = new Gate();
            gate.setArea(cuboid);
            gate.setOpen(open);
            gate.setClose(close);

            //add gate
            if(!component.areaMap.containsKey(areaName)){
                sender.sendMessage(ChatColor.RED + "No area found with that name");return true;
            }
            component.areaMap.get(areaName).getGates().add(gate);
            return true;
        }
        
        if(subcmd.equalsIgnoreCase("removegate")){
            GatedArea arena = component.areaMap.get(areaName);
            int id = Integer.parseInt(args[2]);
            if(id < 0 || id >=arena.getGates().size() ){sender.sendMessage(ChatColor.RED + "ID INVALID");return true;}
            arena.getGates().remove(id);
        }

        if(subcmd.equalsIgnoreCase("removecheck")){

                GatedArea arena = component.areaMap.get(areaName);
                int id = Integer.parseInt(args[2]);
                if(id < 0 || id >=arena.getDetectAreas().size() ){sender.sendMessage(ChatColor.RED + "ID INVALID");return true;}
                arena.getDetectAreas().remove(id);

        }





        return true;
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event){
        if(!event.getPlayer().hasPermission("escapeplug.areablock")){return;}
        if(!session.hasSession(event.getPlayer().getName())){
            session.putSession(event.getPlayer().getName(),new AreaBlockSession());
        }
        AreaBlockSession plysession = session.getSession(event.getPlayer().getName());
        if(!plysession.isToolActive()){return;}
        if(event.getPlayer().getItemInHand().getType()!=Material.BOOK){return;}

        event.setCancelled(true);
        switch(event.getAction()){
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
