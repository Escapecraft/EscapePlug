package me.tehbeard.utils.cuboid;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;


/**
 * handles checking a cuboid cache against a player's location
 * @author Tehbeard
 *
 */
public class ChunkCache<T> {

	private HashMap<String,HashSet<CuboidEntry<T>>> cache;

	public ChunkCache(){
	    clearCache();
	}
	/**
	 * Clears the currently loaded cache
	 */
	public void clearCache(){
		cache = new HashMap<String,HashSet<CuboidEntry<T>>>();
	}
	
	/**
	 * Create an entry for the cuboid cache
	 * @param cuboid cuboid used to trigger this.
	 * @param entry Entry to add to this cuboid
	 */
	public void addEntry(Cuboid cuboid,T entry){
	    CuboidEntry<T> cuboidEntry = new CuboidEntry<T>(cuboid,entry);
	    for(String tag: cuboid.getChunks()){
	        if(!cache.containsKey(tag)){
	            cache.put(tag, new HashSet<CuboidEntry<T>>());
	        }
	        cache.get(tag).add(cuboidEntry);
	    }
	    //ADD ITEM TO CACHE
	}

	/**
	 * Return a list of entries that the player is inside
	 * @param player player to check against
	 * @return a list of entries
	 */
	public List<CuboidEntry<T>> getEntries(Player player){
	    return getEntries(player.getLocation()); 
	}
	/**
	 * Return a list of entries at that location
	 * @param location location to check
	 * @return a list of entries
	 */
	public List<CuboidEntry<T>> getEntries(Location location){
		String world = location.getWorld().getName();
		String cx = "" + location.getBlockX() / 16;
		String cz = "" + location.getBlockZ() / 16;
		List<CuboidEntry<T>> ret = new ArrayList<CuboidEntry<T>>();

		if(cache.containsKey(""+world+","+cx+","+cz)){
			//BeardAch.printDebugCon("Chunk cache found records, checking....");
			for(CuboidEntry<T> entry : cache.get(""+world+","+cx+","+cz)){
			    if(entry.getCuboid().isInside(location)){
			        ret.add(entry);
			    }
			}
		}
		
		return ret;
	}
	
	
}
