package en.tehbeard.areablock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.tulonsae.mc.util.Log;

import me.tehbeard.utils.cuboid.*;
import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

@ComponentDescriptor(name="Area Block",slug="areablock",version="1.00")
public class AreaBlockComponent extends AbstractComponent implements Listener{

    private ChunkCache<GatedArea> areas = new ChunkCache<GatedArea>();
    private Map<String,GatedArea> areaMap = new HashMap<String, GatedArea>();
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.load(new File(plugin.getDataFolder(),"areas.yml"));
            
            GatedArea g;
            
            //load all areas
            for(String key : config.getKeys(false)){
                g = new GatedArea(config.getConfigurationSection(key));
                areaMap.put(key, g);
                for( Cuboid area : g.getDetectAreas()){
                    areas.addEntry(area, g);
                }
            }
            
            Bukkit.getPluginManager().registerEvents(this, plugin);
            
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        } catch (InvalidConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub
        
    }
    
    
    @EventHandler
    public void onMove(PlayerMoveEvent event){
        if(event.isCancelled()==false &&
                (event.getTo().getBlockX() != event.getFrom().getBlockX() || 
                event.getTo().getBlockY() != event.getFrom().getBlockY() || 
                event.getTo().getBlockZ() != event.getFrom().getBlockZ() )){
            
            for(CuboidEntry<GatedArea> entry  :  areas.getEntries(event.getPlayer())){
                entry.getEntry().updateArea();
            }
        }
    }

}
