package en.tehbeard.mentorTeleport;

import java.util.HashMap;
import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.ComponentDescriptor;
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

@BukkitCommand(command = { "mentortp" , "mentorback" })
@ComponentDescriptor(name="Mentor TP",slug="mentortp",version="1.0")
public class MentorTeleportComponent extends AbstractComponent implements CommandExecutor {

	private HashMap<String,Location> prevLoc = new HashMap<String,Location>();
	private Plugin plugin;

	/**
	 * Handle mentor commands
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	public boolean onCommand(CommandSender sender,Command cmd,String commandLabel, String[] args){
		if(commandLabel.equals("mentortp")){
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
								prevLoc.put(((Player)sender).getName(), ((Player)sender).getLocation());
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
		}
		if(commandLabel.equals("mentorback")){
			if (!(sender.hasPermission("escapeplug.mentor.teleport"))) {
				sender.sendMessage("You don't have permission to teleport.");
				return true;
			}
			if(sender instanceof Player){
				
				if(!prevLoc.containsKey(((Player)sender).getName())) {
					sender.sendMessage("No Previous desitnation found");
					return true;
				}
				Location l = prevLoc.get(((Player)sender).getName());
				
				prevLoc.remove(((Player)sender).getName());
				((Player)sender).teleport(l);
			}
		}
		return false;

	}
	@Override
	public boolean enable(EscapePlug plugin) {
		this.plugin = plugin;
		plugin.registerCommands(this);
		return true;
	}
	@Override
	public void tidyUp() {}
	@Override
	public void reloadConfig() {}
}