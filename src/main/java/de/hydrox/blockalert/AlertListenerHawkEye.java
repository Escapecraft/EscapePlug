package de.hydrox.blockalert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.util.Vector;

import uk.co.oliwali.HawkEye.DataType;
import uk.co.oliwali.HawkEye.SearchParser;
import uk.co.oliwali.HawkEye.callbacks.BaseCallback;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchDir;
import uk.co.oliwali.HawkEye.database.SearchQuery.SearchError;
import uk.co.oliwali.HawkEye.entry.DataEntry;
import uk.co.oliwali.HawkEye.util.BlockUtil;
import uk.co.oliwali.HawkEye.util.HawkEyeAPI;

public class AlertListenerHawkEye extends AbstractListener {
	
	private List<Player> moderators = new ArrayList<Player>();
	private Map<String, List<Integer>> blockBreak = null;
	private Map<String, List<Integer>> blockPlace = null;

	public AlertListenerHawkEye(Map<String, List<Integer>> blockBreak, Map<String, List<Integer>> blockPlace) {
		Player[] players = Bukkit.getOnlinePlayers();
		this.blockBreak = blockBreak;
		this.blockPlace = blockPlace;
		
		for (Player player : players) {
			if (player.hasPermission("escapeplug.blockalert.notify")) {
				moderators.add(player);
			}
		}
	}
	
	public boolean notifyBlockBreak(BlockBreakEvent event) {
		int id = event.getBlock().getTypeId();
		String world = event.getBlock().getLocation().getWorld().getName();
		Player player = event.getPlayer();
		if (blockBreak.containsKey(world) && blockBreak.get(world).contains(id) && !player.hasPermission("escapeplug.blockalert.ignore." + id)) {
			return true;
		}
		return false;
	}
	
	public boolean notifyBlockPlace(BlockPlaceEvent event) {
		int id = event.getBlock().getTypeId();
		String world = event.getBlock().getLocation().getWorld().getName();
		Player player = event.getPlayer();
		if (blockPlace.containsKey(world) && blockPlace.get(world).contains(id) && !player.hasPermission("escapeplug.blockalert.ignore." + id)) {
			return true;
		}
		return false;
	}
	
	
	public void addModerator(Player mod) {
		moderators.add(mod);
	}
	
	public void removeModerator(Player mod) {
		moderators.remove(mod);
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled() || !notifyBlockBreak(event)) {
			return;
		}

		int x = event.getBlock().getLocation().getBlockX();
		int y = event.getBlock().getLocation().getBlockY();
		int z = event.getBlock().getLocation().getBlockZ();
		Vector loc = new Vector(x, y, z);
		BaseCallback callBack = new SimpleSearch(event.getPlayer(), event.getBlock().getType());
		SearchParser parser = new SearchParser();
		parser.loc = loc;
		parser.actions = Arrays.asList(DataType.BLOCK_PLACE);

		HawkEyeAPI.performSearch(callBack, parser, SearchDir.DESC);
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled() || !notifyBlockPlace(event)) {
			return;
		}
		String msg = ChatColor.DARK_RED + "BlockPlace: " + event.getPlayer().getName() + " placed " + event.getBlock().getType();
		
		for (Player moderator : moderators) {
			moderator.sendMessage(msg);
		}
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
        	if (results.size()>0) {
            	DataEntry entry = results.get(0);
            	msg = ChatColor.DARK_RED + "BlockBreak: " + player.getName() + " broke " + block + " placed by " + entry.getPlayer() + ".";
        	} else {
        		msg = ChatColor.DARK_RED + "BlockBreak: " + player.getName() + " broke " + block + " placed by UNKNOWN.";
        	}
        	for (Player moderator : moderators) {
				moderator.sendMessage(msg);
			}
			Bukkit.getConsoleSender().sendMessage(msg);
        }
        
        public void error(SearchError error, String message) {
        	String msg = "[EscapePlug] Debug: Search failed.";
        	for (Player moderator : moderators) {
				moderator.sendMessage(msg);
			}
			Bukkit.getConsoleSender().sendMessage(msg);
        }

    }
}
