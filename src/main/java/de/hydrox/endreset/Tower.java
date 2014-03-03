package de.hydrox.endreset;

import java.util.Collection;
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

    /**
     * Creates a tower from the serialized config file.
     *
     * @param map the serialization map
     */
     public Tower(Map<String, Object> map) {
        this.size = (Integer) map.get("size");
        this.height = (Integer) map.get("height");
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
        double xCrystal = xLoc + .5;
        double zCrystal = zLoc + .5;

        // obsidian - there has got to be a better algorithm for this - Tul
        // construct the 3x3, also finishes the size 3
        if (size <= 5) {
            createObsidianSquare(cur, 1);
        }
        // finish off the size 5, edge length is 3
        if (size == 5) {
            createObsidianEdges(cur, 1, 2);
        }
        // construct the 5x5
        if (size > 5) {
            createObsidianSquare(cur, 2);
        }
        // finish off the size 7, edge length is 3
        if (size == 7) {
            createObsidianEdges(cur, 1, 3);
        }
        // finish off the size 9, edge length 5 and 3
        if (size == 9) {
            createObsidianEdges(cur, 2, 3);
            createObsidianEdges(cur, 1, 4);
        }

        // bedrock and fire
        cur.setX(xLoc);
        cur.setY(yLoc + height);
        cur.setZ(zLoc);
        world.getBlockAt(cur).setType(Material.BEDROCK);
        cur.setY(yLoc + height + 1);
        world.getBlockAt(cur).setType(Material.FIRE);

        // crystal - don't add one if it already exists
        cur.setX(xCrystal);
        cur.setY(yLoc + height);
        cur.setZ(zCrystal);
        Collection<EnderCrystal> crystals = world.getEntitiesByClass(EnderCrystal.class);
        for (EnderCrystal crystal : crystals) {
            Location cLoc = crystal.getLocation();
            if ((cLoc.getX() == cur.getX()) && ((cLoc.getY() - 1) == cur.getY()) && (cLoc.getZ() == cur.getZ())) {
                return;  // already a crystal so we're done
            }
        }
        world.spawn(cur, EnderCrystal.class);
    }

    private void createObsidianEdges(Location loc, int offset1, int offset2) {
        // top and bottom edges
        for (int x = xLoc - offset1; x <= xLoc + offset1; x++) {
            loc.setX(x);
            loc.setZ(zLoc - offset2);
            createObsidianColumn(loc);
            loc.setZ(zLoc + offset2);
            createObsidianColumn(loc);
        }
        // left and right edges
        for (int z = zLoc - offset1; z <= zLoc + offset1; z++) {
            loc.setX(xLoc - offset2);
            loc.setZ(z);
            createObsidianColumn(loc);
            loc.setX(xLoc + offset2);
            createObsidianColumn(loc);
        }
    }

    private void createObsidianSquare(Location loc, int offset) {
        for (int x = xLoc - offset; x <= xLoc + offset; x++) {
            for (int z = zLoc - offset; z <= zLoc + offset; z++) {
                loc.setX(x);
                loc.setZ(z);
                createObsidianColumn(loc);
            }
        }
    }

    private void createObsidianColumn(Location column) {
        for (int h = 0; h < height; h++) {
            column.setY(yLoc + h);
            column.getWorld().getBlockAt(column).setType(Material.OBSIDIAN);
        }
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("size", size);
        map.put("height", height);
        map.put("x", xLoc);
        map.put("y", yLoc);
        map.put("z", zLoc);

        return map;
    }
}
