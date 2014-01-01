package net.escapecraft.escapeplug;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

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

import net.escapecraft.component.ComponentManager;
import net.escapecraft.component.Log;

import net.serubin.hatme.HatComponent;
import net.serubin.warp.WarpComponent;

import org.tulonsae.afkbooter.AfkBooter;

import de.hydrox.blockalert.BlockAlertComponent;
import de.hydrox.bukkit.DroxPerms.DroxPerms;
import de.hydrox.bukkit.DroxPerms.DroxPermsAPI;
import de.hydrox.bukkit.timezone.TimezoneComponent;
import de.hydrox.endreset.EndResetComponent;
import de.hydrox.lockdown.LockdownComponent;
import de.hydrox.mobcontrol.MobControlComponent;
import de.hydrox.portalblocker.PortalBlockerComponent;
import de.hydrox.vanish.VanishComponent;
import de.hydrox.who.WhoCommandComponent;

public class EscapePlug extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    public static EscapePlug self = null;
    private ComponentManager componentManager;
    private DroxPermsAPI droxPermsAPI = null;
    private EntityStatManager beardStatManager = null;

    /**
     * Returns the permissions manager.
     * 
     * @return
     */
    public DroxPermsAPI getDroxPermsAPI() {
        return droxPermsAPI;
    }

    /**
     * Returns the statistics manager.
     * 
     * @return
     */
    public EntityStatManager getBeardStatManager() {
        return beardStatManager;
    }

    @Override
    public void onEnable() {
        self = this;

        log.info("[EscapePlug] loading EscapePlug");

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
        componentManager = new ComponentManager(this, new Log("EscapePlug"));
        componentManager.addComponent(AfkBooter.class);
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
        log.info("[EscapePlug] EscapePlug loaded");
    }

    @Override
    public void onDisable() {
        getComponentManager().disableComponents();
        self = null;

        log.info("[EscapePlug] EscapePlug unloaded");
    }

    public static void printCon(String line) {
        log.info("[EscapePlug] " + line);
    }

    /**
     * Returns the component manager.
     * 
     * @return
     */
    public ComponentManager getComponentManager() {
        return componentManager;
    }
}
