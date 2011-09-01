package net.escapecraft.escapePlug;

import java.util.HashSet;
import java.util.Iterator;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.timezone.TimezoneCommands;
import en.tehbeard.mentorTeleport.MentorTeleport;
import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	
	private HashSet<IEscapePlugCommandHandler> commandHandlers = new HashSet<IEscapePlugCommandHandler>();

	public void onEnable() {
		log.info("[EscapePlug] loading EscapePlug");

		//start loading AntiSlime
		SlimeDamageListener slimeDamageListener = new SlimeDamageListener();
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_TARGET, slimeDamageListener, Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, slimeDamageListener, Event.Priority.Normal, this);
		//finished loading AntiSlime
		
		//start loading MentorTeleport
		log.info("[EscapePlug] loading MentorTP");
		commandHandlers.add(new MentorTeleport(this));
		//finished loading MentorTeleport
		
		//start loading PigJouster
		log.info("[EscapePlug] loading PigJouster");
		commandHandlers.add(new PigJouster());
		PigListener pigListener = new PigListener();
		PigPlayerListener pigPlayerListener = new PigPlayerListener();
		this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, pigListener, Event.Priority.Normal, this);
		this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pigPlayerListener, Event.Priority.Normal, this);
		//finished loading PigJouster

		//start loading Timezone
		log.info("[EscapePlug] loading Timezone");
		getCommand("timezone").setExecutor(new TimezoneCommands());
		//finished loading Timezone
		log.info("[EscapePlug] EscapePlug loaded");
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args){
		Iterator<IEscapePlugCommandHandler> iter = commandHandlers.iterator();
		while (iter.hasNext()) {
			IEscapePlugCommandHandler handler = (IEscapePlugCommandHandler) iter.next();
			boolean result = handler.handleCommand(sender, commandLabel, args);
			if (result) {
				return true;
			}
		}
		return false;
	}

	public void onDisable() {
		log.info("[EscapePlug] EscapePlug unloaded");
	}
}
