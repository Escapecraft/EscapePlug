package en.tehbeard.quickCraft;

import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.util.config.ConfigurationNode;

/**
 * Provides a simple craft recipe modifier
 * @author James
 *
 */
public class quickCraft {

	public static void enable(ConfigurationNode config){
		ConfigurationNode shapeless = config.getNode("shapeless");
		ConfigurationNode shaped = config.getNode("shaped");
		ConfigurationNode furnace = config.getNode("furnace");
		
		if(shapeless!=null){
			for(Entry<String, ConfigurationNode> item : config.getNodes(("shapeless")).entrySet()){
				ConfigurationNode node = item.getValue();
				if(node.getInt("result.type", -1) != -1 && node.getInt("result.amount", -1) != -1){
					ShapelessRecipe sr = new ShapelessRecipe(
							new ItemStack(node.getInt("result.type",1),node.getInt("result.amount",0)
									));
					
						
				}
				
			}
		}
	}
}
