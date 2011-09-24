package en.tehbeard.cartCollect;



import java.util.HashMap;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.event.vehicle.VehicleEntityCollisionEvent;
import org.bukkit.event.vehicle.VehicleListener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.inventory.ItemStack;


public class CartCollectListener extends VehicleListener {

	@Override
	public void onVehicleMove(VehicleMoveEvent event) {
		// TODO Auto-generated method stub
		if(event.getVehicle() instanceof StorageMinecart){


			for(Entity e:event.getVehicle().getNearbyEntities(3, 3, 3)){
				if(e.isDead()){continue;}


				//if item
				if(e instanceof Item){

					HashMap<Integer, ? extends ItemStack> contain = ((StorageMinecart)event.getVehicle()).getInventory().all(
							((Item)e).getItemStack().getType()
					);
					boolean ignore = true;
					for(ItemStack is: contain.values()){
						if(is.getMaxStackSize() != is.getAmount()){ignore = false;}
					}
					if(ignore && ((StorageMinecart)event.getVehicle()).getInventory().firstEmpty()==-1){return;}

					//pickup item
					if(e.getLocation().distance(event.getVehicle().getLocation())<2){
						((StorageMinecart)event.getVehicle()).getInventory().addItem(
								((Item)e).getItemStack());
						e.remove();
					}
				}
			}
		}
	}



}
