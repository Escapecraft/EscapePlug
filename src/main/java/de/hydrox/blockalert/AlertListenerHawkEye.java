package de.hydrox.blockalert;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.SearchParser;
import uk.co.oliwali.HawkEye.callbacks.BaseCallback;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchDir;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class AlertListenerHawkEye extends AbstractListener {
    boolean notifyOnCanceledBreak = false;
    boolean notifyOnCanceledPlace = false;

	public AlertListenerHawkEye(Map<String, List<Integer>> blockBreak, Map<String, List<Integer>> blockPlace, boolean notifyOnCanceledBreak, boolean notifyOnCanceledPlace) {
		this.blockBreak = blockBreak;
		this.blockPlace = blockPlace;
		this.notifyOnCanceledBreak = notifyOnCanceledBreak;
		this.notifyOnCanceledPlace = notifyOnCanceledPlace;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if ((event.isCancelled() && !notifyOnCanceledBreak) || !notifyBlockBreak(event)) {
			return;
		}

		int x = event.getBlock().getLocation().getBlockX();
		int y = event.getBlock().getLocation().getBlockY();
		int z = event.getBlock().getLocation().getBlockZ();
		String [] worlds = {event.getBlock().getLocation().getWorld().getName()};
		Vector loc = new Vector(x, y, z);
		BaseCallback callBack = new SimpleSearch(event.getPlayer(), event.getBlock().getType());
		SearchParser parser = new SearchParser();
		parser.loc = loc;
		parser.worlds = worlds;
		parser.actions = Arrays.asList(DataType.BLOCK_PLACE);

		HawkEyeAPI.performSearch(callBack, parser, SearchDir.DESC);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if ((event.isCancelled() && !notifyOnCanceledPlace) || !notifyBlockPlace(event)) {
			return;
		}
		String msg = ChatColor.GRAY + "EP: " + ChatColor.LIGHT_PURPLE + event.getPlayer().getName() + ChatColor.GOLD +" (use) " + ChatColor.WHITE + event.getBlock().getType() + " (#" + event.getBlock().getTypeId() + ").";
		
		notifyMods(msg);
		Bukkit.getConsoleSender().sendMessage(msg);
	}
	
	class SimpleSearch extends BaseCallback {
        
		private Player player = null;
		private Material block;
        public SimpleSearch(Player player, Material block) {
        	this.player = player;
        	this.block = block;
        }

        public void execute() {
        	String msg = null;
        	String owner = null;
        	if (results.size()>0) {
            	DataEntry entry = results.get(0);
            	owner = entry.getPlayer();
            	if (player.getName().equals(owner)) {
            		return;
            	}
        	} else {
        		owner = "UNKNOWN";
        	}
    		msg = ChatColor.GRAY + "EP: " + ChatColor.LIGHT_PURPLE + player.getName() + ChatColor.GOLD +" (break) " + ChatColor.WHITE + block + " (#" + block.getId() + ") placed by " + ChatColor.LIGHT_PURPLE + owner;
    		notifyMods(msg);
			Bukkit.getConsoleSender().sendMessage(msg);
        }
        
        public void error(SearchError error, String message) {
        	String msg = "[EscapePlug] Debug: Search failed.";
    		notifyMods(msg);
			Bukkit.getConsoleSender().sendMessage(msg);
        }

    }
}
