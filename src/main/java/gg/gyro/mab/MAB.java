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
    BukkitCommandHandler handler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        /* UTILS */
        handler = BukkitCommandHandler.create(this);

        db.connect();
        db.initialize();

        /* COMMANDS */
        handler.getAutoCompleter().registerSuggestion("privacy_scopes", (args, sender, command) -> lists.privacy_scopes);
        handler.getAutoCompleter().registerSuggestion("gamemodes", (args, sender, command) -> lists.gamemodes);
        handler.getAutoCompleter().registerSuggestion("config", (args, sender, command) -> lists.config_parameters);

        handler.register(
                new GamemodeCommand(),
                new WikiCommand(),
                new PrivacyCommand(this),
                new ConfigCommand(this)
        );

        /* LISTENERS */
        getServer().getPluginManager().registerEvents(new PingPlayer(this), this);
        getServer().getPluginManager().registerEvents(new Insomnia(this), this);

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
        return this.lists;
    }

    public BukkitCommandHandler getCommandHandler() {
        return this.handler;
    }
}
