package en.tehbeard.endernerf;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name="Endermen nerf",slug="endernerf",version="1.00")
public class EndernerfComponent extends AbstractComponent implements Listener {

    @EventHandler(priority=EventPriority.HIGHEST)
    public void onEndermanChangeBlockEvent(EntityChangeBlockEvent event) {
	if (event.getEntityType() == EntityType.ENDERMAN) {
	    event.setCancelled(true);
	}
    }

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        Bukkit.getPluginManager().registerEvents(this,plugin);
        return true;
    }

    @Override
    public void disable() {
    }
	

	
}
