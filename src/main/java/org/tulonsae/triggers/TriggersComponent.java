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

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * Triggers component of EscapePlug.
 * @author Tulonsae
 */
@ComponentDescriptor(name = "Triggers", slug = "Triggers", version = "0.1")
@SchemaDescriptor(version = "1")
@BukkitCommand(command = { "triggers", "addtrigger", "remtrigger", "changetrigger" })
public class TriggersComponent extends AbstractComponent implements CommandExecutor {

    private EscapePlug plugin;

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
            log.severe("Error loading database, disabling component");
            return false;
        }

        // register with Bukkit
//        plugin.getServer().getPluginManager().registerEvents(new TriggersListener(), plugin);
        //plugin.getComponentManager().registerCommands(this);

        return true;
    }

    /**
     * Cleans up Triggers component.
     */
    @Override
    public void disable() {
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
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

        // SQLite provider
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
    String readSQL(String filename) {
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
