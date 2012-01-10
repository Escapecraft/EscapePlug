package en.tehbeard.mentorTeleport;

import java.util.HashMap;

import net.escapecraft.escapePlug.component.BukkitCommand;

import org.bukkit.Location;
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
@BukkitCommand(command = { "mentorback" })
public class MentorBack implements CommandExecutor {

	protected static HashMap<String,Location> prevLoc = new HashMap<String,Location>(); 
	private Plugin plugin;
	public MentorBack(Plugin plugin){
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
			
			if(!MentorBack.prevLoc.containsKey(((Player)sender).getName())) {
				sender.sendMessage("No Previous desitnation found");
				return true;
			}
			Location l = MentorBack.prevLoc.get(((Player)sender).getName());
			
			MentorBack.prevLoc.remove(((Player)sender).getName());
			((Player)sender).teleport(l);
			

	
					
				
			
		}

		return false;
	}
}
