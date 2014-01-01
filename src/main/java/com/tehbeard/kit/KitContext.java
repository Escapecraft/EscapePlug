package com.tehbeard.kit;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs("KitContext")
public class KitContext implements ConfigurationSerializable {

    private String kit;
    private long timeIn;
    
    private static HashMap<String,Set<KitContext>> contexts = new HashMap<String,Set<KitContext>>();
    
    public KitContext(Map<String, Object> m) {
        this((String)m.get("name"), (Long)m.get("cooldown"));
    }

    public KitContext(String kit, long timeIn) {
        this.kit = kit;
        this.timeIn = timeIn;
    }
    
    public String getName() {
        return kit;
    }
    
    public long time() {
        return timeIn;
    }

    public boolean canUseTime() {
        return (timeIn < System.currentTimeMillis());
    }
    
    public long timeTill() {
        return (timeIn - System.currentTimeMillis()) / 1000L;
    }

    public static Set<KitContext> getContexts(String player) {
        if (!contexts.containsKey(player)) {
            contexts.put(player,new HashSet<KitContext>());
        }
        Iterator<KitContext> it = contexts.get(player).iterator();
        
        //self garbage collect, only contexts that are needed should be kept
        while (it.hasNext()) {
            if (it.next().canUseTime()) {
                it.remove();
            }
        }
        return contexts.get(player);
    }
    
    public static void addContext(String player,KitContext context) {
        if (!contexts.containsKey(player)) {
            contexts.put(player,new HashSet<KitContext>());
        }
        contexts.get(player).add(context);
    }
    
    public static  HashMap<String,Set<KitContext>> getAllContexts() {
        return contexts;
    }

    public Map<String, Object> serialize() {
        Map<String,Object> m = new HashMap<String, Object>();
        m.put("name", kit);
        m.put("cooldown", timeIn);

        return m;
    }
}
