package gg.gyro.mvb.listeners;

import gg.gyro.mvb.MVB;
import gg.gyro.mvb.utils.Privacy;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PingPlayer implements Listener {
    MVB plugin;
    Privacy privacy;

    public PingPlayer(MVB plugin) {
        privacy = new Privacy(plugin);
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.equals(event.getPlayer())) { return; }
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {
                if (!(privacy.getScope(player, "ping"))) { return; }
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        });
    }
}
