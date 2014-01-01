package net.escapecraft.component;

import java.lang.annotation.Annotation;
import java.util.HashMap;

import org.bukkit.command.CommandExecutor;

import net.escapecraft.escapeplug.EscapePlug;

public class ComponentManager {

    private EscapePlug plugin;
    private Log log;

    private HashMap<String, Class<? extends AbstractComponent>> components = new HashMap<String, Class<? extends AbstractComponent>>();
    private HashMap<String, AbstractComponent> activeComponents = new HashMap<String, AbstractComponent>();

    /**
     * Component Manager object.
     */
    public ComponentManager(EscapePlug plugin, Log log) {
        this.plugin = plugin;
        this.log = log;
    }

    /**
     * Adds a component to this manager.
     * @param component
     */
    public void addComponent(Class<? extends AbstractComponent> component) {
        ComponentDescriptor cd = component.getAnnotation(ComponentDescriptor.class);
        if (cd != null) {
            components.put(cd.slug(), component);
        }
    }

    /**
     * Starts all enabled components.
     */
    public void startupComponents() {
        for (String slug : components.keySet()) {
            startComponent(slug, false);
        }
    }

    /**
     * Shuts down all active components.
     */
    public void shutdownComponents() {
        for (String slug : activeComponents.keySet()) {
            stopComponent(slug, false);
        }
    }

    /**
     * Starts a component.
     * @param slug the keyname of the component
     * @param override if true, start component regardless of component's config enabled setting
     */
    public void startComponent(String slug, boolean override) {
        Class<? extends AbstractComponent> component = components.get(slug);
        if (component != null) {
            ComponentDescriptor cd = component.getAnnotation(ComponentDescriptor.class);
            if (cd != null) {
                if (plugin.getConfig().getBoolean("plugin." + cd.slug() + ".enabled", true) || override) {
//                    enableComponent(cd.name(), cd.version(), component.newInstance());
                    // this log prints both plugin and component prefixes
                    Log compLog = new Log(plugin.getLogPrefix(), cd.name());
                    try {
                        compLog.info("Enabling version " + cd.version());
                        AbstractComponent instance = component.newInstance();
                        instance.setLog(compLog);
                        if (instance.enable(plugin)) {
                            activeComponents.put(slug, instance);
                        }
                    } catch (Exception e) {
                        compLog.severe("COULD NOT START");
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Shuts down a component.
     * @param slug the keyname of the component
     * @param single if false, then do not remove from the hashmap
     *        (because deleting from a hashmap while iterating it - is bad)
     */
    public void stopComponent(String slug, boolean single) {
        AbstractComponent component = activeComponents.get(slug);
        if (component != null)  {
            component.getLog().info("Disabling");
            // TODO - should check for disable errors?
            component.disable();
            if (single) {
                activeComponents.remove(slug);
            }
        }
    }

    /**
     * Registers a command executor.
     * @param executor
     */
    public void registerCommands(CommandExecutor executor) {
        for (Annotation a: executor.getClass().getAnnotations()) {

            if (a instanceof BukkitCommand) {
                BukkitCommand bc = (BukkitCommand)a;
                for (String comm : bc.command()) {
                    plugin.getCommand(comm).setExecutor(executor);
                }
            }
        }
    }
}
