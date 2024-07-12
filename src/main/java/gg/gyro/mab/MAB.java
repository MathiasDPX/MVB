package gg.gyro.mab;

import gg.gyro.mab.commands.*;
import gg.gyro.mab.listeners.*;
import gg.gyro.mab.utils.Database;

import revxrsal.commands.bukkit.BukkitCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;

public final class MAB extends JavaPlugin {
    Database db = new Database(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        /* UTILS */
        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        db.connect();
        db.initialize();

        /* COMMANDS */
        handler.getAutoCompleter().registerSuggestion("privacy_scope", (args, sender, command) -> {
            return Arrays.asList("ping", "mp");
        });
        handler.getAutoCompleter().registerSuggestion("gamemodes", (args, sender, command) -> {
            return Arrays.asList("creative", "survival", "adventure", "spectator");
        });

        handler.register(
                new GamemodeCommand(),
                new WikiCommand(),
                new PrivacyCommand(this)
        );

        /* LISTENERS */
        getServer().getPluginManager().registerEvents(new PingPlayer(), this);

        System.out.println("Plugin enabled");
    }

    @Override
    public void onDisable() {
        db.close();
        System.out.println("Plugin disabled");
    }

    public Connection getConnection() {
        try {
            return db.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
