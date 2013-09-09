package com.tehbeard.horsemod;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Horse.Color;
import org.bukkit.entity.Horse.Variant;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.InventoryHolder;
import org.tulonsae.mc.util.Log;

import com.tehbeard.horsemod.HorseSession.HorseState;

import en.tehbeard.areablock.SessionStore;
import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

/**
 * escapeplug.horsemod.override - Override all controls OP
 * escapeplug.horsemod.override.inventory - Override inventory lock FALSE
 * escapeplug.horsemod.override.transfer  - Override transfer check FALSE
 * escapeplug.horsemod.override.damage    - Override horse damaging FALSE
 * @author James
 *
 */
@ComponentDescriptor(name="Horse control",slug="horsemod",version="1.00")
@BukkitCommand(command="horsemod")
public class HorseModComponent extends AbstractComponent implements CommandExecutor,Listener{

	public static final String PERM_INV      = "escapeplug.horsemod.override.inventory";
	public static final String PERM_TRANSFER = "escapeplug.horsemod.override.transfer";
	public static final String PERM_DAMAGE   = "escapeplug.horsemod.override.damage";
	public static final String PERM_RIDE     = "escapeplug.horsemod.override.ride";

	private SessionStore<HorseSession> session = new SessionStore<HorseSession>();

	@Override
	public boolean enable(Log log, EscapePlug plugin) {
		plugin.getCommand("horsemod").setExecutor(this);

		Bukkit.getPluginManager().registerEvents(session,plugin);
		Bukkit.getPluginManager().registerEvents(this,plugin);

		plugin.getComponentManager().registerCommands(this);

		return true;
	}

	@Override
	public void disable() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String cmdlbl,
			String[] args) {

		if(sender instanceof Player == false){
			sender.sendMessage("This command can only execute from a player");
			return true;
		}
		Player player = (Player) sender;
		if(args.length > 0){

			if(args[0].equals("transfer") && args.length == 2){
				HorseSession horseSession = getSession(player.getName());
				horseSession.state = HorseState.TRANSFER;
				
				Player p = Bukkit.getPlayerExact(args[1]);
				if(p == null){
					player.sendMessage(ChatColor.RED + "Can only transfer to players who are online");
					return true;
				}
				horseSession.toOwner = args[1];
				return true;
			}

			if(args[0].equals("info")){
				HorseSession pses = getSession(player.getName());
				pses.state = HorseState.INFO;
				return true;
			}

		}
		return false;
	}


	/**
	 * Disable inventory access unless you own a horse.
	 * @param event
	 */
	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void horseInventoryCheck(InventoryOpenEvent event){
		InventoryHolder holder = event.getInventory().getHolder();

		if(holder instanceof Horse){
			Horse horse = (Horse)holder;
			if(!haveHorsePermission(horse, (Player) event.getPlayer(),PERM_INV)){
				event.setCancelled(true);
			}

		}
	}

	@EventHandler(priority=EventPriority.HIGHEST,ignoreCancelled=true)
	public void horseClick(PlayerInteractEntityEvent event){
		if(event.getRightClicked() instanceof Horse == false){return;}

		Horse horse = (Horse)event.getRightClicked();

		Player player = event.getPlayer();
		HorseSession horseSession = getSession(player.getName());

		boolean canInv      = haveHorsePermission(horse, event.getPlayer(),PERM_INV);
		boolean canRide     = haveHorsePermission(horse, event.getPlayer(),PERM_RIDE);
		boolean canTransfer = haveHorsePermission(horse, event.getPlayer(),PERM_TRANSFER);

		switch(horseSession.state){
		case NONE:
			if(player.isSneaking()){
				event.setCancelled(!canInv);return;
			}
			else
			{
				event.setCancelled(!canRide);return;
			}
		case TRANSFER:
			event.setCancelled(true);
			if(canTransfer){
				Player p = Bukkit.getPlayerExact(horseSession.toOwner);
				if(p == null){
					player.sendMessage("Cannot transfer to offline player");
					return;
				}
				horse.setOwner(p);
				player.sendMessage("Horse transferred to " + p);
				horseSession.resetState();
			}
			break;
		case INFO:
			event.setCancelled(true);
			
			Color color  = horse.getColor();
			Variant type = horse.getVariant(); 
			String owner = horse.getOwner().getName();
			
			player.sendMessage(new String[]{
					ChatColor.GOLD + "Owner: " + ChatColor.WHITE + owner,
					ChatColor.GOLD + "Color: " + ChatColor.WHITE + color,
					ChatColor.GOLD + "Type: " + ChatColor.WHITE + type
			});
			horseSession.resetState();
			break;
		}
	}

	/**
	 * Checks if a player is allowed to access a horse by virtue of owning said horse or having a specific permission
	 * @param horse
	 * @param player
	 * @param overridePerm
	 * @return
	 */
	private boolean haveHorsePermission(Horse horse,Player player,String overridePerm){
		return isMyHorse(horse,player.getName()) || player.hasPermission(overridePerm);
	}
	
	/**
	 * Check if player owns a horse
	 * @param horse
	 * @param name
	 * @return
	 */
	private boolean isMyHorse(Horse horse, String name) {
		return horse.getOwner().getName().equalsIgnoreCase(name);
	}

	private HorseSession getSession(String player){
		if(!session.hasSession(player)){
			session.putSession(player, new HorseSession());
		}
		return session.getSession(player);
	}
	
	
	@EventHandler(ignoreCancelled=true,priority=EventPriority.HIGHEST)
	public void horseDamageProtect(EntityDamageEvent event){
		if(event.getEntity() instanceof Horse == false){return;}
		
		Horse horse = (Horse) event.getEntity();
		boolean ownerOnline = Bukkit.getPlayerExact(horse.getOwner().getName()) != null;
		
		event.setCancelled(!ownerOnline);
		
		if(event instanceof EntityDamageByEntityEvent){
			EntityDamageByEntityEvent ede = (EntityDamageByEntityEvent)event;
			if(ede.getDamager() instanceof Player){
				event.setCancelled(!((Player)ede.getDamager()).hasPermission(PERM_DAMAGE));
			}
		}
	}

}
