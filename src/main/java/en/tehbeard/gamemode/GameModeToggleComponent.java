package en.tehbeard.gamemode;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="Toggle Game Mode",slug="togglemode",version="1.00")
@BukkitCommand(command="togglemode")
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
    public boolean enable(Log log, EscapePlug plugin) {
        plugin.getComponentManager().registerCommands(this);
        return true;
    }

    @Override
    public void disable() {
        
    }

}
