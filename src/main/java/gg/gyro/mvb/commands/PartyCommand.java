package gg.gyro.mvb.commands;

import gg.gyro.mvb.MVB;
import gg.gyro.mvb.utils.Party;
import gg.gyro.mvb.utils.Privacy;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import revxrsal.commands.annotation.*;
import revxrsal.commands.annotation.Optional;

import java.util.*;

@Command("party")
@Description("Party with others players")
public class PartyCommand implements Listener {
    Set<Party> parties = new HashSet<>();
    HashMap<Player, Party> invites = new HashMap<>();
    Privacy privacy;
    MVB plugin;

    public PartyCommand(MVB plugin) {
        this.plugin = plugin;
        privacy = new Privacy(plugin);
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    private String prettyWorldName(World world){
        return switch (world.getName()) {
            case "world":
                yield "§aOverworld";
            case "world_nether":
                yield "§4Nether";
            case "world_the_end":
                yield "§dEnd";
            default:
                yield world.getName();
        };
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

    @Subcommand("kick")
    @Description("Kick someone of your party")
    public void kick(CommandSender sender, @Named("target") Player target, @Optional String reason){
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        Party tParty = getPlayerParty(player);

        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        if (pParty != tParty) {
            player.sendMessage("§cYou are not in the same party as "+target.getName());
            return;
        }

        if (!pParty.isPlayerOwner(player)) {
            player.sendMessage("§cYou are not owner of the party!");
            return;
        }

        pParty.removeMember(target);
        target.sendMessage("You have been kicked of the party!");
        if (reason != null) {
            reason = "\nReason: "+reason;
        }

        pParty.broadcast(target.getName()+" has been kicked of your party"+reason);
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

        if (!privacy.getScope(target, "pinvites")) {
            player.sendMessage("§c"+target.getName()+" does not accept invitations!");
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
        player.sendMessage("§a"+target.getName()+" has been invited to your party");
        target.sendMessage("§6"+player.getName()+"§r invited you to a party");

        TextComponent acceptButton = new TextComponent("Click to accept");
        acceptButton.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept"));
        target.spigot().sendMessage(acceptButton);
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
        newParty.addMember(player);
        newParty.broadcast(player.getName()+" has joined your party!");
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

    @Command("pc")
    @SecretCommand()
    @Description("Same as /party chat")
    public void chat_alias(CommandSender sender, @Named("message") String message){
        chat(sender, message);
    }

    @Subcommand("coords")
    @Description("Get party's members coordinates")
    public void coords(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        Party pParty = getPlayerParty(player);
        if (pParty == null) {
            player.sendMessage("§cYou are not member of a party.");
            return;
        }

        HashMap<String, List<Player>> dimensions = new HashMap<>();

        for (Player member : pParty.getMembers()) {
            if (!privacy.getScope(member, "party_coords")) { continue; }

            String worldName = prettyWorldName(member.getWorld());
            dimensions.computeIfAbsent(worldName, k -> new ArrayList<>()).add(member);
        }

        if (dimensions.isEmpty()) {
            player.sendMessage("§cNone of your teammates share their coordinates");
            return;
        }

        for (Map.Entry<String, List<Player>> entry : dimensions.entrySet()) {
            String worldName = entry.getKey();
            List<Player> players = entry.getValue();
            player.sendMessage("§a" + worldName + " ("+players.size()+"):");
            for (Player p : players) {
                Location loc = p.getLocation();
                player.sendMessage(" §6-§r " + p.getName()+" ("+loc.getBlockX()+", "+loc.getBlockY()+", "+loc.getBlockZ()+")");
            }
        }
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
