package com.tehbeard.kit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.component.Log;
import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

/**
 * Manages loading and saving of kit data
 * @author james
 *
 */
@ComponentDescriptor(name="EscapeKit",slug="kitplugin",version="1.0")
public class EscapeKitComponent extends AbstractComponent {

    private YamlConfiguration kitConfig = null;
    private YamlConfiguration contextConfig = null;

    private Map<String,Kit> kits;
    private File dbKitsFile;

    private EscapePlug plugin;

    private File dbContextFile;
    private Log log;

    public void loadData() {
        loadData(true);
    }

    /**
     * reload Kit Data from file
     * @param loadContext 
     */
    public void loadData(boolean loadContext) {
        kits = new HashMap<String,Kit>();

        //load config
        kitConfig = YamlConfiguration.loadConfiguration(dbKitsFile);

        //load kits
        List<Kit> kitsConfig = (List<Kit>) kitConfig.getList("kits",new ArrayList<Kit>());

        for (Kit kit : kitsConfig) {
            kits.put(kit.getName(),kit);
        }

        //load contexts
        if (loadContext) {
            contextConfig = YamlConfiguration.loadConfiguration(dbContextFile); 

            ConfigurationSection players = contextConfig.getConfigurationSection("contexts");
            if (players != null) {
                for (String player : players.getKeys(false)) {
                    for (KitContext kc : (List<KitContext>)players.getList(player,new ArrayList<KitContext>())) {
                        KitContext.addContext(player, kc);
                    }
                }
            }
        }
    }

    /**
     * Save kit Data
     */
    public void saveData() {

        List<Kit> kit = new ArrayList<Kit>();
        for (Kit k : kits.values()) {
            kit.add(k);
        }
        kitConfig.set("kits",kit);

        for (Entry<String, Set<KitContext>> e : KitContext.getAllContexts().entrySet()) {
            contextConfig.set(e.getKey(), e.getValue());
        }

        try {
            kitConfig.save(dbKitsFile);
            contextConfig.save(dbContextFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void convertKitDb(File file) {
        Scanner in;
        try {
            in = new Scanner(file);

            log.info("Converting old kit database");
            while (in.hasNextLine()) {
                //# Name;ID Amount;ID Amount;ID amount (etc)[;-cooldown]
                String line = in.nextLine();
                //# Name;ID Amount;ID Amount;ID amount (etc)[;-cooldown]
                if (!line.startsWith("#")) {
                    String[] part = line.split("\\;");
                    if (part.length > 1) {
                        String name = part[0];
                        int timer = 0;
                        int items = part.length;
                        if (part[part.length-1].startsWith("-")) {
                            timer = 0 - Integer.parseInt(part[part.length-1]);
                            items = items - 1;
                        }
                        Kit newKit = new Kit(name,timer);
                        for (int i = 1; i < items ; i++) {
                            String[] item = part[i].split(" ");
                            int id = 1;
                            short dam = 0;
                            if (item[0].split("\\:").length==2) {
                                id = Integer.parseInt(item[0].split("\\:")[0]);
                                dam = Short.parseShort(item[0].split("\\:")[1]);
                            } else {
                                id = Integer.parseInt(item[0]);
                            }

                            int count = 1;
                            if (item.length==2) {
                                count = Integer.parseInt(item[1]);
                            }
                            newKit.addItem(new ItemStack(id, count, dam));
                        }
                        addKit(newKit);
                    }
                }
            }

            in.close();
            log.info("Conversion complete");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        saveData();
    }

    /**
     * Add a kit
     * @param kit
     */
    public void addKit(Kit kit) {
        kits.put(kit.getName(), kit);
    }

    /**
     * get a kit 
     * @param name
     * @return
     */
    public Kit getKit(String name) {
        return kits.get(name);
    }

    public Collection<Kit> getKits() {
        return kits.values();
    }

    public void removeKit(String name) {
        kits.remove(name);
    }

    @Override
    public boolean enable(Log log,EscapePlug plugin) {
        this.log = log;
        this.plugin = plugin;
        dbKitsFile = new File(plugin.getDataFolder(),"kits.yml");
        dbContextFile = new File(plugin.getDataFolder(),"kits-players.yml");

        ConfigurationSerialization.registerClass(Kit.class);
        ConfigurationSerialization.registerClass(KitContext.class);

        loadData();

        plugin.getComponentManager().registerCommands(new KitCommand(this));
        plugin.getComponentManager().registerCommands(new KitAdminCommand(this));

        //try conversion
        File file = new File(plugin.getDataFolder(),"kits.txt");
        if (file.exists()) {
            convertKitDb(file);
        }

        loadData();

        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void reloadConfig() {
        // TODO Auto-generated method stub

    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub

    }
}
