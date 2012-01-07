package en.tehbeard.kitPlugin.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class KitCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl,
			String[] args) {
		// TODO Auto-generated method stub
		if(!sender.hasPermission("escapeplug.kit")){return false;}
		
		if(args.length==0){
			
		}
		
		return false;
	}

}
