package net.escapecraft.escapePlug;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.config.Configuration;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.timezone.TimezoneCommands;
import en.tehbeard.cartCollect.CartCollectListener;
import en.tehbeard.endernerf.EndernerfListener;
import en.tehbeard.gamemode.GameModeToggle;
import en.tehbeard.mentorTeleport.MentorTeleport;
import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;
import en.tehbeard.quickCraft.quickCraft;
import en.tehbeard.reserve.ReserveListener;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	private Configuration config = null;
	public static EscapePlug self = null;
	public void onEnable() {
		self = this;
		log.info("[EscapePlug] loading EscapePlug");

		//load config
		loadConfig();

		//Starting reserve list
		if(config.getBoolean("plugin.reserve.enabled",true)){
			ReserveListener rl = new ReserveListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN, rl, Event.Priority.Highest, this);
		}

		//start loading AntiSlime
		if(config.getBoolean("plugin.antislime.enabled",true)){
			log.info("[EscapePlug] loading AntiSlime");
			SlimeDamageListener slimeDamageListener = new SlimeDamageListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_TARGET, slimeDamageListener, Event.Priority.Normal, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, slimeDamageListener, Event.Priority.Normal, this);
		} else {
			log.info("[EscapePlug] skipping AntiSlime");
		}
		//finished loading AntiSlime

		//start loading MentorTeleport
		if(config.getBoolean("plugin.mentortp.enabled",true)){
			log.info("[EscapePlug] loading MentorTP");
			getCommand("mentortp").setExecutor(new MentorTeleport(this));
			//finished loading MentorTeleport
		} else {
			log.info("[EscapePlug] skipping MentorTP");
		}

		//start loading PigJouster
		if(config.getBoolean("plugin.pigjoust.enabled",true)){
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


		//start collect cart 
		if(config.getBoolean("plugin.collectcart.enabled",false)){
			log.info("[EscapePlug] Collect Cart enabled");
			this.getServer().getPluginManager().registerEvent(Event.Type.VEHICLE_MOVE, new CartCollectListener(), Event.Priority.Normal, this);
		}
		//finish collect cart


		//start quick craft 
		if(config.getBoolean("plugin.quickcraft.enabled",false)){
			log.info("[EscapePlug] QuickCraft enabled");
			quickCraft.enable(config.getNode("quickcraft.config"));
		}
		//finish quickcraft



		//start loading Timezone
		if(config.getBoolean("plugin.timezone.enabled",true)){
			log.info("[EscapePlug] loading Timezone");
			getCommand("timezone").setExecutor(new TimezoneCommands());
			//finished loading Timezone
		} else {
			log.info("[EscapePlug] skipping Timezone");
		}


		//start loading togglemode
		if(config.getBoolean("plugin.togglemode.enabled",true)){
			log.info("[EscapePlug] loading ToggleGameMode");
			getCommand("togglemode").setExecutor(new GameModeToggle());
			//finished loading togglemode
		}

		//start loading endernerf
		if(config.getBoolean("plugin.endernerf.enabled",true)){
			log.info("[EscapePlug] loading enderNerf");
			EntityListener el = new EndernerfListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PICKUP, el, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PLACE, el, Event.Priority.Highest, this);

			//finished loading endernerf
		}



		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
		self=null;
		log.info("[EscapePlug] EscapePlug unloaded");
	}

	private void loadConfig(){
		File f = new File(getDataFolder(),"Config.yml");
		config = new Configuration(f);
		if(!f.exists()){
			config.setProperty("plugin.mentortp.enabled",true);
			config.setProperty("plugin.pigjoust.enabled",true);
			config.setProperty("plugin.timezone.enabled",true);
			config.setProperty("plugin.antislime.enabled",true);
			config.setProperty("plugin.collectcart.enabled",false);
			config.setProperty("plugin.quickcraft.enabled",false);
			config.setProperty("plugin.togglemode.enabled",false);
			config.setProperty("plugin.endernerf.enabled",true);
			config.save();
		}
		config.load();

	}

	public static void printCon(String line){
		log.info("[EscapePlug] "+line);
	}
}
