package net.danh.litejobs.API.Database;

import net.danh.litejobs.LiteJobs;
import net.danh.litejobs.PlayerData.PlayerData;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

public abstract class Database {
    // The name of the table we created back in SQLite class.
    public String table = "playerdata";
    JavaPlugin plugin;
    Connection connection;

    public Database(JavaPlugin instance) {
        plugin = instance;
    }

    public abstract Connection getSQLConnection();

    public abstract void load();

    public void initialize() {
        connection = getSQLConnection();
        try {
            PreparedStatement ps = connection.prepareStatement("SELECT * FROM " + table + " WHERE player = ?");
            ResultSet rs = ps.executeQuery();
            close(ps, rs);

        } catch (SQLException ex) {
            LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "Unable to retrieve connection", ex);
        }
    }

    public PlayerData getPlayerData(String name) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs;
        PlayerData playerData;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("SELECT * FROM " + table + " WHERE player = '" + name + "';");
            rs = ps.executeQuery();
            if (rs.next()) {
                playerData = new PlayerData(rs.getString("player"), rs.getString("job"), rs.getString("xp"), rs.getString("level"));
                return playerData;
            } else {
                return null;
            }
        } catch (SQLException ex) {
            LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
        return null;
    }

    public void createTable(PlayerData playerData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("INSERT INTO " + table + " (player,job,xp,level) VALUES(?,?,?,?)"); // IMPORTANT. In SQLite class, We made 3 colums. player, Kills, Total.
            ps.setString(1, playerData.getPlayer());
            ps.setString(2, playerData.getJob());
            ps.setString(3, playerData.getXP());
            ps.setString(4, playerData.getLevel());
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void updateTable(PlayerData playerData) {
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = getSQLConnection();
            ps = conn.prepareStatement("UPDATE " + table + " SET job = ?, xp = ?, level = ? " + " WHERE player = ?");
            conn.setAutoCommit(false);
            ps.setString(1, playerData.getJob());
            ps.setString(2, playerData.getXP());
            ps.setString(3, playerData.getLevel());
            ps.setString(4, playerData.getPlayer());
            ps.addBatch();
            ps.executeBatch();
            conn.commit();
        } catch (SQLException ex) {
            plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionExecute(), ex);
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                plugin.getLogger().log(Level.SEVERE, Errors.sqlConnectionClose(), ex);
            }
        }
    }

    public void close(PreparedStatement ps, ResultSet rs) {
        try {
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException ex) {
            Error.close(ex);
        }
    }
}
