package net.escapecraft.escapePlug;

import java.util.List;
import java.util.logging.Logger;

import me.tehbeard.BeardStat.BeardStat;
import me.tehbeard.BeardStat.containers.PlayerStatManager;
import net.escapecraft.component.ComponentManager;
import net.serubin.hatme.HatCommand;

import org.bukkit.plugin.java.JavaPlugin;
import org.tulonsae.afkbooter.AfkBooter;
import org.tulonsae.mc.util.Log;

import uk.co.oliwali.HawkEye.HawkEye;
import de.hydrox.antiSlime.AntiSlimeComponent;
import de.hydrox.blockalert.BlockAlertComponent;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;
import de.hydrox.bukkit.timezone.TimezoneComponent;
import de.hydrox.lockdown.LockdownComponent;
import de.hydrox.mobcontrol.MobControlComponent;
import de.hydrox.vanish.VanishComponent;
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
		componentManager.addComponent(MobControlComponent.class);
		componentManager.addComponent(BlockAlertComponent.class);
		componentManager.addComponent(VanishComponent.class);
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

	public boolean isHawkEyeLoaded() {
	    return hawkEyeLoaded;
	}
}
