package de.hydrox.blockalert;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public abstract class AbstractListener implements Listener{

	protected Map<String, List<Integer>> blockBreak;
	protected Map<String, List<Integer>> blockPlace;
	
	public void notifyMods(String msg) {
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
			if (player.hasPermission("escapeplug.blockalert.notify")) {
				player.sendMessage(msg);
			}
		}
	}
	
	public boolean notifyBlockBreak(BlockBreakEvent event) {
		int id = event.getBlock().getTypeId();
		String world = event.getBlock().getLocation().getWorld().getName();
		Player player = event.getPlayer();
		if (blockBreak.containsKey(world)) {
			if (blockBreak.get(world).contains(id)) {
				if (!player.hasPermission("escapeplug.blockalert.ignore") && !player.hasPermission("escapeplug.blockalert.ignore." + id)) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean notifyBlockPlace(BlockPlaceEvent event) {
		int id = event.getBlock().getTypeId();
		String world = event.getBlock().getLocation().getWorld().getName();
		Player player = event.getPlayer();
		if (blockPlace.containsKey(world)) {
			if (blockPlace.get(world).contains(id)) {
				if (!player.hasPermission("escapeplug.blockalert.ignore") && !player.hasPermission("escapeplug.blockalert.ignore." + id)) {
					return true;
				}
			}
		}
		return false;
	}
}
