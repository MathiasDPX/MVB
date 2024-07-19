package gg.gyro.mvb.commands;

import gg.gyro.mvb.MVB;
import gg.gyro.mvb.utils.Privacy;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;
import revxrsal.commands.annotation.Named;
import revxrsal.commands.annotation.Optional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TeleportCommand {
    MVB plugin;
    Privacy privacy;

    HashMap<Player, Player> tpa_requests = new HashMap<>(); // Tp first to second

    public TeleportCommand(MVB plugin) {
        this.plugin = plugin;
        this.privacy = new Privacy(plugin);
    }

    public List<Player> getInvitesTo(Player player) {
        List<Player> returnList = new ArrayList<>();
        for (Map.Entry<Player, Player> entry: tpa_requests.entrySet()) {
            if (entry.getValue() == player) {
                returnList.add(entry.getKey());
            }
        }
        return returnList;
    }

    @Command("tpa")
    public void tpa(CommandSender sender, @Named("target") Player target) {
        if (!(sender instanceof Player player)) { return; }
        if (player.equals(target)) {
            player.sendMessage("§cYou can't teleport to yourself!");
            return;
        }

        if (!privacy.getScope(target, "tpa_requests")) {
            player.sendMessage("§c"+target.getName()+" don't accept tpa requests");
            return;
        }

        if (tpa_requests.containsKey(player)) {
            player.sendMessage("You already have a pending teleportation request");
            return;
        }
        tpa_requests.put(player, target);

        TextComponent message = new TextComponent(sender.getName() + " sent you a teleportation request.\n§b");
        TextComponent acceptButton = new TextComponent("§a[[ACCEPT]]");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept "+player.getName()));

        TextComponent refuseButton = new TextComponent("§c [[DENY]]");
        refuseButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny "+player.getName()));

        message.addExtra(acceptButton);
        message.addExtra(refuseButton);

        target.spigot().sendMessage(message);

        player.sendMessage("§aSuccessfully sent a teleportation request to "+target.getName());
    }

    @Command("tpaccept")
    @Description("Accept a teleportation request")
    public void tpaccept(CommandSender sender, @Optional @Named("target") Player target) {
        if (!(sender instanceof Player player)) { return; }

        List<Player> invites = getInvitesTo(player);

        if (target != null) {
            if (!invites.contains(target)) {
                player.sendMessage("§c"+target.getName()+" hasn't send you any teleportation requests");
                return;
            } else {
                if (invites.get(invites.indexOf(target)) != target) {
                    player.sendMessage("§c"+target.getName()+" hasn't send you any teleportation requests");
                    return;
                } else {
                    target.sendMessage("§a"+target.getName()+" has accepted your teleportation request");
                    target.sendMessage("He will be teleported in §l3 seconds");
                    player.sendMessage("You will be teleported in §l3 seconds");

                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {
                        player.teleport(target.getLocation());
                        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
                    } }, 20 * 3);
                    tpa_requests.remove(target);
                    return;
                }
            }
        }

        if (invites.size() == 1) {
            Player newTarget = invites.getFirst();
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() { public void run() {
                player.teleport(newTarget.getLocation());
                player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_TELEPORT, 1, 1);
            } }, 20 * 3);
            tpa_requests.remove(newTarget);
            return;
        }
        player.sendMessage("You have multiples invitations:");
        for (Player invite : invites) {
            player.sendMessage(" - "+invite.getName());
        }
        player.sendMessage("Specify a player in your tpaccept command");
    }

    @Command("tpadeny")
    @Description("Deny a teleportation request")
    public void tpadeny(CommandSender sender, @Named("target") Player target) {
        if (!(sender instanceof Player player)) { return; }

        List<Player> invites = getInvitesTo(player);

        if (target != null) {
            if (!invites.contains(target)) {
                player.sendMessage("§c"+target.getName()+" hasn't send you any teleportation requests");
                return;
            } else {
                if (invites.get(invites.indexOf(target)) != target) {
                    player.sendMessage("§c"+target.getName()+" hasn't send you any teleportation requests");
                    return;
                } else {
                    tpa_requests.remove(target);
                    target.sendMessage("§c"+target.getName()+" has deny your teleportation request");
                    return;
                }
            }
        }

        if (invites.size() == 1) {
            Player newTarget = invites.getFirst();
            tpa_requests.remove(newTarget);
            newTarget.sendMessage("§c"+newTarget.getName()+" has deny your teleportation request");
            return;
        }
        player.sendMessage("You have multiples invitations:");
        for (Player invite : invites) {
            player.sendMessage(" - "+invite.getName());
        }
        player.sendMessage("Specify a player in your tpadeny command");
    }
}
