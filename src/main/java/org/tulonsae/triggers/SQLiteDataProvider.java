package org.tulonsae.triggers;

import java.sql.SQLException;

/**
 * Data provider for SQLite storage for Triggers.
 */
public class SQLiteDataProvider extends JDBCDataProvider {

    /**
     * Initializes this data provider.
     *
     * @param component the component object
     * @param dbname the sqlite file path
     * @param prefix the prefix of the tables
     * @throws SQLException
     */
    public SQLiteDataProvider(TriggersComponent component, String dbname, String prefix) throws SQLException {
        super(component, "sqlite", "org.sqlite.JDBC");

        this.connUrl = String.format("jdbc:sqlite:%s", dbname, prefix);
        this.prefix = prefix;

        try {
            initialise();
        } catch (SQLException e) {
            component.getLog().severe("Could not initialise database.");
            throw e;
        }
    }
}
