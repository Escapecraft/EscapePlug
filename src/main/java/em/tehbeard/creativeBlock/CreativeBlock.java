package em.tehbeard.creativeBlock;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CreativeBlock implements CommandExecutor { 


	public boolean onCommand(CommandSender sender, Command command, String label,
			String[] args) {
		if(sender.hasPermission("escapeplug.creativeblock")){
			
		}
		return false;
	}
}
