package com.tehbeard.areablock;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.util.Vector;


/**
 * Represents an area that will be sealed 
 * 
 * @author James
 *
 */
public class Gate {

    private Cuboid area;
    public final void setArea(Cuboid area) {
        this.area = area;
    }

    private Material close = Material.LAPIS_BLOCK;
    private Material open = Material.AIR;
    public Gate(){
        area = new Cuboid();
    }
    public Gate(String config){
        close = Material.getMaterial(config.split("\\|")[0]);
        open  = Material.getMaterial(config.split("\\|")[1]);
        area = new Cuboid();
        area.setCuboid(config.split("\\|")[2]);
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
    
    public void close(){
        setArea(close);
    }
    
    
    public final Cuboid getArea() {
        return area;
    }



    public final void setClose(Material close) {
        this.close = close;
    }



    public final void setOpen(Material open) {
        this.open = open;
    }



    public void open(){
        setArea(open);
    }
    
    public String toString(){
        return close + "|" + open + "|" + area.toString();
    }
    
    public int getSize(){
        return area.size();
    }
}
