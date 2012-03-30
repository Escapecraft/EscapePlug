package en.tehbeard.pigjouster;

import java.util.HashSet;
import java.util.Set;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Pig;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;
/**Simplifed pig management
 * 
 * @author James
 *
 */
@ComponentDescriptor(name="Pig jousting",slug="pigjoust",version="1.00")
@BukkitCommand(command={"pig-active","pig-deactive"})
public class PigJousterComponent extends AbstractComponent implements CommandExecutor {
	private static Set<Player> activepunch = new HashSet<Player>(); 
	private static Set<Pig> pigs = new HashSet<Pig>();

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
		if (!(sender.hasPermission("escapeplug.pigjoust"))) {
			sender.sendMessage("You don't have permission for pig-jousting.");
			return true;
		}
		if(sender instanceof Player){
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

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        plugin.getComponentManager().registerCommands(this);
        Bukkit.getPluginManager().registerEvents(new PigJoustListener(), plugin);
        return true;
    }

    @Override
    public void disable() {
        
    }
}
