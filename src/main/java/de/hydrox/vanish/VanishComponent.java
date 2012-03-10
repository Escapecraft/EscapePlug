package de.hydrox.vanish;

import java.util.ArrayList;
import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name = "Vanish", slug = "vanish", version = "1.00")
@BukkitCommand(command = "vanish")
public class VanishComponent extends AbstractComponent implements
	CommandExecutor, Listener {

    private List<String> vanished = new ArrayList<String>();
    private List<String> fullVanish = new ArrayList<String>();

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
	plugin.getComponentManager().registerCommands(this);
	return true;
    }

    @Override
    public void disable() {

    }

    public boolean onCommand(CommandSender sender, Command cmd,
	    String commandLabel, String[] args) {
	if (args.length == 0) {
	    if (!sender.hasPermission("escapecraft.vanish.vanish")) {
		sender.sendMessage(ChatColor.RED + "You shall not vanish.");
	    }
	    if (sender instanceof ConsoleCommandSender) {
		sender.sendMessage("Console can't vanish.");
		return true;
	    }
	    String name = sender.getName();
	    if (vanished.contains(name) || fullVanish.contains(name)) {
		vanished.remove(name);
		fullVanish.remove(name);
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
		    player.showPlayer(Bukkit.getPlayerExact(name));
		}
		sender.sendMessage(ChatColor.GREEN
			+ "You are no longer vanished.");
	    } else {
		vanished.add(name);
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
		    if (!player.hasPermission("escapecraft.vanish.see")) {
			player.showPlayer(Bukkit.getPlayerExact(name));
		    }
		}
		sender.sendMessage(ChatColor.GREEN + "You are now vanished.");
	    }
	    return true;
	}
	if (args.length == 1) {
	    if (args[0].equalsIgnoreCase("full")) {
		if (!sender.hasPermission("escapecraft.vanish.vanishfull")) {
		    sender.sendMessage(ChatColor.RED
			    + "You shall not fully vanish.");
		}
		String name = sender.getName();
		vanished.remove(name);
		fullVanish.remove(name);
		fullVanish.add(name);
		Player[] players = Bukkit.getOnlinePlayers();
		for (Player player : players) {
		    if (!player.hasPermission("escapecraft.vanish.seefull")) {
			player.showPlayer(Bukkit.getPlayerExact(name));
		    }
		}
		sender.sendMessage(ChatColor.GREEN
			+ "You are now full vanished.");
	    }
	    if (args[0].equalsIgnoreCase("list")) {
		if (!sender.hasPermission("escapecraft.vanish.see")) {
		    sender.sendMessage(ChatColor.RED + "You shall not see.");
		}
		StringBuffer output = new StringBuffer();
		output.append("Vanished Players: ");
		for (String player : vanished) {
		    output.append(player).append(" ");
		}
		sender.sendMessage(ChatColor.GOLD + output.toString());
		if (sender.hasPermission("escapecraft.vanish.seefull")) {
		    output = new StringBuffer();
		    output.append("Full-Vanished Players: ");
		    for (String player : fullVanish) {
			output.append(player).append(" ");
		    }
		    sender.sendMessage(ChatColor.GOLD + output.toString());
		}
	    }
	}
	return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerLogin(PlayerLoginEvent event) {
	Player player = event.getPlayer();
	if (!player.hasPermission("escapecraft.vanish.see")) {
	    for (String hiddenPlayer : vanished) {
		player.hidePlayer(Bukkit.getPlayerExact(hiddenPlayer));
	    }
	}
	if (!player.hasPermission("escapecraft.vanish.seefull")) {
	    for (String hiddenPlayer : fullVanish) {
		player.hidePlayer(Bukkit.getPlayerExact(hiddenPlayer));
	    }
	}
    }

    @EventHandler
    public void onPlayerPickupItemEvent(PlayerPickupItemEvent event) {
	if (isPlayerVanished(event.getPlayer())) {
	    event.setCancelled(true);
	}
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
	Entity target = event.getTarget();
	if (target instanceof Player && isPlayerVanished((Player) target)) {
	    event.setCancelled(true);
	}
    }

    private boolean isPlayerVanished(Player player) {
	return isPlayerVanished(player.getName());
    }

    private boolean isPlayerVanished(String player) {
	if (vanished.contains(player) || fullVanish.contains(player)) {
	    return true;
	}
	return false;
    }
}
