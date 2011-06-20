package en.tehbeard.permwrapper;


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
	public boolean hasPermission(Player player,String node,boolean op){
		if(permissionHandler!=null){
			if(permissionHandler.has(player, node)){
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
	
	public boolean inGroup(Player player, String group){
		if(permissionHandler!=null){
			//permissionHandler.
		}
		return false;
		
	}
}
