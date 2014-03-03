package de.hydrox.endreset;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
//import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
//import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
//import org.bukkit.event.entity.EntityExplodeEvent;
//import org.bukkit.util.Vector;

@ComponentDescriptor(name = "End Reset", slug = "endreset", version = "1.1")
@BukkitCommand(command = "endreset")
public class EndResetComponent extends AbstractComponent implements CommandExecutor, Listener {
    
    private List<Tower> towers;

    private String endWorldName = null;

    private YamlConfiguration config = new YamlConfiguration();
    private File file;

    @SuppressWarnings("unchecked")
    @Override
    public boolean enable(EscapePlug plugin) {
        try {
            file = new File(plugin.getDataFolder(),"endReset.yml");
            file.createNewFile();
            ConfigurationSerialization.registerClass(Tower.class);
            config.load(file);

            towers = (List<Tower>) config.getList("towers");
            if (towers == null) {
                towers = new ArrayList<Tower>();
            }
            log.info("loaded " + towers.size() + " Towers");

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
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("reset")) {
                if (!sender.hasPermission("escapeplug.endreset.reset")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to reset The End.");
                    return true;
                }
                resetTowers(Bukkit.getWorld(endWorldName));
                sender.sendMessage(ChatColor.GREEN
                        + "The End has been reset");
                return true;
            }
            if (args[0].equalsIgnoreCase("spawn")) {
                if (!sender.hasPermission("escapeplug.endreset.spawn")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to spawn an EnderDragon.");
                    return true;
                }
                spawnDragon(Bukkit.getWorld(endWorldName));
                sender.sendMessage(ChatColor.GREEN
                        + "EnderDragon has been spawned");
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getWorld().getName().equals(endWorldName) && event.getEntityType() == EntityType.ENDER_DRAGON) {
            World world = event.getEntity().getWorld();
            if (event.getEntity().getKiller() != null) {
                Bukkit.broadcastMessage(ChatColor.GOLD + event.getEntity().getKiller().getName() + " killed an Enderdragon");
            }
        
            resetTowers(world);
            spawnDragon(Bukkit.getWorld(endWorldName));
        }
    }

    private void spawnDragon(World world) {
        world.spawnEntity(new Location(world, 0, 80, 0), EntityType.ENDER_DRAGON);
    }
    
    private void resetTowers(World world) {
        for (Tower tower : towers) {
            tower.restore(world);
        }
    }
}
