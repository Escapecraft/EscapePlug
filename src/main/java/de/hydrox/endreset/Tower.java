package de.hydrox.endreset;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.EnderCrystal;

/**
 * Represents an End Tower.
 */
@SerializableAs("Tower")
public class Tower implements ConfigurationSerializable {

    private Integer xLoc;
    private Integer yLoc;
    private Integer zLoc;
    private Integer size;
    private Integer height;
    private String shape;

    /**
     * Creates a tower from the serialized config file.
     *
     * @param map the serialization map
     */
     public Tower(Map<String, Object> map) {
        this.size = (Integer) map.get("size");
        this.height = (Integer) map.get("height");
        this.shape = (String) map.get("shape");
        this.xLoc = (Integer) map.get("x");
        this.yLoc = (Integer) map.get("y");
        this.zLoc = (Integer) map.get("z");
    }

    /**
     * Restores a tower to its original version.
     *
     * @param world the end world
     */
    public void restore(World world) {
        Location cur = new Location(world, 0, 0, 0);
        double offset = Math.floor((size / 2));
        double xCrystal = xLoc + .5;
        double zCrystal = zLoc + .5;
        int minCorner = 0;
        int maxCorner = size - 1;

        // obsidian
        for (int x = 0; x < size; x++) {
            for (int z = 0; z < size; z++) {
                if (("round".equalsIgnoreCase(shape)) && (
                        (x == minCorner && z == minCorner) ||
                        (x == maxCorner && z == maxCorner) ||
                        (x == minCorner && z == maxCorner) ||
                        (x == maxCorner && z == minCorner)
                        )) {
                    continue;
                }
                cur.setX(xLoc + x - offset);
                cur.setZ(zLoc + z - offset);
                for (int h = 0; h < height; h++) {
                    cur.setY(yLoc + h);
                    world.getBlockAt(cur).setType(Material.OBSIDIAN);
                }
            }
        }

        // crystal
        cur.setX(xLoc);
        cur.setY(yLoc + height);
        cur.setZ(zLoc);
        world.getBlockAt(cur).setType(Material.BEDROCK);
        cur.setY(yLoc + height + 1);
        world.getBlockAt(cur).setType(Material.FIRE);
        cur.setX(xCrystal);
        cur.setY(yLoc + height);
        cur.setZ(zCrystal);
        world.spawn(cur, EnderCrystal.class);
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("size", size);
        map.put("height", height);
        map.put("shape", shape);
        map.put("x", xLoc);
        map.put("y", yLoc);
        map.put("z", zLoc);

        return map;
    }
}
