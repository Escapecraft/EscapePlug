package de.hydrox.lockdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class LockdownListener implements Listener {
	private String rejectMsg;
	private String notificationMsg;

	private boolean isLockdownActive = false;

	public LockdownListener(Plugin plugin) {
		rejectMsg = plugin.getConfig().getString("plugin.lockdown.rejectmsg");
		notificationMsg = plugin.getConfig().getString("plugin.lockdown.notificationmsg");
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerLogin(PlayerLoginEvent event){
		if(!isLockdownActive) {
			return;
		}
		Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "LOCKDOWNACTIVE");
		if(event.getPlayer().hasPermission("escapeplug.lockdown.allow")){
			Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "Overriding Lockdown");
			event.allow();
		}
		else {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Blocking login due to Lockdown");
			event.setKickMessage(rejectMsg);
			event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
		}
	}

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!isLockdownActive) {
			return;
		}
		if(event.getPlayer().hasPermission("escapeplug.lockdown.notify")){
			event.getPlayer().sendMessage(ChatColor.RED + notificationMsg);
		}
	}
	
	public void activate() {
		isLockdownActive = true;
	}

	public void deactivate() {
		isLockdownActive = false;
	}
}
