package net.escapecraft.escapePlug;

import org.bukkit.command.CommandSender;

public interface IEscapePlugCommandHandler {
	public boolean handleCommand(CommandSender sender,String commandLabel, String[] args);
}
