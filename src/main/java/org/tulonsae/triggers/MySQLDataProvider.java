package org.tulonsae.triggers;

import java.sql.SQLException;

import com.tehbeard.beardstat.BeardStat;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.ResultSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.GZIPOutputStream;

/**
 * Data provider for MySQL storage for Triggers.
 */
public class MySQLDataProvider extends JDBCDataProvider {

    /**
     * Initializes this data provider.
     *
     * @param component the component object
     * @param host the host name
     * @param port the port number
     * @param database the database name
     * @param username the username for the database
     * @param password the password for the database
     * @param prefix the prefix of the tables
     * @throws SQLException
     */
    public MySQLDataProvider(TriggersComponent component, String host, String port, String database, String username, String password, String prefix) throws SQLException {
        super(component, "sql", "com.mysql.jdbc.Driver");

        this.connUrl = String.format("jdbc:mysql://%s:%s/%s", host, port, database);
        this.prefix = prefix;

        this.connProps.put("user", username);
        this.connProps.put("password", password);
        this.connProps.put("autoReconnect", "true");

        try {
            initialise();
        } catch (SQLException e) {
            component.getLog().severe("Could not initialise database.");
            throw e;
        }
    }
}
