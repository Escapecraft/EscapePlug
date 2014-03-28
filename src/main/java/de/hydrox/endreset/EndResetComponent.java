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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

@ComponentDescriptor(name = "End Reset", slug = "endreset", version = "1.2")
@BukkitCommand(command = "endreset")
public class EndResetComponent extends AbstractComponent implements CommandExecutor, Listener {

    private static final String CFG_DRAGON_COUNT = "plugin.escapeplug.endreset.dragon-count";
    
    private List<Tower> towers;

    private EscapePlug plugin;
    private String endWorldName = null;
    private int dragonCount = 0;
    private int respawnTimer = 0;    // minutes
    private long lastDragonDeath;        // millis
    private int convertMinToMillis = 60 * 1000;
    private boolean resetTowersOnDeath = true;

    private YamlConfiguration config = new YamlConfiguration();
    private File file;

    @SuppressWarnings("unchecked")
    @Override
    public boolean enable(EscapePlug plugin) {
        this.plugin = plugin;

        loadConfig();
        // we treat startup as if it's time to respawn dragons
        lastDragonDeath = System.currentTimeMillis() - (respawnTimer * convertMinToMillis);
        Bukkit.getPluginManager().registerEvents(this, plugin);
        plugin.getComponentManager().registerCommands(this);

        return true;
    }

    @Override
    public void disable() {
    }

    /**
     * Load the configuration for this component.
     * This loads both values from the config.yml and endReset.yml.
     */
    private void loadConfig() {
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

            endWorldName = plugin.getConfig().getString("plugin.endreset.world", "world_the_end");
            resetTowersOnDeath = plugin.getConfig().getBoolean("plugin.endreset.reset-towers-on-death", true);
            dragonCount = plugin.getConfig().getInt(CFG_DRAGON_COUNT, 1);
            log.info("dragon count is " + dragonCount);
            respawnTimer = plugin.getConfig().getInt("plugin.endreset.respawn-timer", 0);
            log.info("respawn timer is " + respawnTimer + " minutes.");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (args.length >= 1) {

            // reload config
            if (args[0].equalsIgnoreCase("reload")) {
                if (!sender.hasPermission("escapeplug.endreset.reload")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to reload the configuration for EndReset.");
                    return true;
                }
                loadConfig();
                sender.sendMessage(ChatColor.GREEN
                        + "The EndReset configuration has been reloaded.");
                return true;
            }

            // reset
            if (args[0].equalsIgnoreCase("reset")) {
                if (!sender.hasPermission("escapeplug.endreset.reset")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to reset The End.");
                    return true;
                }
                resetTowers();
                sender.sendMessage(ChatColor.GREEN
                        + "The End has been reset");
                return true;
            }

            // spawn
            if (args[0].equalsIgnoreCase("spawn")) {
                if (!sender.hasPermission("escapeplug.endreset.spawn")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to spawn an EnderDragon.");
                    return true;
                }
                int count = 1;
                if (args[1] != null) {
                    try {
                        count = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        // leave count at 1
                    }
                }
                spawnDragons(count);
                sender.sendMessage(ChatColor.GREEN
                        + String.valueOf(count) + " EnderDragon(s) has/have been spawned");
                return true;
            }

            // set dragon number
            if (args[0].equalsIgnoreCase("setnum")) {
                if (!sender.hasPermission("escapeplug.endreset.setcount")) {
                    sender.sendMessage(ChatColor.RED
                        + "You don't have permission to change the number of EnderDragons.");
                    return true;
                }
                if (args[1] == null) {
                    sender.sendMessage(ChatColor.RED
                        + "Please specify the number of dragons.");
                    return true;
                }
                int count = 0;
                try {
                    count = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(ChatColor.RED
                        + "Please specify an integer instead of " + args[1]);
                    return true;
                }
                dragonCount = count;
                sender.sendMessage(ChatColor.GREEN
                        + "Dragon count has been changed to " + count);
                plugin.getConfig().set(CFG_DRAGON_COUNT, count);
                plugin.saveConfig();
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if an EnderDragon died in The End.  If so, announce the player
     * that killed it, reset the towers, and spawn another dragon.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDeathEvent(EntityDeathEvent event) {
        if (event.getEntity().getWorld().getName().equals(endWorldName) && event.getEntityType() == EntityType.ENDER_DRAGON) {
            if (event.getEntity().getKiller() != null) {
                Bukkit.broadcastMessage(ChatColor.GOLD + event.getEntity().getKiller().getName() + " killed an Enderdragon");
            }

            lastDragonDeath = System.currentTimeMillis();
        
            // for multiple dragons and a timer,
            // this allows us to reset the towers for each dragon death
            // OR to wait till all dragons die
            if (resetTowersOnDeath) {
                // yes, this does mean that the last dragon to die will
                // cause resetting the towers twice
                resetTowers();
            }
            respawnDragons();
        }
    }

    /**
     * When the player changes world, this checks to see if they changed to
     * The End and respawns the dragons.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerChangedWorldEvent(PlayerChangedWorldEvent event) {
        if (event.getPlayer().getWorld().getName().equals(endWorldName)) {
            respawnDragons();
        }
    }

    /**
     * When the player joins, this checks to see if they were in The End
     * and respawns the dragons.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        if (event.getPlayer().getWorld().getName().equals(endWorldName)) {
            respawnDragons();
        }
    }

    /**
     * Spawns number of specified EnderDragons in The End, after
     * resetting the towers.
     */
    private void spawnDragons(int amount) {
        World world = Bukkit.getWorld(endWorldName);

        log.info("Resetting towers and spawning " + amount + " EnderDragons");
        resetTowers();
        for (int i = 0; i < amount; i++) {
            world.spawnEntity(new Location(world, 0, 80, 0), EntityType.ENDER_DRAGON);
        }
    }

    /**
     * Spawns EnderDragons in The End, according to various conditions.
     * If a dragon is spawned, the towers are reset.
     */
    private void respawnDragons() {

        // check to see if enough time has passed
        if (System.currentTimeMillis() < ((respawnTimer * convertMinToMillis) + lastDragonDeath)) {
            return;
        }

        // check for existing dragons, respawn up to dragonCount
        World world = Bukkit.getWorld(endWorldName);
        int numDragons = world.getEntitiesByClass(EnderDragon.class).size();
        int count = dragonCount - numDragons;
        if (count > 0) { 
            spawnDragons(count);
        }
    }

    /**
     * Resets the towers in The End.
     */
    private void resetTowers() {
        for (Tower tower : towers) {
            tower.restore(Bukkit.getWorld(endWorldName));
        }
    }
}
