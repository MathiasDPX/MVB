package gg.gyro.mab.utils;

import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;

public class Party {
    Player owner;
    Set<Player> members = new HashSet<Player>();

    public Party(Player owner) {
        this.owner = owner;
        members.add(owner);
    }

    public Player getOwner() { return owner; }
    public Set<Player> getMembers() { return members; }

    public void changeOwner(Player newOwner) {
        owner = newOwner;
    }

    public void addMember(Player newMember) {
        members.add(newMember);
    }

    public void removeMember(Player oldMember) {
        members.remove(oldMember);
    }

    public void sendToAll(Player sender, String message) {
        for (Player p : members) {
            p.sendMessage("§6#party§r ["+sender.getName()+"]"+message);
        }
    }

    public void dissolve() {
        for (Player p : members) {
            members.remove(p);
        }
    }

    public boolean isPlayerInParty(Player p) {
        return members.contains(p);
    }

    public boolean isPlayerOwner(Player p) {
        return p.equals(owner);
    }

    public void broadcast(String message) {
        for (Player p : members) {
            p.sendMessage("§6#party§r: "+message);
        }
    }
}
