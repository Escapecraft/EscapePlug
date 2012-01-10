package en.tehbeard.reserve;

import org.tulonsae.mc.util.Log;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

/**
 * provides the reserve list for EscapePlug
 * @author james
 *
 */
@ComponentDescriptor(name = "Reserve List", slug = "reserve", version = "1.0")
public class ReserveListComponent extends AbstractComponent{

	@Override
	public boolean enable(Log log,EscapePlug plugin) {
		plugin.registerEvents(new ReserveListener(log));
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
