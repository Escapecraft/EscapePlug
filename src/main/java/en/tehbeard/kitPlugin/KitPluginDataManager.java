package en.tehbeard.kitPlugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Manages loading and saving of kit data
 * @author james
 *
 */
public class KitPluginDataManager {

	private YamlConfiguration config = null;

	private static KitPluginDataManager instance;
	private Plugin plugin;
	private Map<String,Kit> kits;
	private File dbFile = new File(Bukkit.getPluginManager().getPlugin("EscapePlug").getDataFolder(),"kits.yml");

	/**
	 * Initialise EscapeKit
	 * @param plugin
	 */
	public static void boot(Plugin plugin) {
		if(instance == null){instance = new KitPluginDataManager(plugin);}

	}

	/**
	 * Get an instance of EscapeKit
	 * @return
	 */
	public static KitPluginDataManager getInstance() {
		return instance;
	}

	/**
	 * reload Kit Data from file
	 */
	public void loadData(){
		kits = new HashMap<String,Kit>();
		//load config

		config = YamlConfiguration.loadConfiguration(dbFile);

		//load kits
		ConfigurationSection kitSection = config.getConfigurationSection("kits");
		if(kitSection!=null){
			Set<String> keySet = kitSection.getKeys(false);
			if(keySet!=null){
				for(String key : keySet){
					ConfigurationSection  kitConfig = kitSection.getConfigurationSection(key);
					System.out.println("Loading kit " + key);
					Kit kit = new Kit(key,kitConfig.getInt("timer"));

					List<String> items  = (List<String>)kitConfig.getList("items");
					if(items!=null){
						for(String item:items){
							String[] opt = item.split("\\|");
							if(opt.length>=3){
								ItemStack is = new ItemStack(
										Integer.parseInt(opt[0]),
										Integer.parseInt(opt[2]),
										Short.parseShort(opt[1])
										);
								for(int i =3;i< opt.length;i++){
									String[] enc = opt[i].split("\\:");
									is.addEnchantment(
											Enchantment.getById(Integer.parseInt(enc[0])), 
											Integer.parseInt(enc[1]));
								}

								kit.addItem(is
										);
							}
						}
					}
					kits.put(key,kit);
				}
			}
		}
	}

	/**
	 * Save kit Data
	 */
	public void saveData(){
		for(Kit kit : kits.values()){ 
			config.set("kits." + kit.getName() + ".timer",kit.getCooldown());
			int i = 0;
			List<String> lis = new LinkedList<String>();
			String rec;
			for(ItemStack is: kit.getItems()){

				rec = ""+is.getType().getId()+"|"+
						is.getDurability()+"|"+
						is.getAmount();

				for(Entry<Enchantment, Integer> es : is.getEnchantments().entrySet()){
					rec += "|" + es.getKey().getId() + ":" + es.getValue();
				}

				lis.add(rec);
			}
			config.set("kits." + kit.getName() + ".items",lis);
		}

		try {
			config.save(dbFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	KitPluginDataManager(Plugin plugin) {
		dbFile = new File(plugin.getDataFolder(),"kits.yml");

		//register serialization classes

		loadData();

	}

	public void convertKitDb(File file){
		Scanner in;
		try {
			in = new Scanner(file);
			
			System.out.println("Converting old kit database");
			while(in.hasNextLine()){
				//# Name;ID Amount;ID Amount;ID amount (etc)[;-cooldown]
				String line = in.nextLine();
				//# Name;ID Amount;ID Amount;ID amount (etc)[;-cooldown]
				if(!line.startsWith("#")){
					String[] part = line.split("\\;");
					if(part.length > 1){
						String name = part[0];
						int timer = 0;
						int items = part.length;
						if(part[part.length-1].startsWith("-")){
							timer = 0 - Integer.parseInt(part[part.length-1]);
							items = items - 1;
						}
						Kit newKit = new Kit(name,timer);
						for(int i = 1; i < items ; i++){
							String[] item = part[i].split(" ");
							int id = 1;
							short dam = 0;
							if(item[0].split("\\:").length==2){
								id = Integer.parseInt(item[0].split("\\:")[0]);
								dam = Short.parseShort(item[0].split("\\:")[1]);
							}
							else
							{
								id = Integer.parseInt(item[0]);
							}
							
							int count = 1;
							if(item.length==2){
							count = Integer.parseInt(item[1]);
							}
							newKit.addItem(new ItemStack(
									id,
									
									count,
									dam
									));
						}
						addKit(newKit);
						
					}
					
				}
			}

			in.close();
			System.out.println("Conversion complete");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveData();
	}
	/**
	 * Add a kit
	 * @param kit
	 */
	public void addKit(Kit kit){
		kits.put(kit.getName(),kit);
	}

	/**
	 * get a kit 
	 * @param name
	 * @return
	 */
	public Kit getKit(String name){
		return kits.get(name);
	}

	public Collection<Kit> getKits(){
		return kits.values();
	}

	public void removeKit(String name){
		kits.remove(name);
	}


}
