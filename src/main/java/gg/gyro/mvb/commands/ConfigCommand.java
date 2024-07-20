package gg.gyro.mvb.commands;

import gg.gyro.mvb.MVB;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.InputStream;
import java.io.InputStreamReader;

@Command("config")
@CommandPermission("mvb.config")
@Description("Read plugin configuration")
public class ConfigCommand {
    MVB plugin;

    public ConfigCommand(MVB plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @Description("Reload config")
    public void reload(CommandSender sender) {
        this.plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN+"Config reloaded!");
    }

    @Subcommand("get")
    @CommandPermission("mvb.config.read")
    @AutoComplete("@config")
    @Description("Get configuration")
    public void get(CommandSender sender, @Named("parameter") String parameter) {
        if (!(this.plugin.getLists().config_parameters.contains(parameter))) {
            sender.sendMessage(ChatColor.DARK_RED+"Invalid parameter!");
            return;
        }
        Object value = this.plugin.getConfig().get(parameter);
        sender.sendMessage(parameter+": " + value);
    }

    @Subcommand("defaultfor")
    @CommandPermission("mvb.config.default")
    @Description("Get default configuration")
    @AutoComplete("@config")
    public void defaultfor(CommandSender sender, @Named("parameter") String parameter) {
        if (!(this.plugin.getLists().config_parameters.contains(parameter))) {
            sender.sendMessage(ChatColor.DARK_RED + "Invalid parameter!");
            return;
        }

        try {
            InputStream customClassStream= this.plugin.getClass().getResourceAsStream("/config.yml");
            assert customClassStream != null;
            InputStreamReader strR = new InputStreamReader(customClassStream);
            FileConfiguration defaults = YamlConfiguration.loadConfiguration(strR);

            sender.sendMessage(parameter+": "+defaults.get(parameter));
        } catch (Exception e) {
            sender.sendMessage("§cAn error occured while reading default config");
        }
    }

}
