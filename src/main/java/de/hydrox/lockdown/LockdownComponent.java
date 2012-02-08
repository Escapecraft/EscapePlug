package de.hydrox.lockdown;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.RegisteredListener;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="Emergency Lockdown",slug="lockdown",version="1.00")
@BukkitCommand(command="lockdown")
public class LockdownComponent extends AbstractComponent implements CommandExecutor {

	private LockdownListener listener = null;
	
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
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        listener = new LockdownListener(plugin);
        Bukkit.getPluginManager().registerEvents(listener , plugin);
        plugin.getComponentManager().registerCommands(this);
        return true;
    }
    @Override
    public void disable() {
        
    }

}
