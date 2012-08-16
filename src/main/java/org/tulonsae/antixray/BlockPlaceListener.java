package org.tulonsae.antixray;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * Handle BlockPlaceEvent.
 */
public class BlockPlaceListener implements Listener {

    private boolean updatePlayerOnlyChanges = true;

    public BlockPlaceListener(EscapePlug plugin) {
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
