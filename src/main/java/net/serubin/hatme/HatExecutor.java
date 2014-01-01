package net.serubin.hatme;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

class HatExecutor {
    
    private HatComponent component;
    
    public HatExecutor(HatComponent component) {
        this.component = component;
    }

    public boolean hatOn(Player player) {
        ItemStack itemHand = player.getItemInHand();

        if (itemHand == null || itemHand.getTypeId() == 0) {
            if (player.getInventory().getHelmet() == null) {
                component.sendMessage(player, ChatColor.RED,
                        component.airHeadMessage());
                return true;
            } else {
                if (hatOff(player)) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            if (!headEmpty(player)) {
                component.sendMessage(player, ChatColor.RED,
                        component.hatAlreadyOnMessage());
                // TODO swap helmet with hand
                return true;
            }

            if (moveOneToHead(itemHand, player)) {
                component.sendMessage(player, ChatColor.YELLOW,
                        component.hatOnMessage());
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean hatOnAll(Player player) {
        ItemStack itemHand = player.getItemInHand();

        if (itemHand == null) {
            component.sendMessage(player, ChatColor.RED, component.airHeadMessage());
            // TODO take off hat
            return true;
        } else {

            if (!headEmpty(player)) {
                component.sendMessage(player, ChatColor.RED,
                        component.hatAlreadyOnMessage());
                return true;
            }

            if (moveAllToHead(itemHand, player)) {
                component.sendMessage(player, ChatColor.YELLOW,
                        component.hatOnMessage());
                return true;
            } else {
                return false;
            }
        }
    }

    public boolean giveHat(Player player, int itemID) {
        if (itemID == 0) {
            component.sendMessage(player, ChatColor.RED, component.airHeadMessage());
            return true;
        } else {

            if (!headEmpty(player)) {
                component.sendMessage(player, ChatColor.RED,
                        component.hatAlreadyOnMessage());
                return true;
            }

            if (setHead(itemID, player)) {
                component.sendMessage(player, ChatColor.YELLOW,
                        component.hatOnMessage());
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
            component.sendMessage(player, ChatColor.RED, component.noSpaceMessage());
            return true;
        } else {
            inventory.setHelmet(null);
            inventory.setItem(empty, itemHead);
            component.sendMessage(player, ChatColor.YELLOW, component.hatOffMessage());
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
            ItemStack newHelmet = new ItemStack(itemHand.getType(), 1,
                    itemHand.getDurability());
            component.debug("itemHand Original: " + itemHand.getAmount());
            newHelmet.setAmount(1);
            playerInv.setHelmet(newHelmet);
            itemHand.setAmount((itemHand.getAmount() - 1));
            component.debug("itemHand New: " + itemHand.getAmount());
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
