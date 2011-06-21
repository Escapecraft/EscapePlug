package en.tehbeard.mentorTeleport;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import en.tehbeard.permwrapper.PermissionWrapper;


/**
 * Mentor Teleportation to newbies.
 * @author James
 *
 */
public class MentorTeleport {

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
			if(commandLabel.equals("mentor")){
				if(PermissionWrapper.hasPermission((Player)sender, "mentor.teleport",false)){
					if(args.length == 1){
						if(plugin.getServer().getPlayer(args[0])!=null){
							Player p = plugin.getServer().getPlayer(args[0]);
							if(PermissionWrapper.inGroup(p.getWorld(),p,"default")||PermissionWrapper.inGroup(p.getWorld(),p,"builder")){
								sender.sendMessage("Sending you to " + p.getName());
								((Player)sender).teleport(p);
								return true;
								}
						}
						else
						{
							sender.sendMessage("Cannot find player");
						}
					}
					else
					{
						sender.sendMessage("No player selected");
					}
				}

			}
		}

		return true;
	}
}
