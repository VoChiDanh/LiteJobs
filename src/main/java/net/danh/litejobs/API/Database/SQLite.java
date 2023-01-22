package net.danh.litejobs.API.Database;

import net.danh.litejobs.LiteJobs;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;

public class SQLite extends Database {
    public String SQLiteCreateTokensTable = "CREATE TABLE IF NOT EXISTS playerdata ("
            + "`player` LONGTEXT NOT NULL,"
            + "`job` LONGTEXT NOT NULL,"
            + "`xp` LONGTEXT NOT NULL,"
            + "`level` LONGTEXT NOT NULL,"
            + "PRIMARY KEY (`player`)"
            + ");";
    String dbname;

    public SQLite(JavaPlugin instance) {
        super(instance);
        dbname = "playerdata";
    }


    public Connection getSQLConnection() {
        File dataFolder = new File(plugin.getDataFolder(), dbname + ".db");
        if (!dataFolder.exists()) {
            try {
                dataFolder.createNewFile();
            } catch (IOException e) {
                LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "File write error: " + dbname + ".db");
            }
        }
        try {
            if (connection != null && !connection.isClosed()) {
                return connection;
            }
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + dataFolder);
            return connection;
        } catch (SQLException ex) {
            LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "SQLite exception on initialize", ex);
        } catch (ClassNotFoundException ex) {
            LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "You need the SQLite J BDC library. Google it. Put it in /lib folder.");
        }
        return null;
    }

    public void load() {
        connection = getSQLConnection();
        try {
            Statement s = connection.createStatement();
            s.executeUpdate(SQLiteCreateTokensTable);
            s.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        initialize();
    }
}
