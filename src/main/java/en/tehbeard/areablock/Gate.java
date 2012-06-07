package en.tehbeard.areablock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;

import me.tehbeard.utils.cuboid.Cuboid;

/**
 * Represents an area that will be sealed 
 * 
 * @author James
 *
 */
public class Gate {

    private Cuboid area;
    private Material material = Material.LAPIS_BLOCK;

    public Gate(String config){
        area = new Cuboid();
        area.setCuboid(config);
    }
    public Gate(Cuboid cuboid){
        area = cuboid;
    }
    
    
    private void setArea(Material m){
        Vector min = area.getCorners()[0];
        Vector max = area.getCorners()[1];
        World w = Bukkit.getWorld(area.getWorld());
        for(int x = min.getBlockX();x<=max.getBlockX();x++){
            for(int y = min.getBlockY();y<=max.getBlockY();y++){
                for(int z = min.getBlockZ();z<=max.getBlockZ();z++){
                    w.getBlockAt(x, y, z).setType(m);
                }
            }   
        }
    }
    /**
     * Fill area with the block
     */
    public void close(){
        setArea(material);
    }
    
    public void open(){
        setArea(Material.AIR);
    }
}
