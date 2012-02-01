package net.escapecraft.component;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.plugin.Plugin;
import org.tulonsae.mc.util.Log;


/**
 * Represents an abstract component of EscapePlug
 * @author james
 *
 */
public abstract class AbstractComponent {

	/**
	 * Called upon being enabled
	 * @param plugin instance of EscapePlug
	 */
	public abstract boolean enable(Log log,EscapePlug plugin);
	
	/**
	 * Called during onDisable() 
	 */
	public abstract void disable();
	
	/**
	 * Called to tell the plugin to re-check it's config
	 */
	public void reloadConfig(){};
	
	
}
