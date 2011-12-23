package en.tehbeard.gamemode;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeToggle implements CommandExecutor{

	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if(sender instanceof Player){
			if(!((Player)sender).hasPermission("escapeplug.gamemode")){
				return true;
			}
			if(((Player)sender).getGameMode().equals(GameMode.SURVIVAL)){
				((Player)sender).setGameMode(GameMode.CREATIVE);	
			}
			else
			{
				((Player)sender).setGameMode(GameMode.SURVIVAL);	
			}
			
		}
		return true;
	}

}
