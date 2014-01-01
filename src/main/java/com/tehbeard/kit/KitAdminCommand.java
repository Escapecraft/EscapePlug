package com.tehbeard.kit;

import net.escapecraft.component.BukkitCommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@BukkitCommand(command="kit-admin")
public class KitAdminCommand implements CommandExecutor {

    private EscapeKitComponent dataManager;
    public KitAdminCommand(EscapeKitComponent dataManager) {
        this.dataManager = dataManager;
    }
    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {

        if (args.length>=2 && sender instanceof Player) {
            if (args[0].equalsIgnoreCase("make")) {
                String name = args[1];
                int timer = 0;
                if (args.length == 3) {
                    timer = Integer.parseInt(args[2]);
                }
                Kit newKit = new Kit(name, timer);
                Player p = (Player) sender;
                for (ItemStack is:p.getInventory().getContents()) {
                    if (is!=null) {
                        newKit.addItem(is);
                    }
                }
                dataManager.addKit(newKit);
                dataManager.saveData();
            }

            if (args[0].equalsIgnoreCase("del")) {
                dataManager.removeKit(args[1]);
                dataManager.saveData();
            }

            return true;
        }
        return false;
    }
}
