package de.hydrox.blockalert;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class AlertListener extends AbstractListener{
	
	public AlertListener(Map<String, List<Integer>> blockBreak, Map<String, List<Integer>> blockPlace) {
		this.blockBreak = blockBreak;
		this.blockPlace = blockPlace;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockBreak(BlockBreakEvent event) {
		if (event.isCancelled() || !notifyBlockBreak(event)) {
			return;
		}
		String msg = ChatColor.GRAY + "EP: " + ChatColor.LIGHT_PURPLE + event.getPlayer().getName() + ChatColor.GOLD +" (break) " + ChatColor.WHITE + event.getBlock().getType() + " (#" + event.getBlock().getTypeId() + ").";
		
		notifyMods(msg);
		Bukkit.getConsoleSender().sendMessage(msg);
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onBlockPlace(BlockPlaceEvent event) {
		if (event.isCancelled() || !notifyBlockPlace(event)) {
			return;
		}
		String msg = ChatColor.GRAY + "EP: " + ChatColor.LIGHT_PURPLE + event.getPlayer().getName() + ChatColor.GOLD +" (use) " + ChatColor.WHITE + event.getBlock().getType() + " (#" + event.getBlock().getTypeId() + ").";
		
		notifyMods(msg);
		Bukkit.getConsoleSender().sendMessage(msg);
	}
}
