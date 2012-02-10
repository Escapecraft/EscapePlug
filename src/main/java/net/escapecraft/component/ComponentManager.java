package net.escapecraft.component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.command.CommandExecutor;
import org.tulonsae.mc.util.Log;


public class ComponentManager{

	private EscapePlug plugin;
	private Log log;
	public ComponentManager(EscapePlug plugin,Log log){
		this.plugin = plugin;
		this.log = log;
	}

	private Set<AbstractComponent> activeComponents = new HashSet<AbstractComponent>();

	private HashMap<String,Class<? extends AbstractComponent>> components = new HashMap<String, Class<? extends AbstractComponent>>();

	/**
	 * add a component to this manager
	 * @param component
	 */
	public void addComponent(Class<? extends AbstractComponent> component){
		ComponentDescriptor cd = component.getAnnotation(ComponentDescriptor.class);
		if(cd!=null)	{
			components.put(cd.slug(), component);
		}

	}

	/**
	 * Start an individual component
	 * @param slug
	 */
	public void startComponent(String slug,boolean override){
		Class<? extends AbstractComponent> component = components.get(slug);
		if(component !=null){
			ComponentDescriptor cd = component.getAnnotation(ComponentDescriptor.class);
			if(cd!=null){
				if(plugin.getConfig().getBoolean("plugin." +cd.slug() + ".enabled", true) || override){
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
	
	/**
	 * Attempt to start all components that have been loaded
	 */
	public void startupComponents(){
		for(String slug:components.keySet()){
			startComponent(slug,false);
		}
	}

	
	

	private void enableComponent(Log log,AbstractComponent component){
		if(component.enable(log,plugin)){
			activeComponents.add(component);
		}
	}

	/**
	 * Disable an active component
	 * @param slug component to disable
	 */
	public void disableComponent(String slug){
		Iterator<AbstractComponent> it = activeComponents.iterator();
		AbstractComponent component;
		while(it.hasNext()){
			component = it.next();
		    ComponentDescriptor cd = component.getClass().getAnnotation(ComponentDescriptor.class);
			if(cd!=null){
				if(cd.slug().equals(slug)){
					component.disable();
					it.remove();
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
	
	public void disableComponents(){
	    log.info("Shutting down");
	    for(AbstractComponent comp : activeComponents){
	      comp.disable();
	    }
	}

	
}
