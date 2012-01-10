package en.tehbeard.kitPlugin.command;


import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import en.tehbeard.kitPlugin.Kit;
import en.tehbeard.kitPlugin.KitPluginDataManager;

public class KitAdminCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl,
			String[] args) {
		// TODO Auto-generated method stub


		if(args.length==2 && sender instanceof Player){
			if(args[0].equalsIgnoreCase("make")){
				String name = args[1];
				int timer = 0;
				if(args.length == 3){
					timer = Integer.parseInt(args[2]);
				}
				Kit newKit = new Kit(name, timer);
				Player p = (Player)sender;
				for(ItemStack is:p.getInventory().getContents()){
					if(is!=null){
						newKit.addItem(is);
					}
				}
				KitPluginDataManager.getInstance().addKit(newKit);
				KitPluginDataManager.getInstance().saveData();
			}

			if(args[0].equalsIgnoreCase("del")){
				KitPluginDataManager.getInstance().removeKit(args[1]);
				KitPluginDataManager.getInstance().saveData();
			}
		}
		return true;
	}

}
