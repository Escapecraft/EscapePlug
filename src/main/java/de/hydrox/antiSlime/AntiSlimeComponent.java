package de.hydrox.antiSlime;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.entity.Slime;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.tulonsae.mc.util.Log;

public class AntiSlimeComponent extends AbstractComponent implements Listener {

	private Log log;

	

	@EventHandler
	public void onEntityTarget(EntityTargetEvent event) {
		if (event.getTarget() instanceof Player && event.getEntity() instanceof Slime) {
			log.info("[AntiSlime] SlimeTarget from " + event.getEntity().toString() + " to " + event.getTarget().toString());
			log.info("[AntiSlime] Trying to remove " + event.getEntity().toString());
			event.getEntity().remove();
			event.setCancelled(true);
		}
    }

	@EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
    	if (event instanceof EntityDamageByEntityEvent) {
    		this.onEntityDamageByEntity((EntityDamageByEntityEvent) event);
    		return;
    	}
    }
	@EventHandler
	public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Player && event.getDamager() instanceof Slime) {
			log.info("[AntiSlime] SlimeDamage from " + event.getDamager().toString() + " to " + event.getEntity().toString());
			log.info("[AntiSlime] Trying to remove " + event.getDamager().toString());
			event.getDamager().remove();
			event.setCancelled(true);
		}
	}

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.log = log;
        Bukkit.getPluginManager().registerEvents(this,plugin);
        return true;
    }

    @Override
    public void disable() {
        
    }
}
