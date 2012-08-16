package org.tulonsae.antixray;

import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(slug="antixray", name="AntiXray", version="1.00")
public class AntiXrayComponent extends AbstractComponent {

    private Log log;
    private EscapePlug plugin;

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.log = log;
        this.plugin = plugin;

        Bukkit.getPluginManager().registerEvents(new AntiXrayListener(this), plugin);

	return true;
    }

    @Override
    public void disable() {
    }

    protected Log getLogger() {
        return log;
    }

    protected EscapePlug getPlugin() {
        return plugin;
    }

    /**
     * Determine if the specified world is being monitored for antixray.
     *
     * @param world the world to check
     * @return true if the world is monitored, otherwise false
     */
    protected static boolean isMonitoredWorld(World world) {
        // TODO - implement this

        return true;
    }

    /**
     * Update the player with new blocks around the location specified.
     *
     * @param player the online player to update
     * @param block the block which has been removed
     */
    protected static void updatePlayer(Player player, Block block) {
        updateAdjacentBlock(player, block, BlockFace.DOWN);
        updateAdjacentBlock(player, block, BlockFace.UP);
        updateAdjacentBlock(player, block, BlockFace.NORTH);
        updateAdjacentBlock(player, block, BlockFace.SOUTH);
        updateAdjacentBlock(player, block, BlockFace.WEST);
        updateAdjacentBlock(player, block, BlockFace.EAST);
    }

    /**
     * Update the player for blocks around the blocks in the list.
     *
     * @param player the online player to update
     * @param blockList the list of blocks which have been removed
     */
    protected static void updatePlayer(Player player, List<Block> blockList) {
        for (Block block : blockList) {
            updatePlayer(player, block);
        }
    }

    // update adjacent block
    private static void updateAdjacentBlock(Player player, Block block, BlockFace face) {
        Block adjBlock = block.getRelative(face);
        if (adjBlock == null) {
            return;
        }

        int typeId = adjBlock.getTypeId();
        if (isProtectedBlock(typeId)) {
            player.sendBlockChange(adjBlock.getLocation(), typeId, adjBlock.getData());
        }
    }

    // check if protected block
    private static boolean isProtectedBlock(int id) {
        int[] ids = { 14, 15, 16, 21, 48, 54, 56, 73, 74, 98, 129 };

        for (int i = 0; i < ids.length; i++) {
            if (id == ids[i]) {
                return true;
            }
        }

        return false;
    }
}
