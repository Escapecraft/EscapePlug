package net.escapecraft.escapePlug;

import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import de.hydrox.antiSlime.SlimeDamageListener;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");

	public void onEnable() {
		log.info("[EscapePlug] EscapePlug loaded");

		//start loading AntiSlime
		SlimeDamageListener listener = new SlimeDamageListener();
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_TARGET,listener,Event.Priority.Highest,this);
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE,listener,Event.Priority.Highest,this);
		//finished loading AntiSlime
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		return true;
	}

	public void onDisable() {
		log.info("[EscapePlug] EscapePlug unloaded");
	}
}
