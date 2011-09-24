package en.tehbeard.quickCraft;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
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
			//for each entry in shapeless
			for(Entry<String, ConfigurationNode> item : config.getNodes(("shapeless")).entrySet()){
				ConfigurationNode node = item.getValue();
				if(node.getInt("result.type", -1) != -1 && node.getInt("result.amount", -1) != -1){
					EscapePlug.printCon("Creating new shapeless recipe: "+item.getKey());
					ShapelessRecipe sr = new ShapelessRecipe(
							new ItemStack(node.getInt("result.type",1),node.getInt("result.amount",0)
									));
					for(ConfigurationNode part :node.getNodeList("parts",new  LinkedList<ConfigurationNode>())){
						MaterialData md = new MaterialData(part.getInt("type", -1),
								(byte) part.getInt("damage", 0));
						sr.addIngredient(part.getInt("count", 1),md);
					}


				}

			}
		}
	}
}
