package de.hydrox.mobcontrol;

import java.util.List;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

@ComponentDescriptor(slug="mobcontrol",name="Mob Control",version="1.00")
public class MobControlComponent extends AbstractComponent implements Listener {

    private List<String> worlds = null;
    private List<String> mobs = null;

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (worlds.contains(event.getLocation().getWorld().getName())) {
            if (mobs.contains(event.getEntityType().getName())) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public boolean enable(EscapePlug plugin) {
        worlds = plugin.getConfig().getStringList("plugin.mobcontrol.worlds");
        mobs = plugin.getConfig().getStringList("plugin.mobcontrol.mobs");
        Bukkit.getServer().getPluginManager().registerEvents(this, plugin);
        return false;
    }

    @Override
    public void disable() {
    }
}
