package de.hydrox.endreset;

import java.util.ArrayList;
import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.CreatureType;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name = "end reset", slug = "endreset", version = "1.00")
@BukkitCommand(command = "endreset")
public class EndResetComponent extends AbstractComponent implements
	CommandExecutor, Listener {
    
    private List<Location> obsidianBlocks = new ArrayList<Location>();
    private List<Location> enderCrystals = new ArrayList<Location>();

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
	Bukkit.getPluginManager().registerEvents(this, plugin);
	return true;
    }

    @Override
    public void disable() {
	// TODO Auto-generated method stub

    }

    public boolean onCommand(CommandSender arg0, Command arg1, String arg2,
	    String[] arg3) {
	// TODO Auto-generated method stub
	return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
	if (event.getEntityType() == EntityType.ENDER_CRYSTAL) {
	    Location loc = event.getEntity().getLocation();
	    enderCrystals.add(loc);
	    System.out.println("Crystal destroyed at " + loc);
	}
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeathEvent(EntityDeathEvent event) {
	if (event.getEntityType() == EntityType.ENDER_DRAGON) {
	    World world = event.getEntity().getWorld();
	    resetBlocks(world);
	    world.spawnCreature(new Location(world, 0, 80, 0), EntityType.ENDER_DRAGON);
	}
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreakEvent(BlockBreakEvent event) {
	if (event.isCancelled()) {
	    return;
	}
	Block block = event.getBlock();
	if (block.getType() == Material.OBSIDIAN) {
	    System.out.println("Obsidian destroyed at " + block.getLocation());
	    obsidianBlocks.add(block.getLocation());
	}
    }

    private void resetBlocks(World world) {
	for (Location loc : obsidianBlocks) {
	    System.out.println("Trying to place Obsidian at " + loc);
	    Block block = world.getBlockAt(loc);
	    block.setType(Material.OBSIDIAN);
	}
	obsidianBlocks.clear();

	for (Location crystal : enderCrystals) {
	    
	    System.out.println("Trying to place Crystal at " + crystal);
	    crystal.setY(crystal.getY()-1);
	    world.spawn(crystal, EnderCrystal.class);
	}
	enderCrystals.clear();
    }

}
