package gg.gyro.mab.listeners;

import gg.gyro.mab.MAB;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.world.TimeSkipEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

public class Insomnia implements Listener {
    public Set<Player> playersWhoSlept = new HashSet<>();
    MAB plugin;

    public Insomnia(MAB plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        if (event.getBedEnterResult() == PlayerBedEnterEvent.BedEnterResult.OK) {
            playersWhoSlept.add(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        playersWhoSlept.remove(event.getPlayer());
    }

    @EventHandler
    public void onTimeSkip(TimeSkipEvent event) {
        if (event.getSkipReason() != TimeSkipEvent.SkipReason.NIGHT_SKIP) { return; }
        if (playersWhoSlept.isEmpty()) { return; }
        if (!(this.plugin.getConfig().getBoolean("features.insomnia"))) { return; } // Verify if insomnia is enabled

        for (Player player : playersWhoSlept) {
            if (new Random().nextDouble() > this.plugin.getConfig().getDouble("insomnia.chance")) { return; }

            System.out.println("Giving insomnia to "+player.getName());
            String message = Objects.requireNonNull(this.plugin.getConfig().getString("insomnia.message"), "You've did an insomnia");
            player.sendMessage(message);
            player.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 200, 1)); // Slowness II
            player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 200, 0)); // Weakness
            player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS , 200, 0)); // Blindness
        }
        playersWhoSlept.clear();
    }
}