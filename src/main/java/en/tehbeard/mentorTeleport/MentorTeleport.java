package en.tehbeard.mentorTeleport;

import net.escapecraft.escapePlug.IEscapePlugCommandHandler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;


/**
 * Mentor Teleportation to newbies.
 * @author James
 *
 */
public class MentorTeleport implements IEscapePlugCommandHandler {

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
	public boolean handleCommand(CommandSender sender,String commandLabel, String[] args){
		if(sender instanceof Player){
			//TODO: ADD PERMISSIONS FOR THESE COMMANDS!
			if(commandLabel.equals("mentortp")){
				if(args.length == 1){
					if(plugin.getServer().getPlayer(args[0])!=null){
						Player p = plugin.getServer().getPlayer(args[0]);
						if(!p.hasPermission("escapeplug.mentor.teleport.notarget")){
							sender.sendMessage("Sending you to " + p.getName());
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
