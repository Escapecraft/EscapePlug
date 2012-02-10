package en.tehbeard.pigjouster;


import org.bukkit.event.Listener;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PigJoustListener implements Listener {
	
	
	@EventHandler
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event){
		//if player is active for making jousting pigs
		if(PigJousterComponent.isPlayer(event.getPlayer()) && event.getRightClicked() instanceof Pig){
			Pig piggy = (Pig)event.getRightClicked();
			if(!PigJousterComponent.isPig(piggy)){
				//ad pig to jousting roster and add a saddle
				PigJousterComponent.addPig(piggy);
				piggy.setSaddle(true);
				event.getPlayer().sendMessage("The pig magically transforms into a jousting pig!");
			}
			else
			{
				//remove pig from jousting roster and remove the saddle
				PigJousterComponent.removePig(piggy);
				piggy.setSaddle(false);
				piggy.eject();
				event.getPlayer().sendMessage("The pig magically transforms into a regular pig!");
			}
			//canel event so they don't jump on the pig
			event.setCancelled(true);
		}
	}
	
	@EventHandler(priority=EventPriority.HIGHEST)
	public void onEntityDamage(EntityDamageEvent event){
        //check if its a protected pig.
        if(event.getEntity() instanceof Pig && PigJousterComponent.isPig((Pig)event.getEntity())){
            //cancel event if its a jousting pig.
            event.setCancelled(true);
        }
        
    }
}
