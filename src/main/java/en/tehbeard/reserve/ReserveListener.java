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
	
	@BukkitEvent(priority = Priority.Highest, type = Type.PLAYER_LOGIN)
	public void onPlayerLogin(PlayerLoginEvent event){
		EscapePlug.printCon("LOGIN FIRED");
		if(event.getResult()!=Result.KICK_WHITELIST){
			if(event.getPlayer().hasPermission("escapeplug.reserve.allow")){
				EscapePlug.printCon("Should allow");
			}
			if((Bukkit.getOnlinePlayers().length >= Bukkit.getMaxPlayers()) && event.getPlayer().hasPermission("escapeplug.reserve.allow")==false){
				EscapePlug.printCon("ATTEMPTING DENIAL!");
				event.setKickMessage("server is fullup :(");
				event.setResult(PlayerLoginEvent.Result.KICK_FULL);
			}
			else
			{
				event.allow();
			}
		}
	}
}
