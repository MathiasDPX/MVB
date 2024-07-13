package gg.gyro.mab.commands;

import gg.gyro.mab.MAB;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import revxrsal.commands.annotation.AutoComplete;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Subcommand;
import revxrsal.commands.bukkit.annotation.CommandPermission;

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
        sender.sendMessage("Config reloaded!");
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
}
