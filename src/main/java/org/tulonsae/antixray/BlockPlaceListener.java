package org.tulonsae.antixray;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * Handle BlockPlaceEvent.
 */
public class BlockPlaceListener implements Listener {

    private EscapePlug plugin;
    private boolean updatePlayerOnlyChanges = true;

    public BlockPlaceListener(EscapePlug plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }

        // check whether to process only player changes
        // in other words, no WE changes
        if (updatePlayerOnlyChanges && (event.getPlayer() == null)) {
            return;
        }

        // check whether tracking this world
//        if (!AntiXrayComponent.isMonitoredWorld(event.getBlock().getWorld())) {
//            return;
//        }

        // get the nearby players
//        Players[] allPlayers = plugin.getServer().getOnlinePlayers();
    }
}
