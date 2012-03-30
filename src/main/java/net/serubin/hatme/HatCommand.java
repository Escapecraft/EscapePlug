package net.serubin.hatme;

import java.util.HashMap;
import java.util.List;

import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class HatCommand implements CommandExecutor {

	private String notAllowedMsg;
	private boolean rbAllow;
	private List<Integer> rbBlocks;
	private List<Integer> allowID;
	private boolean rbOp;
	private EscapePlug plugin;

	public HatCommand(List<Integer> rbBlocks, boolean rbAllow,
			String notAllowedMsg, boolean rbOp) {
		this.rbBlocks = rbBlocks;
		this.rbAllow = rbAllow;
		this.notAllowedMsg = notAllowedMsg;
		this.rbOp = rbOp;
		this.plugin = plugin;
	}

	public boolean onCommand(CommandSender sender, Command cmd,
			String commandLabel, String[] args) {
	    if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player!");
            return true;
        }
        Player player = (Player) sender;
        ItemStack itemHand = player.getItemInHand();
        PlayerInventory inventory = player.getInventory();
        int itemHandId = itemHand.getTypeId();
        int intArg0;
        // on command hat
        if (commandLabel.equalsIgnoreCase("hat")) {
            // if args 0 do standard hat function
            if (args.length == 0) {
                if (checkPermissionBasic(player)) {
                    allowID = rbBlocks;
                    // if restrict is true
                    if (rbAllow) {
                        // if op or has perm no restrict
                        if (!checkPermissionNoRestrict(player)) {
                            // checks for allowed blocks
                            if ((!allowID.contains(itemHandId))
                                    && (itemHand != null)) {
                                // Gets custom message from config
                                player.sendMessage(ChatColor.RED
                                        + notAllowedMsg);
                                return true;
                            } else {
                                // if item is allowed, do hat
                                hatOn(sender, cmd, commandLabel, args);
                                return true;
                            }
                        } else {
                            // if player has perms to ignore restrict is off do
                            // hat
                            hatOn(sender, cmd, commandLabel, args);
                            return true;
                        }
                    } else {
                        // if restricting is off do hat
                        hatOn(sender, cmd, commandLabel, args);
                        return true;
                    }
                } else {
                    // if no perms yell
                    player.sendMessage(ChatColor.RED
                            + "You do not have permission");
                    return true;
                }
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("-a")) {
                    if (checkPermissionAll(player)) {
                        allowID = rbBlocks;
                        // if restrict is true
                        if (rbAllow) {
                            // if op or has perm no restrict
                            if (!checkPermissionNoRestrict(player)) {
                                // checks for allowed blocks
                                if ((!allowID.contains(itemHandId))
                                        && (itemHand != null)) {
                                    // Gets custom message from config
                                    player.sendMessage(ChatColor.RED
                                            + notAllowedMsg);
                                    return true;
                                } else {
                                    // if item is allowed, do hat
                                    hatOnAll(sender);
                                    return true;
                                }
                            } else {
                                // if player has perms to ignore restrict is off
                                // do
                                // hat
                                hatOnAll(sender);
                                return true;
                            }
                        } else {
                            // if restricting is off do hat
                            hatOnAll(sender);
                            return true;
                        }
                    } else {
                        player.sendMessage(ChatColor.RED
                                + "You do not have permission");
                    }
                    return true;
                } else if (checkArgInt(args)) {
                    if (checkPermissionGive(player, args)) {
                        allowID = rbBlocks;
                        // if restrict is true
                        if (rbAllow) {
                            // if op or has perm no restrict
                            if (!checkPermissionNoRestrict(player)) {
                                // checks for allowed blocks
                                if ((!allowID.contains(Integer
                                        .parseInt(args[0])))
                                        && (Integer.parseInt(args[0]) != 0)) {
                                    player.sendMessage(ChatColor.RED
                                            + notAllowedMsg);
                                    return true;
                                } else {
                                    // if block is allowed do give hat
                                    giveHat(sender, cmd, commandLabel, args);
                                    return true;
                                }
                            } else {
                                // of player has perms to ignore
                                // restrict do give hat
                                giveHat(sender, cmd, commandLabel, args);
                                return true;
                            }
                        } else {
                            // if restricting is off do givehat
                            giveHat(sender, cmd, commandLabel, args);
                            return true;
                        }
                    } else {
                        // if no perms, yell
                        player.sendMessage(ChatColor.RED
                                + "You do not have permission");
                        return true;
                    }
                }
            }
        }

        if (commandLabel.equalsIgnoreCase("unhat")) {
            // if command is unhat do unhat
            if (args.length >= 1) {
                return false;
            } else {
                // check is hat is 0
                if (player.getInventory().getHelmet() == null) { // If
                                                                 // helmet
                                                                 // is
                                                                 // empty
                                                                 // do
                                                                 // nothing
                    player.sendMessage(ChatColor.RED
                            + "You have no hat to take off!");
                    return true;
                } else {
                    hatOff(sender, cmd, commandLabel, args);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hatOnAll(CommandSender sender) {
        Player player = (Player) sender;
        if (player.getItemInHand().getTypeId() == 0) {
            player.sendMessage(ChatColor.RED + "Please pick a valid item!");
            return true;
        } else {
            ItemStack itemHand = player.getItemInHand();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemHead = inventory.getHelmet();
            if (itemHead != null) {
                player.sendMessage(ChatColor.RED
                        + "You already have a hat! Use /unhat to take it off");
            } else {
                inventory.setHelmet(itemHand);
                inventory.removeItem(itemHand);
                player.sendMessage(ChatColor.YELLOW + "You now have a hat!");
                return true;
            }
        }
        return true;
    }

    public boolean hatOn(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player player = (Player) sender;
        // checks if hand is air
        if (player.getItemInHand().getTypeId() == 0) {
            player.sendMessage(ChatColor.RED + "Please pick a valid item!");
            return true;
        } else {
            ItemStack itemHand = player.getItemInHand();
            PlayerInventory inventory = player.getInventory();
            ItemStack itemHead = inventory.getHelmet();
            // checks if head has hat
            if (itemHead != null) {
                int empty = inventory.firstEmpty();
                // checks if first empty is null
                if (empty == -1) {
                    player.sendMessage(ChatColor.RED
                            + "You already have a hat!");
                } else {
                    int itemHandAmount = itemHand.getAmount();
                    // if hand has more than 1 item
                    if (itemHandAmount != 1) {
                        // get hand stack -1
                        int newItemHandAmount = itemHandAmount - 1;
                        // set that to hand stack
                        itemHand.setAmount(newItemHandAmount);

                        // create new stack for head
                        Material itemId = Material.getMaterial(itemHand
                                .getTypeId());
                        ItemStack newHead = new ItemStack(itemId, 1);
                        if (itemHand.getTypeId() == 35
                                || itemHand.getTypeId() == 17
                                || itemHand.getTypeId() == 18
                                || itemHand.getTypeId() == 44) {
                            short itemHandData = itemHand.getDurability();
                            newHead = new ItemStack(itemId, 1, itemHandData);
                        }
                        inventory.setHelmet(newHead);
                    } else {
                        inventory.setHelmet(itemHand);
                        inventory.setItemInHand(null);
                        player.sendMessage(ChatColor.YELLOW
                                + "You now have a hat!");
                        inventory.setItem(empty, itemHead);
                        return true;
                    }// removes item from helmet
                     // Sets item from
                     // helmet
                     // to first open
                     // slot
                    return true;
                }
                return false;
            } else {
                // if no hat
                int itemHandAmount = itemHand.getAmount();
                // if hand has more than one block
                if (itemHandAmount != 1) {
                    // set hand stack amount -1S
                    int newItemHandAmount = itemHandAmount - 1;
                    // set new stack to hand
                    itemHand.setAmount(newItemHandAmount);
                    // get hand id
                    Material itemId = Material
                            .getMaterial(itemHand.getTypeId());
                    ItemStack newHead = new ItemStack(itemId, 1);
                    if (itemHand.getTypeId() == 35
                            || itemHand.getTypeId() == 17
                            || itemHand.getTypeId() == 18
                            || itemHand.getTypeId() == 44) {
                        short itemHandData = itemHand.getDurability();
                        newHead = new ItemStack(itemId, 1, itemHandData);
                    }
                    inventory.setHelmet(newHead);
                } else {
                    // if hand is one block set helmet remove hand
                    inventory.setHelmet(itemHand);
                    inventory.setItemInHand(null);
                    player.sendMessage(ChatColor.YELLOW + "You now have a hat!");
                    return true;
                }
                player.sendMessage(ChatColor.YELLOW + "You now have a hat!");
                return true;
            }

        }
    }

    public boolean hatOff(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        if (args.length >= 1) {
            return false;
        } else {
            if (player.getInventory().getHelmet() == null) { // If
                                                             // helmet
                                                             // is
                                                             // empty
                                                             // do
                                                             // nothing
                player.sendMessage(ChatColor.RED
                        + "You have no hat to take off!");
                return true;
            } else {
                // ItemStack itemHand = player.getItemInHand();
                // PlayerInventory inventory = player.getInventory();
                int empty = inventory.firstEmpty();
                ItemStack itemHead = inventory.getHelmet(); // Get item in
                                                            // helmet
                if (empty == -1) {
                    player.sendMessage(ChatColor.RED
                            + "You have no space to take of your hat!");
                } else {
                    inventory.setHelmet(null); // removes item from helmet
                    inventory.setItem(empty, itemHead); // Sets item from
                                                        // helmet
                                                        // to first open
                                                        // slot
                    player.sendMessage(ChatColor.YELLOW
                            + "You have taken off your hat!");
                    return true;
                }
            }
        }
        return true;
    }

    public boolean giveHat(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        Player player = (Player) sender;
        PlayerInventory inventory = player.getInventory();
        ItemStack itemHead = inventory.getHelmet();
        int empty = inventory.firstEmpty();
        ItemStack item;
        Material itemId;
        int itemIdint = Integer.parseInt(args[0]);
        // gets id
        try {
            itemId = Material.getMaterial(itemIdint);
            // gets item from id
        } catch (NumberFormatException e) {
            itemId = Material.matchMaterial(args[0]);
        }
        // int to Material
        item = new ItemStack(itemId, 1);
        // puts on head
        if (itemHead != null) {
            if (empty == -1) {
                player.sendMessage(ChatColor.RED + "You already have a hat!");
            } else {
                inventory.setHelmet(item);
                inventory.setItem(empty, itemHead);
                player.sendMessage(ChatColor.YELLOW
                        + "You now have been given a hat!");
            }
        } else {
            inventory.setHelmet(item);
            player.sendMessage(ChatColor.YELLOW
                    + "You now have been given a hat!");
            return true;
        }
        return true;
    }

    public boolean checkPermissionBasic(Player player) {
        // check perm
        if (player.hasPermission("escapeplug.hatme.hat")
                || player.hasPermission("escapeplug.hatme.hat."
                        + player.getItemInHand().getTypeId()))
            return true;
        if (rbOp = true && player.isOp())
            return true;
        return false;
    }

    public boolean checkPermissionGive(Player player, String[] args) {
        // check perm
        if (player.hasPermission("escapeplug.hatme.hat.give")
                || player.hasPermission("escapeplug.hatme.hat.give."
                        + Integer.parseInt(args[0])))
            return true;
        if (rbOp = true && player.isOp())
            return true;
        return false;
    }

    public boolean checkPermissionAll(Player player) {
        // check perm
        if (player.hasPermission("escapeplug.hatme.hat.all")
                || player.hasPermission("escapeplug.hatme.hat.all."
                        + player.getItemInHand().getTypeId()))
            return true;
        if (rbOp = true && player.isOp())
            return true;
        return false;
    }

    public boolean checkPermissionNoRestrict(Player player) {
        // check perm
        // if (player.hasPermission("excapeplug.hatme.norestrict"))
        // return true;
        // if (rbOp = true && player.isOp())
        // return true;
        return false;
    }

    public boolean checkArgInt(String[] args) {
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException ex) {
            // Item was not an int, do nothing
            return false;
        }
        return true;
	}
}
