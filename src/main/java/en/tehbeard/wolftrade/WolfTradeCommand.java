package en.tehbeard.wolftrade;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;

import java.util.HashMap;
public class WolfTradeCommand implements CommandExecutor {

	public static HashMap<String,session> wolfSession = null; 
	WolfTradeCommand(){
		wolfSession = new HashMap<String,session>(); 
	}
	
	@Override

	public boolean onCommand(CommandSender sender, Command cmd, String cmdlbl,
			String[] args) {
		if(sender instanceof Player == false){
			return false;
		}
		if(cmdlbl.equals("wolftrade")){
			if(sender.hasPermission("escapeplug.wolftrade.tradeown")){
				if(args.length == 1){
					if(!wolfSession.containsKey(((Player)sender).getName())){
						wolfSession.put(((Player)sender).getName(),new  session());
					}
					session s = wolfSession.get(((Player)sender).getName());
					s.setName(args[0]);
					sender.sendMessage("New owner set to:" + args[0] + ". right click a wolf.");
				} else {sender.sendMessage("no player selected");}
			} else {sender.sendMessage("You can't trade wolfs");}
		}

		if(cmdlbl.equals("wolftradeconfirm")){
			if(sender.hasPermission("escapeplug.wolftrade.tradeown")){
				if(!wolfSession.containsKey(((Player)sender).getName())){
					sender.sendMessage("no inprogress wolf trade found.");
					if(
					wolfSession.get(((Player)sender).getName()).getName() !=null &&
					wolfSession.get(((Player)sender).getName()).getWolf() !=null){
						
					wolfSession.get(((Player)sender).getName()).getWolf().setOwner(EscapePlug.self.getServer().getPlayer(
							wolfSession.get(((Player)sender).getName()).getName()		
					));
					}
					return true;
				}
			}
		}

		return true;
	}

	public class session{
		private String name = null;
		private Wolf wolf = null;
		public void setName(String name) {
			this.name = name;
		}
		public String getName() {
			return name;
		}
		public void setWolf(Wolf wolf) {
			this.wolf = wolf;
		}
		public Wolf getWolf() {
			return wolf;
		}
	}

}
