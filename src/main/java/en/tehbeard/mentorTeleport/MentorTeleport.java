package en.tehbeard.mentorTeleport;

import net.escapecraft.escapePlug.component.BukkitCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Mentor Teleportation to newbies.
 * @author James
 *
 */
@BukkitCommand(command = { "mentortp" })
public class MentorTeleport implements CommandExecutor {

	private Plugin plugin;
	public MentorTeleport(Plugin plugin){
		this.plugin = plugin;
	}
	/**
	 * Handle mentor commands
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	public boolean onCommand(CommandSender sender,Command cmd,String commandLabel, String[] args){
		
		if (!(sender.hasPermission("escapeplug.mentor.teleport"))) {
			sender.sendMessage("You don't have permission to teleport.");
			return true;
		}
		if(sender instanceof Player){
			if(commandLabel.equals("mentortp")){
				if(args.length == 1){
					if(plugin.getServer().getPlayer(args[0])!=null){
						Player p = plugin.getServer().getPlayer(args[0]);
						if(!p.hasPermission("escapeplug.mentor.teleport.notarget")){
							sender.sendMessage("Sending you to " + p.getName());
							MentorBack.prevLoc.put(((Player)sender).getName(), ((Player)sender).getLocation());
							((Player)sender).teleport(p);
							return true;
						}
						else
						{
							sender.sendMessage("You are not allowed to teleport to this player.");
							return true;
						}
					}
					else
					{
						sender.sendMessage("Cannot find player");
						return true;
					}
				}
				else
				{
					sender.sendMessage("No player selected");
					return true;
				}
			}
		}

		return false;
	}
}
