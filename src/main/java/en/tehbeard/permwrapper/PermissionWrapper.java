package en.tehbeard.permwrapper;


import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;
import org.bukkit.plugin.Plugin;

public class PermissionWrapper {
	private static PermissionHandler permissionHandler;

	private static void setupPermissions(Plugin p) {
		Plugin permissionsPlugin = p.getServer().getPluginManager().getPlugin("Permissions");

		if (permissionHandler == null) {
			if (permissionsPlugin != null) {
				permissionHandler = ((Permissions) permissionsPlugin).getHandler();
			} else {
				//Default to OP message here
			}
		}
	}
	/**
	 * simple Wrapper for permissions 
	 * @param node node to check for
	 * @param op whether it should be an op command if permissions fails
	 * @return
	 */
	public static boolean hasPermission(Player player,String node,boolean op){
		if(permissionHandler!=null){
			if(permissionHandler.has(player,"escapeplug."+node)){
				return true;
			}		}
		else
		{
			if(op){
				if(player.isOp()){
					return true;
				}
			}
			else
			{
				return true;
			}
		}
	return false;
	}
	
	/**
	 * Check if player is in a group
	 * @param world
	 * @param player
	 * @param group
	 * @return
	 */
	public static boolean inGroup(World world, Player player, String group){
		if(permissionHandler!=null){
			return permissionHandler.inGroup(world.getName(),
                    player.getName(),
                    group);
		}
		return false;
		
	}
}
