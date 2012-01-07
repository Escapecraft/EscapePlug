package en.tehbeard.kitPlugin.command;

import java.util.Collection;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import en.tehbeard.kitPlugin.Kit;
import en.tehbeard.kitPlugin.KitPluginDataManager;

public class KitCommand implements CommandExecutor{

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lbl,
			String[] args) {
		// TODO Auto-generated method stub

		if(args.length==0){
			sender.sendMessage(ChatColor.AQUA + "Kits you have access to:");
			Collection<Kit> kits = KitPluginDataManager.getInstance().getKits();
			String msg = "";
			if(kits!=null){
				for(Kit kit : kits){
					if(kit.canUsePerm(sender)){
						if(msg.length()>0){
							msg+=", ";
						}
						msg+=kit.getName();
					}
				}
			}
		}

	return true;
}

}
