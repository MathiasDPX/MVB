package gg.gyro.mab.commands;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;
import revxrsal.commands.bukkit.annotation.CommandPermission;
import revxrsal.commands.command.CommandActor;

import java.util.Arrays;

public class GamemodeCommand {
    @Command({"gamemode", "gm"})
    @AutoComplete("@gamemodes")
    @CommandPermission("gyro.commands.gamemode")
    public void onCommand(CommandActor actor, @Named("gamemode") String gamemodes, @Optional @Default("me") Player player) {
        String mode = gamemodes.toLowerCase();

        if (Arrays.asList("creative", "1", "c").contains(mode)) {
            player.setGameMode(GameMode.CREATIVE);
            player.sendMessage("Change "+ ChatColor.RED+player.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"creative");
        } else if (Arrays.asList("adventure", "2", "a").contains(mode)) {
            player.setGameMode(GameMode.ADVENTURE);
            player.sendMessage("Change "+ ChatColor.RED+player.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"adventure");
        } else if (Arrays.asList("spectator", "3", "sp").contains(mode)) {
            player.setGameMode(GameMode.SPECTATOR);
            player.sendMessage("Change "+ ChatColor.RED+player.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"spectator");
        } else if (Arrays.asList("survival", "0", "s").contains(mode)) {
            player.setGameMode(GameMode.SURVIVAL);
            player.sendMessage("Change "+ ChatColor.RED+player.getName()+ChatColor.RESET+" gamemode to "+ChatColor.GOLD+"survival");
        } else {
            player.sendMessage(ChatColor.RED+"Unknown gamemode "+mode);
        }
    }
}
