package gg.gyro.mab.commands;

import gg.gyro.mab.MAB;
import gg.gyro.mab.utils.Party;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import revxrsal.commands.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Command("party")
public class PartyCommand implements Listener {
    Set<Party> parties = new HashSet<>();
    HashMap<Player, Party> invites = new HashMap<>();

    public PartyCommand(MAB plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Party pParty = getPlayerParty(player);

        if (pParty == null) { return; }

        pParty.broadcast(player.getName()+" leaved your party!");
        pParty.removeMember(player);

        if (pParty.getMembers().isEmpty()) {
            parties.remove(pParty);
        }
    }

    public Party getPlayerParty(Player player) {
        for (Party party : parties) {
            if (party.getMembers().contains(player)) {
                return party;
            }
        }
        return null;
    }

    @Subcommand("transfer")
    @Description("Transfer ownership")
    public void transfer(CommandSender sender, @Named("NewOwner") Player newOwner) {
        if (!(sender instanceof Player player)) { return; }
        Party party = getPlayerParty(newOwner);
        if (party == null) {
            player.sendMessage("§cYou are not member of a party!");
            return;
        }
        if (!party.isPlayerOwner(player)) {
            player.sendMessage("§cYou are not owner of this party!");
        }

        if (player.equals(newOwner)) {
            player.sendMessage("§cYou are already owner of this party!");
            return;
        }

        party.changeOwner(newOwner);
        party.broadcast("Ownership has been transfered to §6"+newOwner.getName());
    }

    @Subcommand("create")
    @Description("Create a party")
    public void create(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        if (getPlayerParty(player) != null) {
            sender.sendMessage("§cYou are already a member of a party.");
            return;
        }

        Party party = new Party(player);
        parties.add(party);

        player.sendMessage(ChatColor.GREEN+"You have created a party!");
    }

    @Subcommand("invite")
    @Description("Invite someone to your party")
    public void invite(CommandSender sender, @Named("target") Player target) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        if (!pParty.isPlayerOwner(player)) {
            player.sendMessage("§cYou are not owner of the party.");
            return;
        }

        if (getPlayerParty(target) != null) {
            player.sendMessage("§c"+target.getName()+" is already a member of a party.");
            return;
        }

        if (invites.containsKey(target)) {
            player.sendMessage("§c"+target.getName()+" already has a pending invitation.");
            return;
        }

        invites.put(target, pParty);
        player.sendMessage("§a"+player.getName()+" has been invited to your party");
        target.sendMessage("§6"+player.getName()+"§r invited you to a party");
        target.sendMessage("Type §e/party accept§r to accept");
    }

    @Subcommand("members")
    @Description("Show every member of your party")
    public void members(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);

        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        player.sendMessage("§6Your party's members:");
        for (Player member : pParty.getMembers()) {
            if (pParty.isPlayerOwner(member)) {
                player.sendMessage("§4-§r §e★§r "+member.getName());
            } else {
                player.sendMessage("§4-§r "+member.getName());
            }
        }
    }

    @Subcommand("accept")
    @Description("Accept a pending invitation")
    public void accept(CommandSender sender) {
        if (!(sender instanceof Player player)) { return;}
        if (!(invites.containsKey(player))) {
            player.sendMessage("§cYou have no pending invitation.");
            return;
        }

        Party pParty = getPlayerParty(player);
        if (!(pParty == null)) {
            player.sendMessage("§cYou are already a member of a party.");
            return;
        }

        Party newParty = invites.get(player);
        newParty.broadcast(player.getName()+" has joined your party!");
        newParty.addMember(player);
        invites.remove(player);
    }

    @Subcommand("refuse")
    @Description("Refuse a pending invitation")
    public void refuse(CommandSender sender) {
        if (!(sender instanceof Player player)) { return;}

        if (!(invites.containsKey(player))) {
            player.sendMessage("§cYou have no pending invitation.");
            return;
        }

        Party newParty = invites.get(player);
        newParty.broadcast(player.getName()+" has refused your party!");
        invites.remove(player);
    }

    @Subcommand("chat")
    @Description("Send a message to your party")
    public void chat(CommandSender sender, @Named("message") String message) {
        if (!(sender instanceof Player player)) { return;}
        if (getPlayerParty(player) == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        Party pParty = getPlayerParty(player);
        pParty.sendToAll(player, message);
    }

    @Subcommand("leave")
    @Description("Leave a party")
    public void leave(CommandSender sender, @Named("reason") @Optional String reason) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        pParty.removeMember(player);
        if (reason != null){
            pParty.broadcast(player.getName()+" leaved your party\nReason: "+reason);
        } else {
            pParty.broadcast(player.getName()+" leaved your party");
        }

        if (pParty.getMembers().isEmpty()) {
            parties.remove(pParty);
        }
        
        player.sendMessage("§aYou have leaved your party");
    }

    @Subcommand("dissolve")
    @Description("Dissolve a party")
    public void dissolve(CommandSender sender, @Named("reason") @Optional String reason) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        if (!(pParty.isPlayerOwner(player))) {
            player.sendMessage("§cYou are not owner of this party.");
            return;
        }

        if (reason == null) {
            pParty.broadcast("This party has been dissolved by " + player.getName());
        } else {
            pParty.broadcast("This party has been dissolved by " + player.getName() + "\nReason: "+reason);
        }
        pParty.dissolve();
    }
}
