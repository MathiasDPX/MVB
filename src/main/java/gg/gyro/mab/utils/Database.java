package gg.gyro.mab.utils;

import gg.gyro.mab.MAB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class Database {
    MAB plugin;
    Connection connection;

    public Database(MAB plugin) {
        this.plugin = plugin;
    }

    public void connect() {
        if (plugin.getConfig().getString("database.jdbc") == null) {
            plugin.getLogger().severe("Please, add a database in the config.yml file!");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }

        try {
            Class.forName("com.mysql.jdbc.Driver");
            this.connection = DriverManager.getConnection(
                    Objects.requireNonNull(this.plugin.getConfig().getString("database.jdbc")),
                    this.plugin.getConfig().getString("database.user"),
                    this.plugin.getConfig().getString("database.password"));
        } catch (SQLException | ClassNotFoundException e) {
            plugin.getLogger().severe("Error while connecting connection to Database");
            plugin.getServer().getPluginManager().disablePlugin(plugin);
        }
    }

    public void close() {
        if (this.connection != null) {
            try {
                if (!this.connection.isClosed()) {
                    this.connection.close();
                }
            } catch (SQLException e) {
                plugin.getLogger().severe("Error while closing connection to Database");
            }
        }
    }

    public void initialize(){
        try {
            connection.prepareStatement("CREATE TABLE IF NOT EXISTS privacy (player VARCHAR(36) NOT NULL, scope VARCHAR(32) NOT NULL, state BOOLEAN DEFAULT 1)").executeUpdate();
        } catch (SQLException e) {
            plugin.getLogger().severe("Error while initializing Database");
        }
    }

    public Connection getConnection() throws SQLException {
        if (this.connection != null) {
            if (!this.connection.isClosed()) {
                return this.connection;
            }
        }
        this.connect();
        return this.connection;
    }
}
