package en.tehbeard.mentorTeleport;

import java.util.HashMap;
import java.util.Map;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.tulonsae.mc.util.Log;


/**
 * Mentor Teleportation to newbies.
 * @author James
 *
 */
@ComponentDescriptor(slug="mentortp",name="Mentor TP",version="1.00")
@BukkitCommand(command={"mentortp","mentorback"})
public class MentorTeleportComponent extends AbstractComponent implements CommandExecutor {

	private Plugin plugin;
	private Map<String,Location> prevLoc; 

	/**
	 * Handle mentor commands
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	public boolean onCommand(CommandSender sender,Command cmd,String commandLabel, String[] args){
		if(commandLabel.equalsIgnoreCase("mentortp")){
			if (!(sender.hasPermission("escapeplug.mentor.teleport"))) {
				sender.sendMessage("You don't have permission to teleport.");
				return true;
			}
			if(sender instanceof Player){
				if(commandLabel.equals("mentortp") && args.length == 1){
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
		
		if(commandLabel.equalsIgnoreCase("mentorback")){
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
	public boolean enable(Log log, EscapePlug plugin) {
		this.plugin = plugin;
		prevLoc = new HashMap<String,Location>();
		plugin.getComponentManager().registerCommands(this);
		return true;
	}
	@Override
	public void disable() {

	}
}
