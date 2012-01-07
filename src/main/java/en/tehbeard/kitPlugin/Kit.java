package en.tehbeard.kitPlugin;

import java.util.ArrayList;
import java.util.Map;


import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.permissions.Permissible;

/**
 * Represents a kit
 * @author james
 *
 */
public class Kit {
	private String name;
	private ArrayList<ItemStack> items;
	private int timer;
	
	
	/**
	 * Create an empty kit
	 * @param name
	 * @param timer
	 */
	public Kit(String name,int timer){
		this(name, timer,new ArrayList<ItemStack>());
	}
	
	/**
	 * Create a kit from an existing list
	 * @param name
	 * @param timer
	 * @param items
	 */
	public Kit(String name,int timer,ArrayList<ItemStack> items){
		this.name = name;
		this.items = items;
		this.timer = timer;
	}
	/**
	 * get name of kit
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * get the items in a kit
	 * @return
	 */
	public ArrayList<ItemStack> getItems() {
		return items;
	}

	/**
	 * 
	 * @return
	 */
	public int getCooldown() {
		return timer;
	}

	/**
	 * adds an item to a kit
	 * @param item
	 */
	public void addItem(ItemStack item){
		items.add(item);
	}
	
	/**
	 * returns the size of the kit (number of items)
	 * @return
	 */
	public int size(){
		return items.size();
	}
	
	/**
	 * returns string of a kit
	 */
	public String toString(){
		String ret  = "kit: " + name + "\n";
		for(ItemStack item :items){
			ret += item.toString() + "\n";
		}
		
		return ret;
	}

	/**
	 * Returns if a permissible can use this kit
	 * @param permissible
	 * @return
	 */
	public boolean canUsePerm(Permissible permissible){
		return permissible.isOp() || permissible.hasPermission("escapeplug.kit.*")|| permissible.hasPermission("escapeplug.kit."+name);
	}
	
	/**
	 * Give the kit to a player
	 * @param player
	 * @return
	 */
	public Result giveKit(Player player){
		return giveKit(player,false);
	}
	
	/**
	 * give a kit to a player
	 * @param player player to give kit to
	 * @param overrideContexts if set to true, any existing context (timer) is ignored
	 * @return
	 */
	public Result giveKit(Player player,boolean overrideContexts){
		if(!overrideContexts){
			for(KitContext kc : KitContext.getContexts(player.getName())){
				if(kc.getName().equals(name)){
					if(!kc.canUseTime()){
						return Result.TIMER;
					}
				}
			}
		}
		if(!canUsePerm(player)){
			return Result.PERM;
		}
		for(ItemStack item : items){
		player.getInventory().addItem(item);
		}
		if(!overrideContexts){
			KitContext newContext = new KitContext(name, System.currentTimeMillis() + (timer*1000));
			KitContext.addContext(player.getName(),newContext);
		}
		return Result.OK;
	}

	public enum Result{
		OK,
		PERM,
		TIMER
	}
}
 