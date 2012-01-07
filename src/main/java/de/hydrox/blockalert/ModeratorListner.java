package de.hydrox.blockalert;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerQuitEvent;

public class ModeratorListner extends PlayerListener {
	
	AbstractListener listener = null;
	
	public ModeratorListner(AbstractListener listener) {
		this.listener = listener;
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event.getPlayer().hasPermission("escapeplug.blockalert.notify")) {
			listener.addModerator(event.getPlayer());
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_BLUE + "Add " + event.getPlayer().getName());
		}
	}

	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event.getPlayer().hasPermission("escapeplug.blockalert.notify")) {
			listener.removeModerator(event.getPlayer());
			Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_BLUE + "Remove " + event.getPlayer().getName());
		}
	}

}
