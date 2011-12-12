package de.hydrox.lockdown;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LockdownCommand implements CommandExecutor {

	LockdownListener listener = null;
	
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
				this.listener.isLockdownActive = true;
				return true;
			}
			if (args[0].equalsIgnoreCase("disable")) {
				this.listener.isLockdownActive = false;
				return true;
			}
		}
		return false;
	}

}
