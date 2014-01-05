package org.tulonsae.triggers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

/*
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.MatchResult;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
*/

/**
 * Base class for JDBC data providers for Triggers.
 * @author Based on James' code, modified by Tulonsae.
 */
public abstract class JDBCDataProvider implements IDataProvider {

    protected TriggersComponent component = null;
    protected String prefix = null;
    protected String suffix = null;

    protected Connection conn;
    protected String connUrl = null;
    protected Properties connProps = new Properties();
    protected PreparedStatement keepAlive;

    private static final String SQL_CREATE_TABLES = "triggers/create_tables";

    /**
     * Constructs this data provider.
     *
     * @param component the component object
     * @param suffix specifies which the script suffix for sql scripts
     * @param driver the driver class
     */
    public JDBCDataProvider(TriggersComponent component, String suffix, String driver) {
        this.component = component;
        this.suffix = suffix;

        // load driver
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            component.getLog().severe("Could not locate driver library for " + driver);
            e.printStackTrace();
        }
    }

    /**
     * Initialises this data provider.
     *
     * @throws SQLException
     */
    protected void initialise() throws SQLException {
        createConnection();
        createTables();
    }

    /**
     * Connects to the database.
     *
     * @throws SQLException
     */
    private void createConnection() throws SQLException {

        component.getLog().info("Connecting....");

        try {
            this.conn = DriverManager.getConnection(this.connUrl, this.connProps);
        } catch (SQLException e) {
            component.getLog().severe("Could not connect to the database.");
            throw e;
        }
    }

    /**
     * Creates tables, if necessary.
     *
     * @throws SQLException
     */
    protected void createTables() throws SQLException {
        String sqlText = component.readSQL(SQL_CREATE_TABLES + "." + suffix).replaceAll("\\$\\{PREFIX\\}", prefix).replaceAll("\\$\\{CODE_VER\\}", component.getVersion()).replaceAll("\\$\\{SCHEMA_VER\\}", component.getSchema());
        String[] sqlStatements = sqlText.split("\\;");

        try {
            for (String sql : sqlStatements) {
                conn.createStatement().execute(sql);
            }
        } catch (SQLException e) {
            component.getLog().severe("Could not create database tables.");
            throw e;
        }
    }

    /**
     * Checks connection to database.
     * <p>MySQL has an automatic timeout.</p>
     *
     * @return true if still connected
     */
/*
    private synchronized boolean checkConnection() {
        try {
            if ((this.conn == null) || !this.conn.isValid(0)) {
                component.getLog().info("Lost the database connection, attempting reconnect...");
                createConnection();
            }
        } catch (SQLException e) {
            this.conn = null;
            return false;
        }

        return this.conn != null;
    }
*/

    // ??
/*
    protected void prepareStatements() {
        this.plugin.getLogger().config("Preparing statements");

        // Maintenance
        this.keepAlive = getStatementFromScript(SQL_KEEP_ALIVE);
    }
*/

    /** ??
     * Runner used to flush to database async.
     */
/*
    private Runnable flush = new Runnable() {
        @Override
        public void run() {
            synchronized (writeCache) {
                try {
                    keepAlive.execute();
                } catch (SQLException e1) {
                }

                if (!checkConnection()) {
                    Bukkit.getConsoleSender()
                            .sendMessage(
                            ChatColor.RED
                            + "Could not restablish connection, will try again later, WARNING: CACHE WILL GROW WHILE THIS HAPPENS");
                } else {
                    plugin.getLogger().config("Saving to database");
                    for (Entry<String, EntityStatBlob> entry : writeCache
                            .entrySet()) {

                        EntityStatBlob pb = entry.getValue();
                        IStat stat = null;
                        try {
                            saveEntityData.clearBatch();
                            for (Iterator<IStat> it = pb.getStats().iterator(); it.hasNext();) {
                                stat = it.next();
                                saveEntityData.setInt(1, pb.getEntityID());
                                saveEntityData.setInt(2, getDomain(stat.getDomain()).getDbId());
                                saveEntityData.setInt(3, getWorld(stat.getWorld()).getDbId());
                                saveEntityData.setInt(4, getCategory(stat.getCategory()).getDbId());
                                saveEntityData.setInt(5, getStatistic(stat.getStatistic()).getDbId());
                                saveEntityData.setInt(6, stat.getValue());
                                saveEntityData.addBatch();
                            }
                            saveEntityData.executeBatch();

                        } catch (SQLException e) {
                            plugin.getLogger().log(Level.WARNING, "entity id: {0} :: {1}", new Object[]{pb.getName(), pb.getEntityID()});
                            plugin.getLogger().log(Level.WARNING, "domain: {0} :: {1}", new Object[]{stat.getDomain(), getDomain(stat.getDomain()).getDbId()});
                            plugin.getLogger().log(Level.WARNING, "world: {0} :: {1}", new Object[]{stat.getWorld(), getWorld(stat.getWorld()).getDbId()});
                            plugin.getLogger().log(Level.WARNING, "category: {0} :: {1}", new Object[]{stat.getCategory(), getCategory(stat.getCategory()).getDbId()});
                            plugin.getLogger().log(Level.WARNING, "statistic: {0} :: {1}", new Object[]{stat.getStatistic(), getStatistic(stat.getStatistic()).getDbId()});
                            plugin.getLogger().log(Level.WARNING, "Value: {0}", stat.getValue());
                            plugin.mysqlError(e, SQL_SAVE_STAT);
                            checkConnection();
                        }
                    }
                    plugin.getLogger().config("Clearing write cache");
                    writeCache.clear();
                }
            }

        }
    };
*/

/*
    @Override
    public void flushSync() {
        this.plugin.getLogger().info("Flushing in main thread! Game will lag!");
        this.flush.run();
        this.plugin.getLogger().info("Flushed!");
    }
*/

/*
    @Override
    public void flush() {

        new Thread(this.flush).start();
    }
*/

/*
        String[] sqlStatements = this.plugin.readSQL(this.scriptSuffix, scriptName, this.tblPrefix).split("\\;");
            String statement = matcher.replaceMatches(s, new Callback() {
                @Override
                public String foundMatch(MatchResult result) {
                    if (keys.containsKey(result.group(1))) {
                        return keys.get(result.group(1));
                    }
                    return "";
                }
            });

            if (statement.startsWith("#!")) {
                String subScript = statement.substring(2);
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Executing : " + subScript);
                executeScript(subScript, keys);
                continue;
            } else if (statement.startsWith("#")) {
                Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Status : " + statement.substring(1));
            } else {
            }
        }
    }
*/

/*
    public PreparedStatement getStatementFromScript(String scriptName, int flags) {
        try {
            return this.conn.prepareStatement(this.plugin.readSQL(this.scriptSuffix, scriptName, this.tblPrefix), flags);
        } catch (SQLException ex) {
            this.plugin.mysqlError(ex, scriptName);
            throw new BeardStatRuntimeException("Failed to create SQL statement for a script", ex, false);
        }
    }
*/

/*
    public PreparedStatement getStatementFromScript(String scriptName) {
        try {
            return this.conn.prepareStatement(this.plugin.readSQL(this.scriptSuffix, scriptName, this.tblPrefix));
        } catch (SQLException ex) {
            this.plugin.mysqlError(ex, scriptName);
            throw new BeardStatRuntimeException("Failed to create SQL statement for a script", ex, false);
        }
    }
*/
}
