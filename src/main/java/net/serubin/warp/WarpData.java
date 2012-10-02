package net.serubin.warp;

import java.util.Date;

import org.bukkit.Location;

public class WarpData {

    private String name;
    private Location loc;
    private String date;
    private String user;

    public WarpData(String name, Location loc, String date, String user) {
        this.name = name;
        this.date = date;
        this.loc = loc;
        this.user = user;
    }

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
}
