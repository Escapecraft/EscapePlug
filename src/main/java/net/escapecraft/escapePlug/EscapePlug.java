package net.escapecraft.escapePlug;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;


import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.BukkitEvent;
import net.escapecraft.escapePlug.component.ComponentDescriptor;
import net.serubin.hatme.HatmeComponent;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.plugin.java.JavaPlugin;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.timezone.TimezoneComponent;
import de.hydrox.lockdown.LockdownComponent;
import de.hydrox.lockdown.LockdownListener;
import en.tehbeard.endernerf.EndernerfListener;
import en.tehbeard.gamemode.GameModeToggleComponent;
import en.tehbeard.kitPlugin.EscapeKitComponent;
import en.tehbeard.kitPlugin.command.KitAdminCommand;
import en.tehbeard.kitPlugin.command.KitCommand;

import en.tehbeard.mentorTeleport.MentorTeleportComponent;

import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;
import en.tehbeard.reserve.ReserveListComponent;


public class EscapePlug extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");

	//keeps a record of all active Components
	private Set<AbstractComponent> activeComponents = new HashSet<AbstractComponent>();


	private void startComponent(Class<? extends AbstractComponent> component){
		for(Annotation a: component.getAnnotations()){
			if(a instanceof ComponentDescriptor){
				ComponentDescriptor cd = (ComponentDescriptor)a;
				if(getConfig().getBoolean("plugin." +cd.slug() + ".enabled", true)){
					try {
						printCon("Enabling " + cd.name() + " " + cd.version());
						enableComponent(component.newInstance());
					} catch (Exception e) {
						printCon("COULD NOT START");
						e.printStackTrace();
					} 
				}
			}
		}

	}
	
	
	public void enableComponent(AbstractComponent component){
		if(component.enable(this)){
			activeComponents.add(component);
		}
	}


	/**
	 * Register events of a listener
	 * @param listener
	 */
	public void registerEvents(Listener listener){
		for(Method m: listener.getClass().getMethods()){
			for(Annotation a: m.getAnnotations()){

				if(a instanceof BukkitEvent){
					BukkitEvent bv = (BukkitEvent)a;
					printCon("registering event hook " + bv.type().toString());
					this.getServer().getPluginManager().registerEvent(bv.type(), listener,bv.priority(), this);
				}
			}
		}
	}
	
	/**
	 * Register a command executor
	 * @param executor
	 */
	public void registerCommands(CommandExecutor executor){
		for(Annotation a: executor.getClass().getAnnotations()){

			if(a instanceof BukkitCommand){
				BukkitCommand bc = (BukkitCommand)a;
				for(String comm : bc.command()){
					printCon("Registering command /" +comm);
					getCommand(comm).setExecutor(executor);
			}
		}
	}
	}

	public void onEnable() {
		
		log.info("[EscapePlug] loading EscapePlug");

		//load/creates/fixes config
		getConfig().options().copyDefaults(true);
		saveConfig();

		//Starting reserve list
		startComponent(ReserveListComponent.class);

		//start loading MentorTeleport
		startComponent(MentorTeleportComponent.class);

		//start loading togglemode
		startComponent(GameModeToggleComponent.class);
		
		//start EscapeKit
		startComponent(EscapeKitComponent.class);
	
		//start loading lockdown
		startComponent(LockdownComponent.class);
		
		//start loading Timezone
		startComponent(TimezoneComponent.class);
		

		//start loading hatMe
		startComponent(HatmeComponent.class);
		
		
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

		




		//start loading endernerf
		if(getConfig().getBoolean("plugin.endernerf.enabled", true)){
			log.info("[EscapePlug] loading enderNerf");
			EntityListener el = new EndernerfListener();
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PICKUP, el, Event.Priority.Highest, this);
			this.getServer().getPluginManager().registerEvent(Event.Type.ENDERMAN_PLACE, el, Event.Priority.Highest, this);

			//finished loading endernerf
		}

		

		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
		for(AbstractComponent comp: activeComponents){
			comp.tidyUp();
		}
		log.info("[EscapePlug] EscapePlug unloaded");
	}

	public void printCon(String line){
		log.info("[EscapePlug] "+line);
	}
}
