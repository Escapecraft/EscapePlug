package de.hydrox.antiSlime;

import java.util.logging.Logger;

import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;

public class SlimeDamageListener extends EntityListener {

	private Logger log;

	public SlimeDamageListener() {
		log = Logger.getLogger("Minecraft");
	}

	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player && event.getEntity() instanceof Slime) {
			log.info("[AntiSlime] SlimeTarget from " + event.getEntity().toString() + " to " + event.getTarget().toString());
			log.info("[AntiSlime] Trying to remove " + event.getEntity().toString());
			event.getEntity().remove();
			event.setCancelled(true);
		}
    }

    public void onEntityDamage(EntityDamageEvent event) {
    	if (event instanceof EntityDamageByEntityEvent) {
    		this.onEntityDamageByEntity((EntityDamageByEntityEvent) event);
    		return;
    	}
    }
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Slime) {
			log.info("[AntiSlime] SlimeDamage from " + event.getDamager().toString() + " to " + event.getEntity().toString());
			log.info("[AntiSlime] Trying to remove " + event.getDamager().toString());
			event.getDamager().remove();
			event.setCancelled(true);
		}
	}
}
