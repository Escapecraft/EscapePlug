package net.serubin.hatme;

import java.util.List;


import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

	EscapePlug plugin;

	public AdminCommand(List<Integer> rbBlocks, boolean rbAllow,
			String notAllowedMsg, boolean rbOp) {
		// TODO Auto-generated constructor stub
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (commandLabel.equalsIgnoreCase("hadmin")) {
			Player player = (Player) sender;
			if (args.length == 0) {
				return false;
			} else {
				if (args[0].equalsIgnoreCase("list")) {
					if (checkPermissionAdmin(player)) {
						if (args.length > 1) {
							player.sendMessage("/hadmin list - lists allowed block list");
							return true;
						} else {
							String blocks = plugin.rbBlocks.toString();
							player.sendMessage("Allowed Blocks: " + blocks);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "You do not have permission");
						return true;
					}

				} else {
				}
			}
		}
		return false;
	}

	public boolean checkPermissionAdmin(Player player) {
		if (player.hasPermission("escapeplug.hatme.admin"))
			return true;
		if (player.isOp())
			return true;
		return false;
	}
}
