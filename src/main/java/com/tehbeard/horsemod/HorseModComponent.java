package com.tehbeard.horsemod;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;
import org.tulonsae.mc.util.Log;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

@ComponentDescriptor(name="Horse control",slug="horsemod",version="1.00")
public class HorseModComponent extends AbstractComponent implements CommandExecutor,Listener{

	@Override
	public boolean enable(Log log, EscapePlug plugin) {
		plugin.getCommand("horsemod").setExecutor(this);
		return true;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlbl,
			String[] args) {
		
		return true;
	}

}
