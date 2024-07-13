package gg.gyro.mab.commands;

import gg.gyro.mab.MAB;
import gg.gyro.mab.utils.Lists;
import gg.gyro.mab.utils.Privacy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;

import java.util.HashMap;
import java.util.Objects;

@Command("privacy")
@Description("Manage privacy settings")
public class PrivacyCommand {
    MAB plugin;
    Privacy privacy;
    Lists lists;

    public PrivacyCommand(MAB plugin) {
        this.plugin = plugin;
        this.privacy = new Privacy(plugin);
        this.lists = plugin.getLists();
    }

    private String getDescription(String scope){
        return switch (scope) {
            case "ping" -> "Play sound when you got mention in chat";
            case "pm" -> "Allow people to send you private messages";
            case "response" -> "Allow people to respond to your private messages";
            default -> "No description found";
        };
    }

    @Subcommand("get")
    @AutoComplete("@privacy_scopes")
    @Description("Show privacy settings")
    @Usage("/privacy get [scope]")
    public void get(CommandSender sender, @Optional() String scope) {
        if (!(sender instanceof Player player)) { return; }

        if (scope != null) {
            if (!(lists.privacy_scopes.contains(scope))) {
                player.sendMessage(ChatColor.RED+"Invalid scope");
                return;
            }
            boolean value = this.privacy.getScope(player, scope);
            if (value) {
                player.sendMessage(scope+": "+ ChatColor.GREEN +"allowed");
            } else {
                player.sendMessage(scope+": "+ ChatColor.RED + "denied");
            }
            return;
        }

        HashMap<String, Boolean> settings = this.privacy.getAll(player);

        if (settings == null) {
            player.sendMessage(ChatColor.DARK_RED+"Unable to fetch privacy settings");
            return;
        }

        if (settings.isEmpty()){
            player.sendMessage("You haven't change your privacy settings");
            return;
        }

        player.sendMessage("Your "+ChatColor.ITALIC+"changed"+ChatColor.RESET+" privacy settings:");
        for (String key : settings.keySet()) {
            if (settings.get(key)) {
                player.sendMessage(key+": "+ ChatColor.GREEN + "allowed");
            } else {
                player.sendMessage(key+": "+ ChatColor.RED + "denied");
            }
        }
    }

    @Subcommand("set")
    @AutoComplete("@privacy_scopes")
    @Description("Change privacy setting")
    @Usage("/privacy set <scope> <value>")
    public void set(CommandSender sender, @Named("scope") String key, @Named("state") boolean value) {
        if (!(sender instanceof Player player)) { return; }

        if (!(lists.privacy_scopes.contains(key))) {
            player.sendMessage(ChatColor.RED+"Invalid scope");
            return;
        }

        boolean success = this.privacy.updateScope(player, key, value);
        if (success) {
            if (value){
                player.sendMessage("Successfully "+ChatColor.GREEN+"allowed "+ChatColor.RESET+key);
            } else {
                player.sendMessage("Successfully "+ChatColor.RED+"disallowed "+ChatColor.RESET+key);
            }
        } else {
            player.sendMessage(ChatColor.DARK_RED+"Unable to change privacy settings");
        }
    }

    @Subcommand("reset")
    @Description("Reset every player's privacy settings")
    @Usage("/privacy reset")
    public void reset(CommandSender sender, @Optional() String confirmation) {
        if (!(sender instanceof Player player)) { return;}

        if (Objects.equals(confirmation, "confirm")) {
            boolean success = this.privacy.removePlayer(player);
            if (success) {
                player.sendMessage("Successfully reset your privacy settings");
            } else {
                player.sendMessage(ChatColor.DARK_RED+"Unable to reset privacy settings");
            }
        } else {
            player.sendMessage("Are you sur you want to "+ChatColor.DARK_RED+ChatColor.BOLD+"delete"+ChatColor.RESET+" your privacy settings ?");
            player.sendMessage("Type "+ChatColor.YELLOW+"/privacy reset confirm"+ChatColor.RESET+" to confirm");
        }
    }

    @Subcommand("remove")
    @AutoComplete("@privacy_scopes")
    @Description("Remove a scope")
    @Usage("/privacy remove <scope>")
    public void remove(CommandSender sender, @Named("scope") String scope) {
        if (!(sender instanceof Player player)) { return; }

        boolean success = this.privacy.removeScope(player, scope);
        if (success) {
            player.sendMessage("Successfully removed "+scope);
        } else {
            player.sendMessage(ChatColor.DARK_RED+"Unable to remove "+scope+" from privacy settings");
        }
    }

    @Subcommand("info")
    @AutoComplete("@privacy_scopes")
    @Description("Get info about privacy scopes")
    @Usage("/privacy info <scope>")
    public void info(CommandSender sender, @Named("scope") String scope) {
        if (!(sender instanceof Player player)) { return; }

        if (!(lists.privacy_scopes.contains(scope))) {
            player.sendMessage(ChatColor.RED+"Invalid scope");
            return;
        }

        player.sendMessage(scope+": "+getDescription(scope));
    }
}
