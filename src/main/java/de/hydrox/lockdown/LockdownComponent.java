package de.hydrox.lockdown;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="Emergency lockdown",slug="lockdown",version="1.0")
@BukkitCommand(command = { "lockdown" })
public class LockdownComponent extends AbstractComponent implements CommandExecutor {

	LockdownListener listener = null;
	
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if (!(sender.hasPermission("escapeplug.lockdown.change"))) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to change lockdown.");
			return true;
		}
		if (args.length >= 1) {
			if (args[0].equalsIgnoreCase("enable")) {
				this.listener.isLockdownActive = true;
				Player[] players = Bukkit.getOnlinePlayers();
				for (Player player : players) {
					if(player.hasPermission("escapeplug.lockdown.notify")) {
						player.sendMessage(ChatColor.RED + "LOCKDOWN-MODE ACTIVATED.");
					}
				}
				return true;
			}
			if (args[0].equalsIgnoreCase("disable")) {
				this.listener.isLockdownActive = false;
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
	@Override
	public boolean enable(Log log,EscapePlug plugin) {
		listener = new LockdownListener(plugin);
		plugin.registerEvents(listener);
		plugin.registerCommands(this);
		
		//finished loading lockdown
		return true;
	}
	@Override
	public void tidyUp() {}
	@Override
	public void reloadConfig() {}
}
