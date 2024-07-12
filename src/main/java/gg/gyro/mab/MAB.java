package gg.gyro.mab;

import gg.gyro.mab.commands.*;
import gg.gyro.mab.listeners.*;

import gg.gyro.mab.utils.Lists;
import gg.gyro.mab.utils.Database;

import revxrsal.commands.bukkit.BukkitCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class MAB extends JavaPlugin {
    Database db = new Database(this);
    Lists lists = new Lists();

    @Override
    public void onEnable() {
        saveDefaultConfig();

        /* UTILS */
        BukkitCommandHandler handler = BukkitCommandHandler.create(this);
        db.connect();
        db.initialize();

        /* COMMANDS */
        handler.getAutoCompleter().registerSuggestion("privacy_scopes", (args, sender, command) -> {
            return lists.privacy_scopes;
        });
        handler.getAutoCompleter().registerSuggestion("gamemodes", (args, sender, command) -> {
            return lists.gamemodes;
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

    public Lists getLists() {
        return lists;
    }
}
