package de.hydrox.who;

import java.util.Comparator;

import org.bukkit.entity.Player;

import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;

public class PlayerComparator implements Comparator<Player> {
    
    private DroxPermsAPI perms = null;
    
    public PlayerComparator(DroxPermsAPI perms) {
        this.perms = perms;
    }

    public int compare(Player arg0, Player arg1) {
        if (perms != null) {
            int compare = perms.getPlayerGroup(arg0.getName()).compareTo(perms.getPlayerGroup(arg1.getName()));
            if ( compare != 0) {
                return compare;
            }
        }
        return arg0.getName().compareTo(arg1.getName());
    }
}
