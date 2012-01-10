package net.serubin.hatme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.escapecraft.escapePlug.EscapePlug;
import net.escapecraft.escapePlug.component.AbstractComponent;
import net.escapecraft.escapePlug.component.BukkitCommand;
import net.escapecraft.escapePlug.component.ComponentDescriptor;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

@ComponentDescriptor(name="HatMe",slug="hatme",version="7.4-ECV")
@BukkitCommand(command={"hat","unhat"})
public class HatmeComponent extends AbstractComponent implements CommandExecutor {

	private String notAllowedMsg;
	private boolean rbUseList;
	private List<Integer> rbBlocks;
	//private List<Integer> allowID;
	private boolean rbOp;

	private YamlConfiguration config;
	private File configFile;
	private EscapePlug plugin;
	

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage("This command requires an instance of a player.");
			return false;
		}
		Player player = (Player) sender;
		ItemStack itemHand = player.getItemInHand();
		PlayerInventory inventory = player.getInventory();
		int itemHandId = itemHand.getTypeId();
		if (commandLabel.equalsIgnoreCase("hat")) {
			if (checkPermissionBasic(player)) {
				if (args.length == 0) {
					
					if (rbUseList) {
						// if restrict is true
						if ((!rbBlocks.contains(itemHandId))
								&& (itemHandId != 0)) {
							// checks for allowed blocks
							player.sendMessage(ChatColor.RED + notAllowedMsg);
							return true;
						} else {
							hatOn(sender);
							return true;
						}
					} else {
						hatOn(sender);
						return true;
					}
					// if restrict is false
				}
				if (args.length == 1) {
					if (checkPermissionGive(player, args)) {
						
						if (rbUseList) {
							// if restrict is true
							if (!checkPermissionNoRestrict(player)) {
								// if op or has perm no restrict
								if ((!rbBlocks.contains(Integer
										.parseInt(args[0])))
										&& (Integer.parseInt(args[0]) != 0)) {
									// checks for allowed blocks
									player.sendMessage(ChatColor.RED
											+ notAllowedMsg);
									return true;
								} else {
									giveHat(sender, cmd, commandLabel, args);
									return true;
								}
							} else {
								giveHat(sender, cmd, commandLabel, args);
								return true;
							}
						} else {
							giveHat(sender, cmd, commandLabel, args);
							return true;
						}
					} else {
						player.sendMessage(ChatColor.RED
								+ "You do not have permission");
						return true;
					}
				}
			} else {
				player.sendMessage(ChatColor.RED + "You do not have permission");
				return true;
			}

			/*
			 * Material item; int itemId = Integer.parseInt(args[0]); try {
			 * //int id = Integer.parseInt(args[0]); item =
			 * Material.getMaterial(itemId); } catch (NumberFormatException e) {
			 * item = Material.matchMaterial(args[0]); }
			 * 
			 * }
			 */
			// return true;
			// return true;
		}

		if (commandLabel.equalsIgnoreCase("unhat")) {
			if (player.getInventory().getHelmet().getTypeId() == 0) {
				// If helmet is empty do nothing
				player.sendMessage(ChatColor.RED
						+ "You have no hat to take off!");
				return true;
			} else {
				// ItemStack itemHand = player.getItemInHand();
				// PlayerInventory inventory = player.getInventory();
				int empty = inventory.firstEmpty();
				// Get item in helmet
				ItemStack itemHead = inventory.getHelmet();
				if (empty == -1) {
					player.sendMessage(ChatColor.RED
							+ "You have no space to take of your hat!");
				} else {
					// removes item from helmet
					inventory.setHelmet(null);
					// Sets item from helmet to first open slot
					inventory.setItem(empty, itemHead);
					player.sendMessage(ChatColor.YELLOW
							+ "You have taken off your hat!");
					return true;
				}
			}
		}

		return true;
	}

	private boolean checkPermissionBasic(Player player) {
		if (player.hasPermission("escapeplug.hatme.hat")
				|| player.hasPermission("escapeplug.hatme.hat."
						+ player.getItemInHand().getTypeId()))
			return true;
		if (rbOp = true && player.isOp())
			return true;
		return false;
	}

	private boolean checkPermissionGive(Player player, String[] args) {
		if (player.hasPermission("escapeplug.hatme.give")
				|| player.hasPermission("escapeplug.hatme.give."
						+ Integer.parseInt(args[0])))
			return true;
		if (rbOp = true && player.isOp())
			return true;
		return false;
	}

	private boolean checkPermissionNoRestrict(Player player) {
		if (player.hasPermission("escapeplug.hatme.norestrict"))
			return true;
		if (rbOp = true && player.isOp())
			return true;
		return false;
	}

	public boolean hatOn(CommandSender sender) {
		Player player = (Player) sender;
		if (player.getItemInHand().getTypeId() == 0) {
			player.sendMessage(ChatColor.RED + "Please pick a valid item!");
			return true;
		} else {
			ItemStack itemHand = player.getItemInHand();
			PlayerInventory inventory = player.getInventory();
			ItemStack itemHead = inventory.getHelmet();
			inventory.removeItem(itemHand);
			inventory.setHelmet(itemHand);
			if (itemHead.getTypeId() != 0) {
				inventory.setItemInHand(itemHead);
				player.sendMessage(ChatColor.YELLOW + "You now have a hat!");
			} else {
				player.sendMessage(ChatColor.YELLOW + "You now have a hat!");
				return true;
			}
		}
		return false;
	}

	public boolean giveHat(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
		Player player = (Player) sender;
		PlayerInventory inventory = player.getInventory();
		ItemStack itemHead = inventory.getHelmet();
		int empty = inventory.firstEmpty();
		ItemStack item;
		Material itemId;
		int itemIdint = Integer.parseInt(args[0]);
		// gets id
		try {
			// int id = Integer.parseInt(args[0]);
			itemId = Material.getMaterial(itemIdint);
			// gets item from id
		} catch (NumberFormatException e) {
			itemId = Material.matchMaterial(args[0]);
		}
		// int to Material
		item = new ItemStack(itemId, 1);
		if (itemHead.getTypeId() != 0) {
			if (empty == -1) {
				player.sendMessage(ChatColor.RED + "You already have a hat!");
			} else {
				inventory.setHelmet(item);
				inventory.setItem(empty, itemHead);
				player.sendMessage(ChatColor.YELLOW
						+ "You now have been given a hat!");
			}
		} else {
			inventory.setHelmet(item);
			player.sendMessage(ChatColor.YELLOW
					+ "You now have been given a hat!");
			return true;
		}
		return true;
	}

	@Override
	public boolean enable(EscapePlug plugin) {
		this.plugin = plugin;
		configFile = new File(plugin.getDataFolder(),"hatme.yml");
		config = YamlConfiguration.loadConfiguration(configFile );
		
		readConfig();
		

		//construct command and assign to /hat and /unhat
		plugin.registerCommands(this);
		
		
		
		return true;
	}
	
	private void readConfig(){
		//get Config
		rbBlocks = config.getIntegerList("hatme.allowed");
		rbUseList = config.getBoolean("hatme.enableRestrict");
		notAllowedMsg = config.getString("hatme.notAllowedMsg");
		rbOp = config.getBoolean("hatme.opnorestrict");
	}

	private void initialConfig(){
		//generate initial config data
		YamlConfiguration temp = new YamlConfiguration();
		temp.set("hatme.enableRestrict", true);
        temp.set("hatme.opnorestrict", true);
		temp.set("hatme.notAllowedMsg","Invalid Item");
        List<Integer> list = new LinkedList<Integer>();
        Integer[] ints = { 0, 1
, 2
, 3
, 4
, 5
, 12
, 13
        , 14
        , 15
        , 17
        , 18
        , 20
        , 22
        , 23
        , 24
        , 25
        , 35
        , 41
        , 42
        , 44
        , 45
        , 46
        , 47
        , 48
        , 49
        , 52
        , 54
        , 57
        , 58
        , 80
        , 81
        , 82
        , 87
        , 88
        , 89
        , 91
        , 98
        , 103
        , 112};

		 for(int i :ints){
			 list.add(i);
		 }
		 temp.set("hatme.allowed", list);
		 
		 //set as defaults and save
		 config.setDefaults(temp);
		 config.options().copyDefaults(true);
		 try {
			config.save(configFile);
		} catch (IOException e) {
			plugin.printCon("Could not read hatme config!");
			e.printStackTrace();
		}
	}
	@Override
	public void tidyUp() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void reloadConfig() {
		try {
			config.load(configFile);
			readConfig();
		} catch (FileNotFoundException e) {
			plugin.printCon("Could not find hatme config!");
			e.printStackTrace();
		} catch (IOException e) {
			plugin.printCon("Could not read hatme config!");
			e.printStackTrace();
		} catch (InvalidConfigurationException e) {
			plugin.printCon("Config file is broken!");
			e.printStackTrace();
		}

		
	}

}
