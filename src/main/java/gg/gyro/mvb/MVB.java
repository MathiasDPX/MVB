package gg.gyro.mvb;

import gg.gyro.mvb.commands.*;
import gg.gyro.mvb.listeners.*;

import gg.gyro.mvb.utils.Lists;
import gg.gyro.mvb.utils.Database;

import revxrsal.commands.bukkit.BukkitCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.SQLException;

public final class MVB extends JavaPlugin {
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

        for (String block : lists.utility_blocks) {
            lists.config_parameters.add("utility."+block);
        }

        /* COMMANDS */
        handler.getAutoCompleter().registerSuggestion("privacy_scopes", (args, sender, command) -> lists.privacy_scopes);
        handler.getAutoCompleter().registerSuggestion("gamemodes", (args, sender, command) -> lists.gamemodes);
        handler.getAutoCompleter().registerSuggestion("config", (args, sender, command) -> lists.config_parameters);

        handler.register(
                new GamemodeCommand(),
                new WikiCommand(),
                new PrivacyCommand(this),
                new ConfigCommand(this),
                new PrivateMessageCommands(this),
                new UtilityCommand(this),
                new PartyCommand(this)
        );

        /* LISTENERS */
        getServer().getPluginManager().registerEvents(new PingPlayer(this), this);
        getServer().getPluginManager().registerEvents(new Insomnia(this), this);
        getServer().getPluginManager().registerEvents(new JoinLeaveMessage(this), this);
        getServer().getPluginManager().registerEvents(new DurabilityPing(this), this);
        getServer().getPluginManager().registerEvents(new FoodPoisoning(this), this);
        getServer().getPluginManager().registerEvents(new DeathCompass(this), this);

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
            System.out.println("§cError while connecting to the database!!!");
        }
        return null;
    }

    public Lists getLists() {
        return this.lists;
    }
}