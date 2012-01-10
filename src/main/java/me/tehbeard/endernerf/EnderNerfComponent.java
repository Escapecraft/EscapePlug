package me.tehbeard.endernerf;

import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityListener;
import org.tulonsae.mc.util.Log;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitEvent;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

@ComponentDescriptor(name="Endernerf",slug="endernerf",version="1.0")
public class EnderNerfComponent extends AbstractComponent {

	@Override
	public boolean enable(Log log,EscapePlug plugin) {
		plugin.registerEvents(new EndListener());
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

	private class EndListener extends EntityListener{
		@Override
		@BukkitEvent(type=Type.ENDERMAN_PICKUP,priority=Priority.Highest)
		public void onEndermanPickup(EndermanPickupEvent event) {
			event.setCancelled(true);
		}
		
		@Override
		@BukkitEvent(type=Type.ENDERMAN_PLACE,priority=Priority.Highest)
		public void onEndermanPlace(EndermanPlaceEvent event) {
			event.setCancelled(true);
		}
	}
}
