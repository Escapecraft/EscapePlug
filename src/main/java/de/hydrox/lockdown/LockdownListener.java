package de.hydrox.lockdown;

import net.escapecraft.escapePlug.component.BukkitEvent;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.Plugin;

public class LockdownListener extends PlayerListener {
	private String REJECTMSG = "";
	private String NOTIFICATIONMSG = "";

	public boolean isLockdownActive = false;

	public LockdownListener(Plugin plugin) {
		REJECTMSG = plugin.getConfig().getString("plugin.lockdown.rejectmsg");
		NOTIFICATIONMSG = plugin.getConfig().getString("plugin.lockdown.notificationmsg");
	}

	@BukkitEvent(type=Type.PLAYER_LOGIN,priority=Priority.Highest)
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
			event.setKickMessage(REJECTMSG);
			event.setResult(PlayerLoginEvent.Result.KICK_WHITELIST);
		}
	}

	@BukkitEvent(type=Type.PLAYER_JOIN,priority=Priority.Highest)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if(!isLockdownActive) {
			return;
		}
		if(event.getPlayer().hasPermission("escapeplug.lockdown.notify")){
			event.getPlayer().sendMessage(ChatColor.RED + NOTIFICATIONMSG);
		}
	}
}
