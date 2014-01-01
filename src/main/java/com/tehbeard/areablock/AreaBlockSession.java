package com.tehbeard.areablock;


import org.bukkit.util.Vector;

public class AreaBlockSession {

    Vector p1;
    Vector p2;
    boolean toolActive;
    
    public final Vector getP1() {
        return p1;
    }
    public final void setP1(Vector p1) {
        this.p1 = p1;
    }
    public final Vector getP2() {
        return p2;
    }
    public final void setP2(Vector p2) {
        this.p2 = p2;
    }
    

    public final boolean isToolActive() {
        return toolActive;
    }
    public final void setToolActive(boolean toolActive) {
        this.toolActive = toolActive;
    }
    
    public int size(){
        if(p1 == null || p2 == null){return 0;}
        Vector v3 = Vector.getMaximum(p1, p2).subtract(Vector.getMinimum(p1, p2));

        return (v3.getBlockX()+1) * (v3.getBlockY()+1) *  (v3.getBlockZ()+1); 
        
    }
    
    public Cuboid makeCuboid(String world){
        if(p1 == null || p2 == null){return null;}
        String c = world + ":" +  
                p1.getBlockX() + ":" +
                p1.getBlockY() + ":" +
                p1.getBlockZ() + ":" +
                p2.getBlockX() + ":" +
                p2.getBlockY() + ":" +
                p2.getBlockZ() + ":0";
        Cuboid cc = new Cuboid();
        cc.setCuboid(c);
        return cc;
    }
}
