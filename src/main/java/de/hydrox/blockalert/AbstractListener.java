package de.hydrox.blockalert;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockListener;

public abstract class AbstractListener extends BlockListener {

	public abstract void addModerator(Player mod);
	public abstract void removeModerator(Player mod);
}
