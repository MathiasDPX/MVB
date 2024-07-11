package gg.gyro.mab;

import gg.gyro.mab.commands.*;

import revxrsal.commands.bukkit.BukkitCommandHandler;
import org.bukkit.plugin.java.JavaPlugin;

public final class MAB extends JavaPlugin {

    private BukkitCommandHandler handler;
    private static MAB instance;

    @Override
    public void onEnable() {
        this.handler = BukkitCommandHandler.create(this);
        this.instance = this;

        this.handler.register(
                new GamemodeCommand()
        );

        System.out.println("Plugin enabled");
    }

    @Override
    public void onDisable() {
        System.out.println("Plugin disabled");
    }

    public static MAB getInstance(){
        return instance;
    }
}
