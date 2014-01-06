package org.tulonsae.triggers;

import java.io.File;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Scanner;

import net.escapecraft.component.AbstractComponent;
import net.escapecraft.component.BukkitCommand;
import net.escapecraft.component.ComponentDescriptor;
import net.escapecraft.component.SchemaDescriptor;
import net.escapecraft.escapeplug.EscapePerms;
import net.escapecraft.escapeplug.EscapePlug;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Triggers component of EscapePlug.
 * @author Tulonsae
 */
@ComponentDescriptor(name = "Triggers", slug = "Triggers", version = "0.1")
@SchemaDescriptor(version = "1")
@BukkitCommand(command = { "triggers", "addtrigger", "remtrigger", "edittrigger" })
public class TriggersComponent extends AbstractComponent implements CommandExecutor {

    private EscapePlug plugin;
    private TriggersManager manager;

    /**
     * Initializes Triggers component.
     */
    @Override
    public boolean enable(EscapePlug plugin) {
        this.plugin = plugin;

        // setup data provider
        log.info("Connecting to database...");
        IDataProvider db = getDataProvider(plugin.getConfig().getConfigurationSection("plugin.triggers.database"));
        if (db == null) {
            log.severe("Error loading database, disabling component.");
            return false;
        }

        // start the manager
        manager = new TriggersManager(this, db);

        // register with Bukkit
        plugin.getServer().getPluginManager().registerEvents(new TriggersListener(this, manager), plugin);
        plugin.getComponentManager().registerCommands(this);

        return true;
    }

    /**
     * Cleans up Triggers component.
     */
    @Override
    public void disable() {
    }

    /**
     * Processes Trigger commands.
     *
     * @param sender the command sender
     * @param command the actual command
     * @param label
     * @param args the arguments to the command
     */
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // if no arguments, give help message
        if (args.length < 1) {
            return false;
        }

        // addtrigger command
        // syntax: /addtrigger motd|tip|notice [to:all|*<group-name>] <message>
        //      /addtrigger join|quit for:<player-name>|*<group-name> <message>
        // possibly add countdown
        if ("addtrigger".equalsIgnoreCase(command.getName())) {

            // need at least 2 args
            if (args.length < 2) {
                return false;
            }

            // check perms
            if (!sender.hasPermission(EscapePerms.CREATE_TRIGGERS)) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to create triggers.");
                return true;
            }

            // parse command
            String type = args[0];
            String[] target = null;
            int msgStart;
            if (args[1].toLowerCase().startsWith("to:") || args[1].toLowerCase().startsWith("for:")) {
                target = (args[1].toLowerCase().split(":"))[1].split(",");
                msgStart = 2;
            } else {
                msgStart = 1;
            }
            if (target == null) {
                target = new String[1];
                target[0] = "all";
            }
            StringBuilder msg = new StringBuilder();
            for (int i = msgStart; i < args.length; i++) {
                msg.append(args[i] + " ");
            }
            String message = msg.toString().trim();

            if ("motd".equalsIgnoreCase(type)) {
                manager.addTrigger(new MotdTrigger(target, message));
                // TODO - give out more info players, groups, etc.
                // and get it back from the manager
                sender.sendMessage(ChatColor.YELLOW + "Added motd message trigger.");
            } else if ("tip".equalsIgnoreCase(type)) {
                sender.sendMessage(ChatColor.YELLOW + "Sorry, not implemented yet!");
            } else if ("notice".equalsIgnoreCase(type)) {
                sender.sendMessage(ChatColor.YELLOW + "Sorry, not implemented yet!");
            } else if ("join".equalsIgnoreCase(type)) {
                sender.sendMessage(ChatColor.YELLOW + "Sorry, not implemented yet!");
            } else if ("quit".equalsIgnoreCase(type)) {
                sender.sendMessage(ChatColor.YELLOW + "Sorry, not implemented yet!");
            } else {
                sender.sendMessage(ChatColor.RED + "What TYPE of trigger do you want to create?  Valid types are: motd, tip, notice, join, quit.");
                 return false;
            }
        }

        return true;
    }

    /**
     * Loads a data provider from the config.
     *
     * @param config the database configuration section
     * @return the data provider or null if not able to
     */
    private IDataProvider getDataProvider(ConfigurationSection section) {
        IDataProvider db = null;

        // MySQL provider
        if (section.getString("type").equalsIgnoreCase("mysql")) {
            try {
                db = new MySQLDataProvider(this,
                    section.getString("host"),
                    String.valueOf(section.getInt("port")),
                    section.getString("database"),
                    section.getString("username"),
                    section.getString("password"),
                    section.getString("prefix"));
            } catch (SQLException e) {
                db = null;
                e.printStackTrace();
            }
        }

        // SQLite provider - TODO
        if (section.getString("type").equalsIgnoreCase("sqlite")) {
            try {
                db = new SQLiteDataProvider(this,
                    new File(plugin.getDataFolder(), "triggers.db").toString(), section.getString("prefix"));
            } catch (SQLException e) {
                db = null;
                e.printStackTrace();
            }
        }

        return db;
    }

    /**
     * Loads SQL script from plugin jar.
     *
     * @param filename the file path
     */
    protected String readSQL(String filename) {
        InputStream is = plugin.getResource(filename);
        if (is == null) {
            log.severe("Could not find sql script " + filename);
        }

        // get the entire file as a single token, removing line breaks
        // this returns the entire file as a single token
        Scanner scanner = new Scanner(is);
        String script = scanner.useDelimiter("\\Z").next().replaceAll("\\Z", "").replaceAll("\\n|\\r", "");

        scanner.close();
        return script;
    }
}
