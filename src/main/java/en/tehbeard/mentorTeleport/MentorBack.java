package en.tehbeard.mentorTeleport;

import java.util.HashMap;
import java.util.Map;

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
public class MentorBack implements CommandExecutor {

	protected static Map<String,Location> prevLoc = new HashMap<String,Location>(); 

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
