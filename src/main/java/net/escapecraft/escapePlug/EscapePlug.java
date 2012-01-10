package net.escapecraft.escapePlug;


import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;

import java.util.Set;
import java.util.logging.Level;


import me.tehbeard.endernerf.EnderNerfComponent;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.BukkitEvent;
import net.escapecraft.escapePlug.component.ComponentDescriptor;
import net.serubin.hatme.HatmeComponent;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.tulonsae.mc.util.Log;

import de.hydrox.antiSlime.SlimeDamageListener;
import de.hydrox.bukkit.timezone.TimezoneComponent;
import de.hydrox.lockdown.LockdownComponent;
import en.tehbeard.gamemode.GameModeToggleComponent;
import en.tehbeard.kitPlugin.EscapeKitComponent;

import en.tehbeard.mentorTeleport.MentorTeleportComponent;

import en.tehbeard.pigjouster.PigJouster;
import en.tehbeard.pigjouster.PigListener;
import en.tehbeard.pigjouster.PigPlayerListener;
import en.tehbeard.reserve.ReserveListComponent;


public class EscapePlug extends JavaPlugin {

	private static final Log log = new Log("EscapePlug");

	//keeps a record of all active Components
	private Set<AbstractComponent> activeComponents = new HashSet<AbstractComponent>();


	private void startComponent(Class<? extends AbstractComponent> component){
		for(Annotation a: component.getAnnotations()){
			if(a instanceof ComponentDescriptor){
				ComponentDescriptor cd = (ComponentDescriptor)a;
				if(getConfig().getBoolean("plugin." +cd.slug() + ".enabled", true)){
					try {
						log.info("Enabling " + cd.name() + " " + cd.version());
						Log compLog = new Log("EscapePlug",cd.name());
						enableComponent(compLog,component.newInstance());
					} catch (Exception e) {
						log.info("COULD NOT START");
						e.printStackTrace();
					} 
				}
			}
		}

	}


	private void enableComponent(Log log,AbstractComponent component){
		if(component.enable(log,this)){
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
					log.config("registering event hook " + bv.type().toString());
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
					log.config("Registering command /" +comm);
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

		if(getConfig().getBoolean("plugin.debugmode",false)){
			Log.setLogLevel(Level.CONFIG);
			log.info("DEBUG MODE ENABLED");
		}
		
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

		//start loading endernerf
		startComponent(EnderNerfComponent.class);

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





		log.info("[EscapePlug] EscapePlug loaded");
	}

	public void onDisable() {
		for(AbstractComponent comp: activeComponents){
			comp.tidyUp();
		}
		log.info("[EscapePlug] EscapePlug unloaded");
	}


}
