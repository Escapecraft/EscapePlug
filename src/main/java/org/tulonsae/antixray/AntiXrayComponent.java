package org.tulonsae.antixray;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.tulonsae.mc.util.Log;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

@ComponentDescriptor(slug="antixray", name="AntiXray", version="1.00")
public class AntiXrayComponent extends AbstractComponent {

    private Log log;
    private EscapePlug plugin;

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.log = log;
        this.plugin = plugin;

        //Bukkit.getPluginManager().registerEvents(this, plugin);

	return true;
    }

    @Override
    public void disable() {
    }
}
