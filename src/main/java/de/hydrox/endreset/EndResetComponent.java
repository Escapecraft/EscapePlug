package de.hydrox.endreset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.component.Log;
import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EnderCrystal;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.util.Vector;

@ComponentDescriptor(name = "End Reset", slug = "endreset", version = "1.00")
@BukkitCommand(command = "endreset")
public class EndResetComponent extends AbstractComponent implements CommandExecutor, Listener {
    
    private List<Vector> obsidianBlocks;
    private List<Vector> enderCrystals;

    private String endWorldName = null;

    private YamlConfiguration config = new YamlConfiguration();
    private File file;

    private Log log;

    @SuppressWarnings("unchecked")
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
    this.log = log;
        try {
            file = new File(plugin.getDataFolder(),"endReset.yml");
            file.createNewFile();
            config.load(file);

            obsidianBlocks = (List<Vector>) config.getList("blocks");
            if (obsidianBlocks == null) {
                obsidianBlocks = new ArrayList<Vector>();
            }
            log.info("loaded " + obsidianBlocks.size() + " Blocks");
            enderCrystals = (List<Vector>) config.getList("crystals");
            if (enderCrystals == null) {
                enderCrystals = new ArrayList<Vector>();
            }
            log.info("loaded " + enderCrystals.size() + " Crystals");
            endWorldName = plugin.getConfig().getString("plugin.endreset.world", "survival_the_end");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getComponentManager().registerCommands(this);
        return true;
    }

    @Override
    public void disable() {
        config.set("blocks", obsidianBlocks);
        log.info("saved " + obsidianBlocks.size() + " Blocks");
        config.set("crystals", enderCrystals);
        log.info("saved " + enderCrystals.size() + " Crystals");
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                if (!sender.hasPermission("escapeplug.endreset.reset")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to reset The End.");
                    return true;
                }
                resetBlocks(Bukkit.getWorld(endWorldName));
                sender.sendMessage(ChatColor.GREEN
                        + "The End has been reset");
                return true;
            }
            if (args[0].equalsIgnoreCase("clear")) {
            if (!sender.hasPermission("escapeplug.endreset.clear")) {
                sender.sendMessage(ChatColor.RED
                        + "You don't have permission to clear End Reset.");
                return true;
            }
            obsidianBlocks.clear();
            enderCrystals.clear();
            sender.sendMessage(ChatColor.GREEN
                + "End Reset cache has been cleared");
            return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityExplodeEvent(EntityExplodeEvent event) {
        if (event.getLocation().getWorld().getName().equals(endWorldName) && event.getEntityType() == EntityType.ENDER_CRYSTAL) {
            Vector loc = event.getEntity().getLocation().toVector();
            enderCrystals.add(loc);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getWorld().getName().equals(endWorldName) && event.getEntityType() == EntityType.ENDER_DRAGON) {
            World world = event.getEntity().getWorld();
            if (event.getEntity().getKiller() != null) {
                Bukkit.broadcastMessage(ChatColor.GOLD + event.getEntity().getKiller().getName() + " killed an Enderdragon");
            }
        
            resetBlocks(world);
            world.spawnEntity(new Location(world, 0, 80, 0), EntityType.ENDER_DRAGON);
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreakEvent(BlockBreakEvent event) {
        if (event.isCancelled() || !event.getBlock().getWorld().getName().equals(endWorldName)) {
            return;
        }
        Block block = event.getBlock();
        if (block.getType() == Material.OBSIDIAN) {
            obsidianBlocks.add(block.getLocation().toVector());
        }
    }

    private void resetBlocks(World world) {
        for (Vector loc : obsidianBlocks) {
            Block block = world.getBlockAt(loc.toLocation(world));
            block.setType(Material.OBSIDIAN);
        }
        obsidianBlocks.clear();

        for (Vector crystal : enderCrystals) {
            crystal.setY(crystal.getY()-1);
            world.spawn(crystal.toLocation(world), EnderCrystal.class);
        }
        enderCrystals.clear();
    }
}
