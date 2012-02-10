package net.escapecraft.escapePlug;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import me.tehbeard.BeardStat.BeardStat;
import me.tehbeard.BeardStat.containers.PlayerStatManager;
import net.escapecraft.component.ComponentManager;
import net.serubin.hatme.HatCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.tulonsae.afkbooter.AfkBooter;
import org.tulonsae.mc.util.Log;

import uk.co.oliwali.HawkEye.HawkEye;
import de.hydrox.antiSlime.AntiSlimeComponent;
import de.hydrox.blockalert.AbstractListener;
import de.hydrox.blockalert.AlertListener;
import de.hydrox.blockalert.AlertListenerHawkEye;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;
import de.hydrox.bukkit.timezone.TimezoneComponent;
import de.hydrox.lockdown.LockdownComponent;
import de.hydrox.mobcontrol.MobControlListener;
import de.hydrox.who.WhoCommandComponent;
import en.tehbeard.endernerf.EndernerfComponent;
import en.tehbeard.gamemode.GameModeToggleComponent;
import en.tehbeard.mentorTeleport.MentorTeleportComponent;
import en.tehbeard.pigjouster.PigJousterComponent;
import en.tehbeard.reserve.ReserveComponent;

public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	private ComponentManager componentManager;
	private DroxPermsAPI droxPermsAPI = null;
	private boolean hawkEyeLoaded = false;
	
	public DroxPermsAPI getDroxPermsAPI() {
        return droxPermsAPI;
    }

    public PlayerStatManager getBeardStatManager() {
        return beardStatManager;
    }

    private PlayerStatManager beardStatManager = null;
        private AfkBooter afkBooter = null;

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
		
		HawkEye hawkEye = (HawkEye)this.getServer().getPluginManager().getPlugin("HawkEye");
		if (hawkEye != null) {
			hawkEyeLoaded = true;
		}
		
		//start the component manager
		componentManager = new ComponentManager(this, new Log("EscapePlug"));
		componentManager.addComponent(MentorTeleportComponent.class);
		componentManager.addComponent(ReserveComponent.class);
		componentManager.addComponent(GameModeToggleComponent.class);
		componentManager.addComponent(TimezoneComponent.class);
		componentManager.addComponent(LockdownComponent.class);
		componentManager.addComponent(EndernerfComponent.class);
		componentManager.addComponent(PigJousterComponent.class);
		componentManager.addComponent(WhoCommandComponent.class);
		componentManager.addComponent(AntiSlimeComponent.class);
		//start components
		componentManager.startupComponents();

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
			getCommand("hat").setExecutor(Hat);
			getCommand("unhat").setExecutor(Hat);
			log.info("[EscapePlug] loaded hatMe version " + hatversion);
		}

		

                // start loading afkbooter
		if (getConfig().getBoolean("plugin.afkbooter.enabled", true)) {
			log.info("[EscapePlug] loading AfkBooter");
                        afkBooter = new AfkBooter(self);
			//finished loading afkbooter
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

		//start loading blockalert
		if (getConfig().getBoolean("plugin.blockalert.enabled", true)) {
			Map<String, List<Integer>> notifyBlockBreak = new HashMap<String, List<Integer>>();
			Map<String, List<Integer>> notifyBlockPlace = new HashMap<String, List<Integer>>();
			Set<String> worlds = getConfig().getConfigurationSection("plugin.blockalert.worlds.break.").getKeys(false);
			for (String world : worlds) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Load Break Alerts for World " + world);
				List<Integer> blockBreakList = getConfig().getIntegerList("plugin.blockalert.worlds.break." + world);
				notifyBlockBreak.put(world, blockBreakList);
			}
			worlds = getConfig().getConfigurationSection("plugin.blockalert.worlds.place.").getKeys(false);
			for (String world : worlds) {
				Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Load Placement Alerts for World " + world);
				List<Integer> blockPlaceList = getConfig().getIntegerList("plugin.blockalert.worlds.place." + world);
				notifyBlockPlace.put(world, blockPlaceList);
			}
			log.info("[EscapePlug] loading BlockAlert");
			AbstractListener alertListener = null;
			if (hawkEyeLoaded) {
				alertListener = new AlertListenerHawkEye(notifyBlockBreak, notifyBlockPlace);				
			} else {
				alertListener = new AlertListener(notifyBlockBreak, notifyBlockPlace);
			}
			this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_BREAK, alertListener, Event.Priority.Monitor, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.BLOCK_PLACE, alertListener, Event.Priority.Monitor, this);
			//finished loading blockalert
		}

		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
                if (afkBooter != null) {
                        afkBooter.tidyUp();
			log.info("[EscapePlug] AfkBooter unloaded");
                }
        getComponentManager().disableComponents();
		self = null;
		
		log.info("[EscapePlug] EscapePlug unloaded");
	}

	public static void printCon(String line) {
		log.info("[EscapePlug] " + line);
	}
	
	/**
	 * Return the component manager
	 * @return
	 */
	public ComponentManager getComponentManager(){
		return componentManager;
	}
}
