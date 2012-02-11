package en.tehbeard.wolftrade;

import org.bukkit.entity.AnimalTamer;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerListener;

public class WolfTradeListener extends PlayerListener {

	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
		if(event.getRightClicked() instanceof Wolf){
			if(!WolfTradeCommand.wolfSession.containsKey(event.getPlayer().getName())){
				return;
			}
			if(WolfTradeCommand.wolfSession.get(event.getPlayer().getName()).getName()==null){
				return;
			}
			AnimalTamer tamer = ((Wolf)event.getRightClicked()).getOwner();
			Player p = (Player)tamer;
			if(p.getName().equals(event.getPlayer().getName())){
				WolfTradeCommand.wolfSession.get(event.getPlayer().getName()).setWolf((Wolf)event.getRightClicked());
				event.getPlayer().sendMessage("wolf set as the one to give away, type /wolftradeconfirm to give this wolf to "+WolfTradeCommand.wolfSession.get(event.getPlayer().getName()).getName());
			}
		}
	}
}


