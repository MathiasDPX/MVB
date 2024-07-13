package gg.gyro.mab.utils;

import gg.gyro.mab.MAB;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class Privacy {
    MAB plugin;
    Connection connection;

    public Privacy(MAB plugin) {
        this.plugin = plugin;
        connection = plugin.getConnection();
    }

    public boolean updateScope(Player player, String scope, boolean state) {
        try{
            PreparedStatement statement = connection.prepareStatement("INSERT INTO privacy VALUES (?,?,?) ON DUPLICATE KEY UPDATE state=?;");

            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, scope);
            statement.setBoolean(3, state);
            statement.setBoolean(4, state);

            statement.executeUpdate();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public boolean getScope(Player player, String scope) {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT state FROM privacy WHERE player = ? AND scope = ? LIMIT 1");
            statement.setString(1, player.getUniqueId().toString());
            statement.setString(2, scope);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                return rs.getBoolean("state");
            } else {
                return true;
            }

        } catch (SQLException e){
            this.plugin.getLogger().warning("Unable to fetch scope "+scope+" for "+player.getUniqueId());
            return false;
        }
    }

    public HashMap<String, Boolean> getAll(Player player) {
        HashMap<String, Boolean> privacySettings = new HashMap<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM privacy WHERE player = ?");
            statement.setString(1, player.getUniqueId().toString());

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                String scope = rs.getString("scope");
                Boolean value = rs.getBoolean("state");

                privacySettings.put(scope, value);
            }
            return privacySettings;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean removePlayer(Player player) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM privacy where player = ?");
            statement.setString(1, player.getUniqueId().toString());
            statement.executeUpdate();

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeScope(Player player, String scope) {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM privacy WHERE scope = ? AND player = ?");
            statement.setString(1, scope);
            statement.setString(2, player.getUniqueId().toString());

            statement.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
