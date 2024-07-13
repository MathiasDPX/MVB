package gg.gyro.mab.commands;

import gg.gyro.mab.utils.Party;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import revxrsal.commands.annotation.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@Command("party")
public class PartyCommand {
    Set<Party> parties = new HashSet<>();
    HashMap<Player, Party> invites = new HashMap<>();

    public Party getPlayerParty(Player player) {
        for (Party party : parties) {
            if (party.getMembers().contains(player)) {
                return party;
            }
        }
        return null;
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
            player.sendMessage("§cYou are not a member of a party.");
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
        target.sendMessage("§6"+player.getName()+"§r invited you to a party");
        target.sendMessage("Type §e/party accept§r to accept");
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
            player.sendMessage("§cYou are not a member of a party.");
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
            player.sendMessage("§cYou are not a member of a party.");
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
    }

    @Subcommand("dissolve")
    @Description("Dissolve a party")
    public void dissolve(CommandSender sender, @Named("reason") @Optional String reason) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        if (pParty == null) {
            player.sendMessage("§cYou are not a member of a party.");
            return;
        }

        if (!(pParty.isPlayerOwner(player))) {
            player.sendMessage("§cYou are not owner of this party.");
            return;
        }

        if (reason == null) {
            pParty.broadcast("This party was dissolved by " + player.getName());
        } else {
            pParty.broadcast("This party was dissolved by " + player.getName() + "\nReason: "+reason);
        }
        pParty.dissolve();
    }
}
