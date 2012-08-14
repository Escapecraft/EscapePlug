package org.tulonsae.antixray;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.Location;
import org.bukkit.World;

import net.escapecraft.escapePlug.EscapePlug;
import org.tulonsae.mc.util.Log;

/**
 * Handle BlockPlaceEvent.
 */
public class AntiXrayListener implements Listener {

    private Log log;
    private EscapePlug plugin;

    private boolean updatePlayerOnlyChanges = false;
    private double nearby = 0;

    public AntiXrayListener(AntiXrayComponent component) {
        this.plugin = component.getPlugin();
        this.log = component.getLogger();

        nearby = (plugin.getServer().getViewDistance() + 1) * 16;
        // use the square of the distance rather than the distance
        // to avoid costly sqrt calculations
        nearby = nearby * nearby;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // check whether to process only player changes
        if (updatePlayerOnlyChanges && (event.getPlayer() == null)) {
            return;
        }

        // check whether tracking this world
        Block block = event.getBlock();
        World world = block.getWorld();
        if (!AntiXrayComponent.isMonitoredWorld(world)) {
            return;
        }

        // update the nearby players
        Player[] allPlayers = plugin.getServer().getOnlinePlayers();
        for (Player player : allPlayers) {
            if (player.getWorld() == world) {
                // note: using square of the distance for comparison
                double distance = player.getLocation().distanceSquared(block.getLocation());
                if (distance < nearby) {
                    AntiXrayComponent.updatePlayer(player, block);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onExplosion(EntityExplodeEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // check whether tracking this world
        Location loc = event.getLocation();
        World world = loc.getWorld();
        if (!AntiXrayComponent.isMonitoredWorld(world)) {
            return;
        }

        // update the nearby players
        Player[] allPlayers = plugin.getServer().getOnlinePlayers();
        for (Player player : allPlayers) {
            if (player.getWorld() == world) {
                // note: using square of the distance for comparison
                double distance = player.getLocation().distanceSquared(loc);
                if (distance < nearby) {
                    AntiXrayComponent.updatePlayer(player, event.blockList());
                }
            }
        }
    }
}
