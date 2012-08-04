package net.serubin.hatme;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class HatExecutor {
    public boolean hatOn(Player player) {
        ItemStack itemHand = player.getItemInHand();
        
        if (itemHand == null) {
            player.sendMessage(ChatColor.YELLOW + "[HatMe] You have just tried to put air on your head. Good job.");
            return true;
        } else {
            
            if (!headEmpty(player)) {
                player.sendMessage(ChatColor.RED + "[HatMe] You already have a hat on! Take it off with /unhat.");
                return true;
            }
            
            if (moveOneToHead(itemHand, player)) {
                player.sendMessage(ChatColor.YELLOW + "[HatMe] You now have a hat! Use /unhat to remove it.");
                return true;
            } else {
                return false;
            }
        }
    }
    
    public boolean hatOnAll(Player player) {
        ItemStack itemHand = player.getItemInHand();
        
        if (itemHand == null) {
            player.sendMessage(ChatColor.YELLOW + "[HatMe] You have just tried to put air on your head. Good job.");
            return true;
        } else {
            
            if (!headEmpty(player)) {
                player.sendMessage(ChatColor.RED + "[HatMe] You already have a hat on! Take it off with /unhat.");
                return true;
            }
            
            if (moveAllToHead(itemHand, player)) {
                player.sendMessage(ChatColor.YELLOW + "[HatMe] You now have a hat! Use /unhat to remove it.");
                return true;
            } else {
                return false;
            }
        }
    }
    
    public boolean giveHat(Player player, int itemID) {
        if (itemID == 0) {
            player.sendMessage(ChatColor.YELLOW + "[HatMe] You have just tried to put air on your head. Good job.");
            return true;
        } else {
            
            if (!headEmpty(player)) {
                player.sendMessage(ChatColor.RED + "[HatMe] You already have a hat on! Take it off with /unhat.");
                return true;
            }
            
            if (setHead(itemID, player)) {
                player.sendMessage(ChatColor.YELLOW + "[HatMe] You now have a hat! Use /unhat to remove it.");
                return true;
            } else {
                return false;
            }
        }
    }
        
        

    public boolean hatOff(Player player) {
        PlayerInventory inventory = player.getInventory();
        int empty = inventory.firstEmpty();
        ItemStack itemHead = inventory.getHelmet();
        if (empty == -1) {
            player.sendMessage(ChatColor.RED + "[HatMe] You have no space to take of your hat!");
            return true;
        } else {
            inventory.setHelmet(null);
            inventory.setItem(empty, itemHead);
            player.sendMessage(ChatColor.YELLOW + "[HatMe] You have taken off your hat!");
            return true;
        }
    }

    private boolean moveOneToHead(ItemStack itemHand, Player player) {
        PlayerInventory playerInv = player.getInventory();
                    
        // if hand has only 1 item
        if (itemHand.getAmount() == 1) {
            playerInv.setHelmet(itemHand);
            playerInv.setItemInHand(null);
            return true;
        } else {
            ItemStack newHelmet = itemHand;
            newHelmet.setAmount(1);
            itemHand.setAmount((itemHand.getAmount() - 1));
            return true;
        }
    }

    private boolean moveAllToHead(ItemStack itemHand, Player player) {
        PlayerInventory playerInv = player.getInventory();
        playerInv.setHelmet(itemHand);
        playerInv.setItemInHand(null);
        return true;
    }
    
    private boolean setHead(int itemID, Player player) {
        PlayerInventory playerInv = player.getInventory();
        playerInv.setHelmet(new ItemStack(itemID));
        return true;
    }

    private boolean headEmpty(Player player) {
        return (player.getInventory().getHelmet() == null) ? true : false;
    }
}
