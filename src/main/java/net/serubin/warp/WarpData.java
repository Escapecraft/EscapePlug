package net.serubin.warp;

import java.security.Timestamp;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public class WarpData {

    private String name;
    private Location loc;
    private java.sql.Timestamp  date;
    private String user;

    public WarpData(String name, Location loc, java.sql.Timestamp date,
            String user) {
        this.name = name;
        this.date = date;
        this.loc = loc;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public Location getLoc() {
        return loc;
    }

    public java.sql.Timestamp  getDate() {
        return date;
    }

    public String getUser() {
        return user;

    }
}
