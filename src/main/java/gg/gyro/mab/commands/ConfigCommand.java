package gg.gyro.mab.commands;

import gg.gyro.mab.MAB;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

import java.io.InputStream;
import java.io.InputStreamReader;

@Command("config")
@CommandPermission("mab.config")
public class ConfigCommand {
    MAB plugin;

    public ConfigCommand(MAB plugin) {
        this.plugin = plugin;
    }

    @Subcommand("reload")
    @CommandPermission("mab.config.reload")
    public void reload(CommandSender sender) {
        this.plugin.reloadConfig();
        sender.sendMessage(ChatColor.GREEN+"Config reloaded!");
    }

    @Subcommand("get")
    @CommandPermission("mab.config.read")
    @AutoComplete("@config")
    public void get(CommandSender sender, @Named("parameter") String parameter) {
        if (!(this.plugin.getLists().config_parameters.contains(parameter))) {
            sender.sendMessage(ChatColor.DARK_RED+"Invalid parameter!");
            return;
        }
        Object value = this.plugin.getConfig().get(parameter);
        sender.sendMessage(parameter+": " + value);
    }

    @Subcommand("defaultfor")
    @CommandPermission("mab.config.default")
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
