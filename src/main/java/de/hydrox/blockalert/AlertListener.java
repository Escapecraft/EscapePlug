package de.hydrox.blockalert;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;

public class AlertListener extends AbstractListener{
	
	private List<Player> moderators = new ArrayList<Player>();
	private Map<String, List<Integer>> blockBreak = null;
	private Map<String, List<Integer>> blockPlace = null;
	
	public AlertListener(Map<String, List<Integer>> blockBreak, Map<String, List<Integer>> blockPlace) {
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
		if (blockBreak.containsKey(world)) {
			if (blockBreak.get(world).contains(id)) {
				if (!player.hasPermission("eescapeplug.blockalert.ignore." + id)) {
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
				if (!player.hasPermission("escapeplug.blockalert.ignore." + id)) {
					return true;
				}
			}
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
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "DEBUG");
		String msg = ChatColor.DARK_RED + "BlockBreak: " + event.getPlayer().getName() + " broke " + event.getBlock().getType();
		
		for (Player moderator : moderators) {
			moderator.sendMessage(msg);
			Bukkit.getConsoleSender().sendMessage(msg);
		}
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled() || !notifyBlockPlace(event)) {
			return;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_RED + "DEBUG");
		String msg = ChatColor.DARK_RED + "BlockPlace: " + event.getPlayer().getName() + " placed " + event.getBlock().getType();
		
		for (Player moderator : moderators) {
			moderator.sendMessage(msg);
			Bukkit.getConsoleSender().sendMessage(msg);
		}
	}
}
