package gg.gyro.mab.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PingPlayer implements Listener {
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();

        Bukkit.getOnlinePlayers().forEach(player -> {
            if (player.equals(event.getPlayer())) { return; }
            if (message.toLowerCase().contains(player.getName().toLowerCase())) {
                player.playSound(player.getEyeLocation(), Sound.BLOCK_NOTE_BLOCK_BELL, 1, 1);
            }
        });
    }
}