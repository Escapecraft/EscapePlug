package de.hydrox.portalblocker;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.event.world.PortalCreateEvent.CreateReason;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapeplug.EscapePlug;

@ComponentDescriptor(slug="portalblocker",name="Portal Blocker",version="1.00")
public class PortalBlockerComponent extends AbstractComponent implements Listener {

    private List<String> worlds = null;

    @Override
    public boolean enable(EscapePlug plugin) {
        worlds = plugin.getConfig().getStringList("plugin.portalblocker.worlds");
        Bukkit.getPluginManager().registerEvents(this, plugin);
        return true;
    }

    @EventHandler
    public void onPortalCreateEvent(PortalCreateEvent event) {
        if(worlds.contains(event.getWorld().getName()) && event.getReason() == CreateReason.FIRE) {
            event.setCancelled(true);
            log.info("Prevented Portal creation in world " + event.getWorld().getName());
        }
    }
    
    @Override
    public void disable() {
    }
}
