package de.hydrox.blockalert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.tulonsae.mc.util.Log;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

@ComponentDescriptor(slug="blockalert",name="Block Alert",version="1.00")
public class BlockAlertComponent extends AbstractComponent {

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
	Map<String, List<Integer>> notifyBlockBreak = new HashMap<String, List<Integer>>();
	Map<String, List<Integer>> notifyBlockPlace = new HashMap<String, List<Integer>>();
	Set<String> worlds = plugin.getConfig().getConfigurationSection("plugin.blockalert.worlds.break.").getKeys(false);
	for (String world : worlds) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Load Break Alerts for World " + world);
		List<Integer> blockBreakList = plugin.getConfig().getIntegerList("plugin.blockalert.worlds.break." + world);
		notifyBlockBreak.put(world, blockBreakList);
	}
	worlds = plugin.getConfig().getConfigurationSection("plugin.blockalert.worlds.place.").getKeys(false);
	for (String world : worlds) {
		Bukkit.getConsoleSender().sendMessage(ChatColor.DARK_PURPLE + "Load Placement Alerts for World " + world);
		List<Integer> blockPlaceList = plugin.getConfig().getIntegerList("plugin.blockalert.worlds.place." + world);
		notifyBlockPlace.put(world, blockPlaceList);
	}
	AbstractListener alertListener = null;
	boolean notifyOnCanceledBreak = plugin.getConfig().getBoolean("plugin.blockalert.notifyOnCanceledBreak");
	boolean notifyOnCanceledPlace = plugin.getConfig().getBoolean("plugin.blockalert.notifyOnCanceledPlace");
	if (plugin.isHawkEyeLoaded()) {
		alertListener = new AlertListenerHawkEye(notifyBlockBreak, notifyBlockPlace, notifyOnCanceledBreak, notifyOnCanceledPlace);
	} else {
		alertListener = new AlertListener(notifyBlockBreak, notifyBlockPlace, notifyOnCanceledBreak, notifyOnCanceledPlace);
	}
        Bukkit.getPluginManager().registerEvents(alertListener,plugin);
	return true;
    }

    @Override
    public void disable() {
    }

}
