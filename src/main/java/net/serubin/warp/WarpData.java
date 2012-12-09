package net.serubin.warp;

import org.bukkit.Location;

public class WarpData {

    private String name;
    private Location loc;
    private float yaw;
    private float pitch;
    private String date;
    private String user;

    public WarpData(String name, Location loc, String date, String user,
            float yaw, float pitch) {
        this.name = name;
        this.date = date;
        this.loc = loc;
        this.user = user;
        // Yaw - pitch work around
        this.yaw = yaw;
        this.pitch = pitch;
    }

    // DONE yaw pitch work around - save as a seprate data point in WarpData
    // then apply to location on teleport, DONT SAVE WITH YAW & PITCH
    /**
     * get warp name
     * 
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * get warp location
     * 
     * @return location
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * get warp creation date
     * 
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * get warp creator
     * 
     * @return user
     */
    public String getUser() {
        return user;

    }

    /**
     * get warp yaw
     * 
     * @return yaw
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * get warp pitch
     * 
     * @return pitch
     */
    public float getPitch() {
        return pitch;
    }

    public String toString() {
        return "[" + name + ", " + loc.getWorld() + ", " + loc.getBlockX()
                + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ", " + user
                + ", " + date + "]";
    }
}
