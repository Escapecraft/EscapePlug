package de.hydrox.who;

import java.util.Arrays;

import me.tehbeard.BeardStat.containers.PlayerStatManager;
import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

@ComponentDescriptor(name="who command",slug="who",version="1.00")
@BukkitCommand(command="who")
public class WhoCommandComponent extends AbstractComponent implements CommandExecutor {

	private DroxPermsAPI perms = null;
	private PlayerComparator playerCompare = null;
	private PlayerStatManager beardStatManager = null;

	

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (!(sender.hasPermission("escapeplug.who.list"))) {
			sender.sendMessage(ChatColor.RED
					+ "You don't have permission for /who.");
			return true;
		}
		if (args.length == 0) {
			Player[] players = Bukkit.getOnlinePlayers();
			Arrays.sort(players, playerCompare);
			sender.sendMessage(ChatColor.GOLD + "Players online: "
					+ players.length + "/" + Bukkit.getMaxPlayers() + ".");
			StringBuffer playersString = new StringBuffer();
			for (Player player : players) {
				if (perms != null) {
					String group = perms.getPlayerGroup(player.getName());
					String groupPrefix = perms.getGroupInfo(group, "prefix");
					String playerPrefix = perms.getPlayerInfo(player.getName(),
							"prefix");
					if (playerPrefix != null) {
						playerPrefix = playerPrefix.replace("&", "\247");
						playersString.append(playerPrefix);
					} else if (groupPrefix != null) {
						groupPrefix = groupPrefix.replace("&", "\247");
						playersString.append(groupPrefix);
					} else {
						playersString.append(ChatColor.WHITE);
					}
				}
				playersString.append(player.getName());
				playersString.append(" ");
			}
			sender.sendMessage(ChatColor.GOLD + "Players: " + playersString);
		}
		if (args.length == 1) {
			if (!(sender.hasPermission("escapeplug.who.player"))) {
				sender.sendMessage(ChatColor.RED
						+ "You don't have permission for /who " + args[0] + ".");
				return true;
			}
			Player player = Bukkit.getPlayer(args[0]);
			if (player != null) {				
				sender.sendMessage(ChatColor.GOLD + "Player: " + player.getName());
				sender.sendMessage(ChatColor.GOLD + "IP: " + player.getAddress());
				Location location = player.getLocation();
				sender.sendMessage(ChatColor.GOLD + "World: " + location.getWorld().getName());
				sender.sendMessage(ChatColor.GOLD + "Coords: (" + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ() + ")");
				sender.sendMessage(ChatColor.GOLD + "Health: " + player.getHealth() + "/20");
				if (perms != null) {
					String group = perms.getPlayerGroup(player.getName());
					String groupPrefix = perms.getGroupInfo(group, "prefix");
					groupPrefix = groupPrefix.replace("&", "\247");
					sender.sendMessage(ChatColor.GOLD + "Group: " + groupPrefix + group);
					String playerPrefix = perms.getPlayerInfo(player.getName(), "prefix");
					String effectivePrefix = "";
					if (playerPrefix != null) {
						effectivePrefix = playerPrefix.replace("&", "\247");
					} else if (groupPrefix!= null) {
						effectivePrefix = groupPrefix;
					}
					sender.sendMessage(ChatColor.GOLD + "Shown Name: " + effectivePrefix + player.getName());
				}
				if (beardStatManager!=null) {
					long seconds = beardStatManager.getPlayerBlob(player.getName()).getStat("stats","playedfor").getValue();
					int weeks   = (int) seconds / 604800;
					int days = (int)Math.ceil((seconds -604800*weeks) / 86400);
					int hours = (int)Math.ceil((seconds - (86400 * days + 604800*weeks)) / 3600);
					int minutes = (int)Math.ceil((seconds - (604800*weeks + 86400 * days + 3600 * hours)) / 60);
					StringBuffer playTime = new StringBuffer();
					playTime.append(ChatColor.GOLD + "Playtime: ");
					if (weeks > 0) {
						playTime.append(weeks +" weeks ");
					}
					if (days > 0) {
						playTime.append(days +" days ");
					}
					if (hours > 0) {
						playTime.append(hours +" hours ");
					}
					if (minutes > 0) {
						playTime.append(minutes +" mins ");
					}
					sender.sendMessage(playTime.toString());
				}
				sender.sendMessage(ChatColor.GOLD + "OP: " + player.isOp());
			}
		}
		return true;
	}

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.perms = plugin.getDroxPermsAPI();
        this.playerCompare = new PlayerComparator(perms);
        this.beardStatManager = plugin.getBeardStatManager();
        plugin.getComponentManager().registerCommands(this);
        return true;
    }

    @Override
    public void disable() {
        
    }

}
