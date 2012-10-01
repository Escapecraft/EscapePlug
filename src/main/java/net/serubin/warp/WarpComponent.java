package net.serubin.warp;

import java.util.logging.Logger;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.escapePlug.EscapePlug;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.tulonsae.mc.util.Log;

@ComponentDescriptor(name = "Warps", slug = "warp", version = "0.1")
@BukkitCommand(command = { "warp", "setwarp" })
public class WarpComponent extends AbstractComponent implements CommandExecutor {
    private EscapePlug plugin;
    private FlatFile flatFile;
    private Logger log;

    public boolean onCommand(CommandSender sender, Command cmd,
            String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("setwarp")) {
            if (args.length == 1) {
                return false;
            }
            flatFile.addWarp(args[0], ((Player) sender).getLocation(),
                    (Player) sender);
            return true;
        }
        return false;
    }

    @Override
    public boolean enable(Log log, EscapePlug plugin) {
        this.plugin = plugin;
        this.log = plugin.getLogger();
        plugin.getComponentManager().registerCommands(this);
        flatFile = new FlatFile(plugin, this.log);
        return true;
    }

    @Override
    public void disable() {
        // TODO Auto-generated method stub

    }

}
