package net.escapecraft.escapeplug;

import org.bukkit.plugin.java.JavaPlugin;

import net.escapecraft.component.ComponentManager;
import net.escapecraft.component.Log;

import com.tehbeard.areablock.AreaBlockComponent;
import com.tehbeard.beardstat.BeardStat;
import com.tehbeard.beardstat.manager.EntityStatManager;
import com.tehbeard.gamemodetoggle.GameModeToggleComponent;
import com.tehbeard.horsemod.HorseModComponent;
import com.tehbeard.kit.EscapeKitComponent;
import com.tehbeard.mentorteleport.MentorTeleportComponent;
import com.tehbeard.pigjouster.PigJousterComponent;
import com.tehbeard.reserve.ReserveComponent;
import com.tehbeard.tourbus.TourBusComponent;

import net.serubin.hatme.HatComponent;
import net.serubin.warp.WarpComponent;

import org.tulonsae.afkbooter.AfkBooterComponent;

import de.hydrox.blockalert.BlockAlertComponent;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;
import de.hydrox.endreset.EndResetComponent;
import de.hydrox.lockdown.LockdownComponent;
import de.hydrox.mobcontrol.MobControlComponent;
import de.hydrox.portalblocker.PortalBlockerComponent;
import de.hydrox.timezone.TimezoneComponent;
import de.hydrox.vanish.VanishComponent;
import de.hydrox.who.WhoCommandComponent;

public class EscapePlug extends JavaPlugin {

    public static EscapePlug self = null;
    private static final String logPrefix = "EscapePlug";
    private Log log = new Log(logPrefix);
    private ComponentManager componentManager;
    private DroxPermsAPI droxPermsAPI = null;
    private EntityStatManager beardStatManager = null;

    /**
     * Gets the plugin log prefix.
     * @return the log prefix string
     */
    public String getLogPrefix() {
        return logPrefix;
    }

    /**
     * Gets the permissions manager.
     * 
     * @return the DroxPerms plugin API
     */
    public DroxPermsAPI getDroxPermsAPI() {
        return droxPermsAPI;
    }

    /**
     * Gets the statistics manager.
     * 
     * @return the BeardStat plugin EntityStatManager
     */
    public EntityStatManager getBeardStatManager() {
        return beardStatManager;
    }

    /**
     * Runs plugin initialization.
     */
    @Override
    public void onEnable() {
        self = this;

        log.info("starting loading...");

        // load/creates/fixes config
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        DroxPerms droxPerms = ((DroxPerms) this.getServer().getPluginManager().getPlugin("DroxPerms"));
        if (droxPerms != null) {
            droxPermsAPI = droxPerms.getAPI();
        }

        BeardStat beardStat = ((BeardStat) this.getServer().getPluginManager().getPlugin("BeardStat"));
        if (beardStat != null) {
            beardStatManager = beardStat.getStatManager();
        }

        // start the component manager
        componentManager = new ComponentManager(this, log);
        componentManager.addComponent(AfkBooterComponent.class);
        componentManager.addComponent(AreaBlockComponent.class);
        componentManager.addComponent(BlockAlertComponent.class);
        componentManager.addComponent(EndResetComponent.class);
        componentManager.addComponent(EscapeKitComponent.class);
        componentManager.addComponent(GameModeToggleComponent.class);
        componentManager.addComponent(HatComponent.class);
        componentManager.addComponent(HorseModComponent.class);
        componentManager.addComponent(LockdownComponent.class);
        componentManager.addComponent(MentorTeleportComponent.class);
        componentManager.addComponent(MobControlComponent.class);
        componentManager.addComponent(PigJousterComponent.class);
        componentManager.addComponent(PortalBlockerComponent.class);
        componentManager.addComponent(ReserveComponent.class);
        componentManager.addComponent(TimezoneComponent.class);
        componentManager.addComponent(TourBusComponent.class);
        componentManager.addComponent(VanishComponent.class);
        componentManager.addComponent(WarpComponent.class);
        componentManager.addComponent(WhoCommandComponent.class);

        // start components
        componentManager.startupComponents();
        log.info("...loading complete");
    }

    /**
     * Runs plugin shutdown cleanup.
     */
    @Override
    public void onDisable() {
        log.info("unloading...");
        getComponentManager().shutdownComponents();
        self = null;
        log.info("unloaded");
    }

    /**
     * Returns the component manager.
     * 
     * @return the component manager
     */
    public ComponentManager getComponentManager() {
        return componentManager;
    }
}
