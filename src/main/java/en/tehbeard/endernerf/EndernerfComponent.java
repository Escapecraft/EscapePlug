package en.tehbeard.endernerf;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="Endermen nerf",slug="endernerf",version="1.00")
public class EndernerfComponent extends AbstractComponent implements Listener {

	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEndermanPickup(EndermanPickupEvent event) {
	event.setCancelled(true);
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEndermanPlace(EndermanPlaceEvent event) {
		// TODO Auto-generated method stub
		event.setCancelled(true);
	}

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
        return true;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        
    }
	

	
}
