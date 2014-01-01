package com.tehbeard.kit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

/**
 * Represents a kit
 * @author james
 *
 */
@SerializableAs("Kit")
public class Kit implements ConfigurationSerializable {
    private String name;
    private List<ItemStack> items;
    private int timer;
    
    /**
     * Create an empty kit
     * @param name
     * @param timer
     */
    public Kit(String name, int timer) {
        this(name, timer, new ArrayList<ItemStack>());
    }
    
    @SuppressWarnings("unchecked")
    public Kit(Map<String,Object> m) {
        name = (String) m.get("name");
        timer = (Integer) m.get("cooldown");
        items = (List<ItemStack>) m.get("items");
    }

    /**
     * Create a kit from an existing list
     * @param name
     * @param timer
     * @param items
     */
    public Kit(String name, int timer, List<ItemStack> items) {
        this.name = name;
        this.items = items;
        this.timer = timer;
    }
    
    /**
     * get name of kit
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * get the items in a kit
     * @return
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * 
     * @return
     */
    public int getCooldown() {
        return timer;
    }

    /**
     * adds an item to a kit
     * @param item
     */
    public void addItem(ItemStack item) {
        items.add(item);
    }
    
    /**
     * returns the size of the kit (number of items)
     * @return
     */
    public int size() {
        return items.size();
    }
    
    /**
     * returns string of a kit
     */
    public String toString() {
        String ret  = "kit: " + name + "\n";
        for (ItemStack item : items) {
            ret += item.toString() + "\n";
        }
        
        return ret;
    }

    /**
     * Returns if a permissible can use this kit
     * @param permissible
     * @return
     */
    public boolean canUsePerm(Permissible permissible) {
        return permissible.hasPermission("escapeplug.kit.kits.*") ||
            permissible.hasPermission("escapeplug.kit.kits."+name);
    }
    
    /**
     * Give the kit to a player
     * @param player
     * @return
     */
    public Result giveKit(Player player) {
        return giveKit(player, (
                player.hasPermission("escapeplug.kit.kits.*.nocooldown") ||
                player.hasPermission("escapeplug.kit.kits." + name + ".nocooldown")
                ), false);
    }
    
    /**
     * give a kit to a player
     * @param player player to give kit to
     * @param overrideContexts if set to true, any existing context (timer) is ignored
     * @return
     */
    public Result giveKit(Player player, boolean overrideContexts, boolean overridePermissions) {

        if (!overrideContexts) {
            for (KitContext kc : KitContext.getContexts(player.getName())) {
                if (kc.getName().equals(name)) {
                    if (!kc.canUseTime()) {
                        return Result.TIMER;
                    }
                }
            }
        }
        if (!canUsePerm(player) && !overridePermissions) {
            return Result.PERM;
        }
        for (ItemStack item : items) {
            player.getInventory().addItem(item);
        }
        if (!overrideContexts) {
            KitContext newContext = new KitContext(name, System.currentTimeMillis() + (timer * 1000));
            KitContext.addContext(player.getName(),newContext);
        }
        return Result.OK;
    }

    public enum Result {
        OK,
        PERM,
        TIMER
    }

    public Map<String, Object> serialize() {
        Map<String,Object> m = new HashMap<String, Object>();
        m.put("name", name);
        m.put("cooldown", timer);
        m.put("items", items);
        return m;
    }
}
