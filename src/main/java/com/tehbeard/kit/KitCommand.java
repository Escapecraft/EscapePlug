package com.tehbeard.kit;

import java.util.Collection;

import net.escapecraft.component.BukkitCommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@BukkitCommand(command="kit")
public class KitCommand implements CommandExecutor {

    private EscapeKitComponent dataManager;

    public KitCommand(EscapeKitComponent dataManager) {
        this.dataManager = dataManager;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
        // TODO Auto-generated method stub

        if (args.length == 0) {
            sender.sendMessage(ChatColor.AQUA + "Kits you have access to:");
            Collection<Kit> kits = dataManager.getKits();
            String msg = "";
            if (kits != null) {
                for (Kit kit : kits) {
                    if (kit.canUsePerm(sender)) {
                        if (msg.length() > 0) {
                            msg += ", ";
                        }
                        msg += kit.getName();
                    }
                }
            }
            sender.sendMessage(msg);
        }

        if (args.length == 1 && sender instanceof Player) {
            Kit kit = dataManager.getKit(args[0]);
            if (kit == null) {
                sender.sendMessage(ChatColor.RED + " Kit does not exist");
                return true;
            }

            switch(kit.giveKit((Player)sender)) {
            case OK:
                sender.sendMessage("Enjoy your kit!");
                dataManager.saveData();
                break;
            case PERM:
                sender.sendMessage("You can't use that kit!");
                break;
            case TIMER:
                for (KitContext kc : KitContext.getContexts(((Player)sender).getName())) {
                    if (kc.getName().equalsIgnoreCase(args[0])) {
                        float hoursTill = (kc.timeTill() / 3600);
                        sender.sendMessage("You must wait " + hoursTill + " hours till you can use that kit!");
                    }
                    break;
                }
                break;
            }
        } else if (sender.isOp() && args.length == 2) { //Add support for console and command block
            Kit kit = dataManager.getKit(args[0]);
            if (kit == null) {
                sender.sendMessage(ChatColor.RED + " Kit does not exist");
                return true;
            }
            
            kit.giveKit(Bukkit.getPlayer(args[1]),true,true);
        }

        return true;
    }
}
