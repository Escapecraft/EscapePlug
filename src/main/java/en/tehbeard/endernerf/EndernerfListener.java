package en.tehbeard.endernerf;

import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityListener;

public class EndernerfListener extends EntityListener {

	@Override
	public void onEndermanPickup(EndermanPickupEvent event) {
	event.setCancelled(true);
	}
	
	@Override
	public void onEndermanPlace(EndermanPlaceEvent event) {
		// TODO Auto-generated method stub
		event.setCancelled(true);
	}
	

	
}
