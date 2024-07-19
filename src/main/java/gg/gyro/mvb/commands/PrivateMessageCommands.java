package gg.gyro.mvb.commands;

import gg.gyro.mvb.MVB;
import gg.gyro.mvb.utils.Privacy;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;

import java.util.HashMap;

public class PrivateMessageCommands {
    MVB plugin;
    Privacy privacy;

    HashMap<Player, Player> lastMessage = new HashMap<>();

    public PrivateMessageCommands(MVB plugin) {
        this.plugin = plugin;
        this.privacy = new Privacy(plugin);
    }

    @Command({"w", "whisper"})
    @Description("Send a private message to a player")
    public void whisper(CommandSender sender, @Named("Target") Player target, @Named("message") String message) {
        if (!(sender instanceof Player player)) { return; }
        if (target == player) {
            player.sendMessage("§cYou can't send a private message to yourself");
            return;
        }

        if (!(privacy.getScope(target, "pm"))) {
            player.sendMessage("§c"+target.getName()+" don't accept private messages");
            return;
        }

        player.sendMessage("§4[§rto "+target.getName()+"§4]§r §6->§r "+message);
        target.sendMessage("§4[§rfrom "+sender.getName()+"§4]§r §6->§r "+message);

        lastMessage.put(target, player);
    }

    @Command({"r", "respond"})
    @Description("Respond to your last whisper")
    public void respond(CommandSender sender, @Named("message") String message) {
        if (!(sender instanceof Player player)) { return; }
        Player target = lastMessage.get(sender);

        if (target == null) {
            player.sendMessage(ChatColor.RED+"You have no one to respond to");
            return;
        }

        if (!target.isOnline()) {
            player.sendMessage(ChatColor.RED+target.getName()+" is offline");
            return;
        }

        if (!(privacy.getScope(target, "response"))) {
            player.sendMessage("§c"+target.getName()+" don't accept responses");
            return;
        }

        player.sendMessage("§4[§rto "+target.getName()+"§4]§r §6->§r "+message);
        target.sendMessage("§4[§rfrom "+sender.getName()+"§4]§r §6->§r "+message);
    }
}
