package en.tehbeard.gamemode;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@ComponentDescriptor(name = "Toggle GameMode", slug = "togglemode", version = "1.0")
@BukkitCommand(command = { "togglemode" })
public class GameModeToggleComponent extends AbstractComponent implements CommandExecutor{

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

	@Override
	public boolean enable(EscapePlug plugin) {
		
		plugin.registerCommands(this);
	
		return true;
	}

	@Override
	public void tidyUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadConfig() {
		// TODO Auto-generated method stub
		
	}

}
