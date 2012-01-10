package net.escapecraft.escapePlug.component;

import java.util.Set;

import org.bukkit.command.CommandExecutor;
import org.tulonsae.mc.util.Log;

import net.escapecraft.escapePlug.EscapePlug;

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
	public abstract void tidyUp();
	
	/**
	 * Called to tell the plugin to recheck it's config
	 */
	public abstract void reloadConfig();
	
	
}
