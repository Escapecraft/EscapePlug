package net.escapecraft.escapePlug;

import java.util.List;
import java.util.logging.Logger;

import me.tehbeard.BeardStat.BeardStat;
import me.tehbeard.BeardStat.containers.PlayerStatManager;
import net.escapecraft.component.ComponentManager;
import net.serubin.hatme.AdminCommand;
import net.serubin.hatme.HatCommand;


import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.java.JavaPlugin;
import org.tulonsae.mc.util.Log;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;
import de.hydrox.bukkit.timezone.TimezoneCommands;
import de.hydrox.lockdown.LockdownCommand;
import de.hydrox.lockdown.LockdownListener;
import de.hydrox.mobcontrol.MobControlListener;
import de.hydrox.who.WhoCommand;
import en.tehbeard.endernerf.EndernerfListener;
import en.tehbeard.gamemode.GameModeToggle;
import en.tehbeard.mentorTeleport.MentorTeleportComponent;
import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;
import en.tehbeard.reserve.ReserveComponent;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	private ComponentManager componentManager;
	private DroxPermsAPI droxPermsAPI = null;
	private PlayerStatManager beardStatManager = null;

	public static EscapePlug self = null;

	public void onEnable() {
		self = this;
		
		
		log.info("[EscapePlug] loading EscapePlug");
		

		//load/creates/fixes config
		getConfig().options().copyDefaults(true);
		saveConfig();

		DroxPerms droxPerms = ((DroxPerms) this.getServer().getPluginManager().getPlugin("DroxPerms"));
		if (droxPerms != null) {
			droxPermsAPI = droxPerms.getAPI();
		}

		BeardStat beardStat = ((BeardStat) this.getServer().getPluginManager().getPlugin("BeardStat"));
		if (beardStat != null) {
			beardStatManager = beardStat.getStatManager();
		}
		
		
		//start the component manager
		componentManager = new ComponentManager(this, new Log("EscapePlug"));
		componentManager.addComponent(MentorTeleportComponent.class);
		componentManager.addComponent(ReserveComponent.class);
		//start components
		componentManager.startupComponents();

		

		//start loading AntiSlime
		if (getConfig().getBoolean("plugin.antislime.enabled", true)) {
			log.info("[EscapePlug] loading AntiSlime");
			SlimeDamageListener slimeDamageListener = new SlimeDamageListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_TARGET, slimeDamageListener, Event.Priority.Normal, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENTITY_DAMAGE, slimeDamageListener, Event.Priority.Normal, this);
		} else {
			log.info("[EscapePlug] skipping AntiSlime");
		}
		//finished loading AntiSlime


		//start loading PigJouster
		if (getConfig().getBoolean("plugin.pigjoust.enabled", true)) {
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
		if (getConfig().getBoolean("plugin.timezone.enabled", true)) {
			log.info("[EscapePlug] loading Timezone");
			getCommand("timezone").setExecutor(new TimezoneCommands());
			//finished loading Timezone
		} else {
			log.info("[EscapePlug] skipping Timezone");
		}

		//start loading togglemode
		if (getConfig().getBoolean("plugin.togglemode.enabled", true)) {
			log.info("[EscapePlug] loading ToggleGameMode");
			getCommand("togglemode").setExecutor(new GameModeToggle());
			//finished loading togglemode
		}

		//start loading endernerf
		if (getConfig().getBoolean("plugin.endernerf.enabled", true)) {
			log.info("[EscapePlug] loading enderNerf");
			EntityListener el = new EndernerfListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PICKUP, el, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PLACE, el, Event.Priority.Highest, this);

			//finished loading endernerf
		}

		//start loading lockdown
		if (getConfig().getBoolean("plugin.lockdown.enabled", true)) {
			log.info("[EscapePlug] loading Emergency Lockdown");
			LockdownListener lockdownListener = new LockdownListener(this);
			getCommand("lockdown").setExecutor(new LockdownCommand(lockdownListener));
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_LOGIN, lockdownListener, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.PLAYER_JOIN, lockdownListener, Event.Priority.Highest, this);
			//finished loading lockdown
		}

		//start loading hatMe
		if (getConfig().getBoolean("plugin.hatme.enabled", true)) {
			log.info("[EscapePlug] loading hatMe");

		List<Integer> rbBlocks = getConfig().getIntegerList("plugin.hatme.allowed");
			boolean rbAllow = getConfig().getBoolean("plugin.hatme.enable");
			String notAllowedMsg = getConfig().getString("plugin.hatme.notAllowedMsg");
			boolean rbOp = getConfig().getBoolean("plugin.hatme.opnorestrict");
			String hatversion = getConfig().getString("plugin.hatme.hatversion");

			//construct command and assign to /hat and /unhat
			HatCommand Hat = new HatCommand(rbBlocks, rbAllow, notAllowedMsg, rbOp);
			AdminCommand Admin = new AdminCommand(rbBlocks, rbAllow, notAllowedMsg,
					rbOp);
			// Permission Perm = new Permission(rbBlocks, rbAllow,
			// notAllowedMsg, rbOp);
			// Set command classes
			getCommand("hat").setExecutor(Hat);
			getCommand("unhat").setExecutor(Hat);
			getCommand("hadmin").setExecutor(Admin);
			log.info("[EscapePlug] loaded hatMe version " + hatversion);
		}

		//start loading who
		if (getConfig().getBoolean("plugin.who.enabled", true)) {
			log.info("[EscapePlug] loading Who");
			getCommand("who").setExecutor(new WhoCommand(droxPermsAPI, beardStatManager));
			//finished loading who
		}

		//start loading mobcontrol
		if (getConfig().getBoolean("plugin.mobcontrol.enabled", true)) {
			log.info("[EscapePlug] loading MobControl");
			List<String> worlds = getConfig().getStringList("plugin.mobcontrol.worlds");
			List<String> mobs = getConfig().getStringList("plugin.mobcontrol.mobs");
			MobControlListener mobControlListener = new MobControlListener(worlds, mobs);
			this.getServer().getPluginManager().registerEvent(Event.Type.CREATURE_SPAWN, mobControlListener, Event.Priority.Normal, this);
			//finished loading mobcontrol
		}

		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
		self = null;
		log.info("[EscapePlug] EscapePlug unloaded");
	}

	public static void printCon(String line) {
		log.info("[EscapePlug] " + line);
	}
	
	public ComponentManager getComponentManager(){
		return componentManager;
	}
}
