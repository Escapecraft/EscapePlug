package de.hydrox.lockdown;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCommand implements CommandExecutor {

	private LockdownListener listener = null;
	
	public LockdownCommand(LockdownListener listener) {
		this.listener = listener;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender.hasPermission("escapeplug.lockdown.change"))) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to change lockdown.");
			return true;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("enable")) {
				this.listener.activate();
				Player[] players = Bukkit.getOnlinePlayers();
				for (Player player : players) {
					if(player.hasPermission("escapeplug.lockdown.notify")) {
						player.sendMessage(ChatColor.RED + "LOCKDOWN-MODE ACTIVATED.");
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("disable")) {
				this.listener.deactivate();
				Player[] players = Bukkit.getOnlinePlayers();
				for (Player player : players) {
					if(player.hasPermission("escapeplug.lockdown.notify")) {
						player.sendMessage(ChatColor.GREEN + "Lockdown-Mode deactivated.");
					}
				}
				return true;
			}
		}
		return false;
	}

}
