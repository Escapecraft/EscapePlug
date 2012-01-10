package net.escapecraft.escapePlug.component;

import java.util.Map;

import org.bukkit.command.CommandExecutor;

import net.escapecraft.escapePlug.EscapePlug;

/**
 * Represents an abstract component of EscapePlug
 * @author james
 *
 */
public abstract class AbstractComponent {

	public abstract void enable(EscapePlug plugin);
	
	public abstract void tidyUp();
	
	public abstract void reloadConfig();
	
	public abstract Map<String,CommandExecutor> getCommands();
}
