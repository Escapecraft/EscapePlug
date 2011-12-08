package de.hydrox.lockdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;

public class LockdownListener extends PlayerListener {
	public boolean isLockdownActive = false;

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
			event.setKickMessage("Server is on Emergency lockdown.");
			event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
		}
	}

	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!isLockdownActive) {
			return;
		}
		event.getPlayer().sendMessage(ChatColor.RED + "!!!SERVER IS IN LOCKDOWN-MODE!!!");
	}
}
