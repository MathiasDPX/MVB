package gg.gyro.mab.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;

@Command({"gm", "gamemode"})
@AutoComplete("@gamemode *")
public class GamemodeCommand {
    @Subcommand("survival")
    @Description("Survival")
    @CommandPermission("gyro.commands.gamemode")
    public void survival(Player player, @Named("target") @Default("self") Player target) {
        player.setGameMode(GameMode.SURVIVAL);
        player.sendMessage("Change "+ ChatColor.RED+target.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"survival");
    }

    @Subcommand("creative")
    @Description("Creative")
    @CommandPermission("gyro.commands.gamemode")
    public void creative(Player player, @Named("target") @Default("self") Player target) {
        player.setGameMode(GameMode.CREATIVE);
        player.sendMessage("Change "+ ChatColor.RED+target.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"creative");
    }

    @Subcommand("adventure")
    @Description("Adventure")
    @CommandPermission("gyro.commands.gamemode")
    public void adventure(Player player, @Named("target") @Default("self") Player target) {
        player.setGameMode(GameMode.ADVENTURE);
        player.sendMessage("Change "+ ChatColor.RED+target.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"adventure");
    }

    @Subcommand("spectator")
    @Description("Spectator")
    @CommandPermission("gyro.commands.gamemode")
    public void spectator(Player player, @Named("target") @Default("self") Player target) {
        player.setGameMode(GameMode.SPECTATOR);
        player.sendMessage("Change "+ ChatColor.RED+target.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"spectator");
    }
}
