package com.tehbeard.areablock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.component.Log;
import net.escapecraft.escapeplug.EscapePlug;

@ComponentDescriptor(name="Area Block",slug="areablock",version="1.00")
public class AreaBlockComponent extends AbstractComponent implements Listener, Runnable {

    ChunkCache<GatedArea> areas = new ChunkCache<GatedArea>();
    public final Map<String,GatedArea> areaMap = new HashMap<String, GatedArea>();
    YamlConfiguration config = new YamlConfiguration();
    private File file;
    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        
        try {
            file = new File(plugin.getDataFolder(),"areas.yml");
            file.createNewFile();
            config.load(file);

            GatedArea g;

            
            //load all areas
            for (String key : config.getKeys(false)) {
                g = new GatedArea(config.getConfigurationSection(key));
                areaMap.put(key, g);
                for (Cuboid area : g.getDetectAreas()) {
                    areas.addEntry(area, g);
                }
            }

            AreaBlockCommands commands = new AreaBlockCommands(this);
            Bukkit.getPluginManager().registerEvents(commands, plugin);
            plugin.getCommand("areablock").setExecutor(commands);
            
            Bukkit.getPluginManager().registerEvents(this, plugin);
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this, 20L, 20L);
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
        for (Entry<String, GatedArea> entry  : areaMap.entrySet()) {
            config.set(entry.getKey(),entry.getValue().toConfig());
        }
        try {
            config.save(file);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (event.isCancelled()==false
                && (event.getTo().getBlockX() != event.getFrom().getBlockX()
                || event.getTo().getBlockY() != event.getFrom().getBlockY()
                || event.getTo().getBlockZ() != event.getFrom().getBlockZ())) {

            for (CuboidEntry<GatedArea> entry : areas.getEntries(event.getPlayer())) {
                entry.getEntry().updateArea();
            }
        }
    }

    public void run() {
        for (GatedArea ga : areaMap.values()) {
            
            if(!ga.isOpen()){
                ga.updateArea();
            }
        }
    }
}
