package en.tehbeard.pigjouster;

import org.bukkit.entity.Pig;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;

/**
 * Allows invincible pigs to be created for sport!
 * @author James
 *
 */
public class PigListener extends EntityListener {

	@Override
	public
	void onEntityDamage(EntityDamageEvent event){
		//check if its a protected pig.
		if(event.getEntity() instanceof Pig){
			if(PigJouster.isPig((Pig)event.getEntity())){
				//cancel event if its a jousting pig.
				event.setCancelled(true);
			}
		}
		
	}
}
