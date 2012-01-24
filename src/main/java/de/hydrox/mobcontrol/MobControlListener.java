package de.hydrox.mobcontrol;

import java.util.List;

import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityListener;

public class MobControlListener extends EntityListener {

	private List<String> worlds = null;
	private List<String> mobs = null;

	public MobControlListener(List<String> worlds, List<String> mobs) {
		this.worlds = worlds;
		this.mobs = mobs;
	}

	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (worlds.contains(event.getLocation().getWorld().getName())) {
			if (mobs.contains(event.getCreatureType().getName())) {
				event.setCancelled(true);
			}
		}
	}
}
