package en.tehbeard.reserve;

import java.util.Set;

import org.bukkit.command.CommandExecutor;
import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;

public class ReserveListComponent extends AbstractComponent{

	@Override
	public boolean enable(EscapePlug plugin) {
		plugin.printCon("Enabling reserve list");
		ReserveListener rl = new ReserveListener();
		plugin.registerEvents(rl);
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

	@Override
	public Set<CommandExecutor> getCommands() {
		// TODO Auto-generated method stub
		return null;
	}

}
