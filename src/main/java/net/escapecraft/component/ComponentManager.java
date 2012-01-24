package net.escapecraft.component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.command.CommandExecutor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.tulonsae.mc.util.Log;


public class ComponentManager{

	private EscapePlug plugin;
	private Log log;
	public ComponentManager(EscapePlug plugin,Log log){
		this.plugin = plugin;
		this.log = log;
	}

	private Set<AbstractComponent> activeComponents = new HashSet<AbstractComponent>();


	public void startComponent(Class<? extends AbstractComponent> component){
		for(Annotation a: component.getAnnotations()){
			if(a instanceof ComponentDescriptor){
				ComponentDescriptor cd = (ComponentDescriptor)a;
				if(plugin.getConfig().getBoolean("plugin." +cd.slug() + ".enabled", true)){
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
		if(component.enable(log,plugin)){
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
					plugin.getServer().getPluginManager().registerEvent(bv.type(), listener,bv.priority(), plugin);
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
					plugin.getCommand(comm).setExecutor(executor);
				}
			}
		}
	}

}
