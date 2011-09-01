package en.tehbeard.pigjouster;

import java.util.HashSet;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
/**Simplifed pig management
 * 
 * @author James
 *
 */
public class PigJouster implements CommandExecutor {
	private static HashSet<Player> activepunch = new HashSet<Player>(); 
	private static HashSet<Pig> pigs = new HashSet<Pig>();

	public static void addPig(Pig pig){
		pigs.add(pig);
	}

	public static boolean isPig(Pig pig){
		return pigs.contains(pig);
	}

	public static boolean isPlayer(Player player){
		return activepunch.contains(player);
	}

	public static void removePig(Pig pig){
		pigs.remove(pig);
	}
	public static void clearPigs(){
		pigs.clear();
	}

	/**
	 * Handle pig commands
	 * @param commandLabel
	 * @param args
	 * @return
	 */
	public boolean onCommand(CommandSender sender,Command cmd, String commandLabel, String[] args){
		if(sender instanceof Player){
			//TODO: ADD PERMISSIONS FOR THESE COMMANDS!
			if(commandLabel.equals("pig-active")){
				activepunch.add((Player)sender);
				return true;
			}

			if(commandLabel.equals("pig-deactive")){
				activepunch.remove((Player)sender);
				return true;
			}
		}
		return false;
	}
}
