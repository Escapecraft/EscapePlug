package me.tehbeard.utils.session;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 * Generic class for storing sessions
 * You can activate killing a session on logout by registering the instance of this class with bukkits event system
 * @author James
 *
 * @param <T>
 */
public class SessionStore<T> implements Listener {

	Map<String,T> sessions;
	
	public SessionStore(){
		sessions = new HashMap<String, T>();
	}
	
	/**
	 * Put a value into the session store
	 * @param player
	 * @param session
	 */
	public void putSession(String player,T session){
		sessions.put(player,session);
	}
	
	/**
	 * returns if the player has a session stored
	 * @param player
	 * @return
	 */
	public boolean hasSession(String player){
		return sessions.containsKey(player);
	}
	
	/**
	 * returns a session
	 * @param player
	 * @return
	 */
	public T getSession(String player){
		return sessions.get(player);
	}
	
		
	@EventHandler
	public void logout(PlayerQuitEvent e){
		sessions.remove(e.getPlayer().getName());
	}
	
}
