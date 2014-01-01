package net.escapecraft.escapePlug;

import java.util.logging.Logger;

import com.tehbeard.beardstat.BeardStat;
import com.tehbeard.beardstat.manager.EntityStatManager;

import net.escapecraft.component.ComponentManager;
import net.serubin.hatme.HatComponent;
import net.serubin.warp.WarpComponent;

import org.bukkit.plugin.java.JavaPlugin;
import org.tulonsae.afkbooter.AfkBooter;
import org.tulonsae.antixray.AntiXrayComponent;
import org.tulonsae.mc.util.Log;

import com.tehbeard.horsemod.HorseModComponent;
import com.tehbeard.kit.EscapeKitComponent;
import com.tehbeard.tourbus.TourBusComponent;

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

import en.tehbeard.areablock.AreaBlockComponent;
import en.tehbeard.gamemode.GameModeToggleComponent;
import en.tehbeard.mentorTeleport.MentorTeleportComponent;
import en.tehbeard.pigjouster.PigJousterComponent;
import en.tehbeard.reserve.ReserveComponent;

public class EscapePlug extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    private ComponentManager componentManager;
    private DroxPermsAPI droxPermsAPI = null;

    public DroxPermsAPI getDroxPermsAPI() {
        return droxPermsAPI;
    }

    public EntityStatManager getBeardStatManager() {
        return beardStatManager;
    }

    private EntityStatManager beardStatManager = null;
    public static EscapePlug self = null;

    @Override
    public void onEnable() {
        self = this;

        log.info("[EscapePlug] loading EscapePlug");

        // load/creates/fixes config
        getConfig().options().copyDefaults(true);
        saveConfig();
        reloadConfig();

        DroxPerms droxPerms = ((DroxPerms) this.getServer().getPluginManager()
                .getPlugin("DroxPerms"));
        if (droxPerms != null) {
            droxPermsAPI = droxPerms.getAPI();
        }

        BeardStat beardStat = ((BeardStat) this.getServer().getPluginManager()
                .getPlugin("BeardStat"));
        if (beardStat != null) {
            beardStatManager = beardStat.getStatManager();
        }

        // start the component manager
        componentManager = new ComponentManager(this, new Log("EscapePlug"));
        componentManager.addComponent(MentorTeleportComponent.class);
        componentManager.addComponent(ReserveComponent.class);
        componentManager.addComponent(GameModeToggleComponent.class);
        componentManager.addComponent(TimezoneComponent.class);
        componentManager.addComponent(LockdownComponent.class);
        componentManager.addComponent(PigJousterComponent.class);
        componentManager.addComponent(WhoCommandComponent.class);
        componentManager.addComponent(MobControlComponent.class);
        componentManager.addComponent(BlockAlertComponent.class);
        componentManager.addComponent(VanishComponent.class);
        componentManager.addComponent(PortalBlockerComponent.class);
        componentManager.addComponent(AreaBlockComponent.class);
        componentManager.addComponent(EndResetComponent.class);
        componentManager.addComponent(HatComponent.class);
        componentManager.addComponent(AfkBooter.class);
        componentManager.addComponent(AntiXrayComponent.class);
        componentManager.addComponent(WarpComponent.class);

        componentManager.addComponent(TourBusComponent.class);
        componentManager.addComponent(EscapeKitComponent.class);
        componentManager.addComponent(HorseModComponent.class);

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
     * Return the component manager
     * 
     * @return
     */
    public ComponentManager getComponentManager() {
        return componentManager;
    }
}
