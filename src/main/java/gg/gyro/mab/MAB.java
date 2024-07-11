package gg.gyro.mab;

import gg.gyro.mab.commands.*;

import revxrsal.commands.bukkit.BukkitCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class MAB extends JavaPlugin {

    @Override
    public void onEnable() {
        BukkitCommandHandler handler = BukkitCommandHandler.create(this);

        handler.register(
                new GamemodeCommand(),
                new WikiCommand()
        );

        System.out.println("Plugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }
}
