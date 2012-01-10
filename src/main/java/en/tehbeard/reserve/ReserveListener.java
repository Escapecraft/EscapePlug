package en.tehbeard.reserve;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.BukkitEvent;

import org.bukkit.Bukkit;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event.Type;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;


public class ReserveListener extends PlayerListener {
	
	EscapePlug plugin;
	public ReserveListener(EscapePlug plugin){
		this.plugin = plugin;
	}
	@BukkitEvent(priority = Priority.Highest, type = Type.PLAYER_LOGIN)
	public void onPlayerLogin(PlayerLoginEvent event){
		plugin.printCon("Event Fired!");
		if(event.getResult()!=Result.KICK_WHITELIST){
			if(event.getPlayer().hasPermission("escapeplug.reserve.allow")){
				plugin.printCon("Allowing player in");
			}
			if((Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) && event.getPlayer().hasPermission("escapeplug.reserve.allow")==false){
				plugin.printCon("Disallowing " + event.getPlayer().getName() + " to enter!");
				event.setKickMessage("server is full :(");
				event.setResult(PlayerLoginEvent.Result.KICK_FULL);
			}
			else
			{
				event.allow();
			}
		}
	}
}
