package net.escapecraft.escapePlug;

import java.io.File;
import java.util.List;
import java.util.logging.Logger;

import net.serubin.hatme.HatmeCommand;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.timezone.TimezoneCommands;
import de.hydrox.lockdown.LockdownCommand;
import de.hydrox.lockdown.LockdownListener;
import en.tehbeard.endernerf.EndernerfListener;
import en.tehbeard.gamemode.GameModeToggle;
import en.tehbeard.mentorTeleport.MentorBack;
import en.tehbeard.mentorTeleport.MentorTeleport;
import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;
import en.tehbeard.reserve.ReserveListener;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	public static EscapePlug self = null;
	//Hatme config variables
	public static boolean rbAllow;
	public static boolean rbOp;
	public static String notAllowedMsg;
	public static List<Integer> rbBlocks;
	
	public void onEnable() {
		self = this;
		log.info("[EscapePlug] loading EscapePlug");

		//load/creates/fixes config
		getConfig().options().copyDefaults(true);
		saveConfig();

		//Starting reserve list
		if(getConfig().getBoolean("plugin.reserve.enabled", true)){
			ReserveListener rl = new ReserveListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN, rl, Event.Priority.Highest, this);
		}

		//start loading AntiSlime
		if(getConfig().getBoolean("plugin.antislime.enabled", true)){
			log.info("[EscapePlug] loading AntiSlime");
			SlimeDamageListener slimeDamageListener = new SlimeDamageListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_TARGET, slimeDamageListener, Event.Priority.Normal, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, slimeDamageListener, Event.Priority.Normal, this);
		} else {
			log.info("[EscapePlug] skipping AntiSlime");
		}
		//finished loading AntiSlime

		//start loading MentorTeleport
		if(getConfig().getBoolean("plugin.mentortp.enabled", true)){
			log.info("[EscapePlug] loading MentorTP");
			getCommand("mentortp").setExecutor(new MentorTeleport(this));
			getCommand("mentorback").setExecutor(new MentorBack(this));
			//finished loading MentorTeleport
		} else {
			log.info("[EscapePlug] skipping MentorTP");
		}

		//start loading PigJouster
		if(getConfig().getBoolean("plugin.pigjoust.enabled", true)){
			log.info("[EscapePlug] loading PigJouster");
			getCommand("pig-active").setExecutor(new PigJouster());
			getCommand("pig-deactive").setExecutor(new PigJouster());
			PigListener pigListener = new PigListener();
			PigPlayerListener pigPlayerListener = new PigPlayerListener();




			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, pigListener, Event.Priority.Normal, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, pigPlayerListener, Event.Priority.Normal, this);
			//finished loading PigJouster
		} else {
			log.info("[EscapePlug] skipping PigJouster");
		}

		//start loading Timezone
		if(getConfig().getBoolean("plugin.timezone.enabled", true)){
			log.info("[EscapePlug] loading Timezone");
			getCommand("timezone").setExecutor(new TimezoneCommands());
			//finished loading Timezone
		} else {
			log.info("[EscapePlug] skipping Timezone");
		}


		//start loading togglemode
		if(getConfig().getBoolean("plugin.togglemode.enabled", true)){
			log.info("[EscapePlug] loading ToggleGameMode");
			getCommand("togglemode").setExecutor(new GameModeToggle());
			//finished loading togglemode
		}

		//start loading endernerf
		if(getConfig().getBoolean("plugin.endernerf.enabled", true)){
			log.info("[EscapePlug] loading enderNerf");
			EntityListener el = new EndernerfListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PICKUP, el, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PLACE, el, Event.Priority.Highest, this);
		
			//finished loading endernerf
		}

		//start loading lockdown
		if(getConfig().getBoolean("plugin.lockdown.enabled", true)){
			log.info("[EscapePlug] loading Emergency Lockdown");
			LockdownListener lockdownListener = new LockdownListener();
			getCommand("lockdown").setExecutor(new LockdownCommand(lockdownListener));
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN, lockdownListener, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, lockdownListener, Event.Priority.Highest, this);
			//finished loading lockdown
		}
		
		//start loading hatMe
		if(getConfig().getBoolean("plugin.hatme.enabled", true)){
			log.info("[EscapePlug] loading hatMe");
			rbBlocks = getConfig().getList("plugin.hatme.allowed");
			rbAllow = getConfig().getBoolean("plugin.hatme.enable");
			notAllowedMsg = getConfig().getString("plugin.hatme.notAllowedMsg");
			rbOp = getConfig().getBoolean("plugin.hatme.opnorestrict");
			
			getCommand("hat").setExecutor(new HatmeCommand());
			getCommand("unhat").setExecutor(new HatmeCommand());
			//finished loading hatMe
		}



		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
		self=null;
		log.info("[EscapePlug] EscapePlug unloaded");
	}

	public static void printCon(String line){
		log.info("[EscapePlug] "+line);
	}
}
