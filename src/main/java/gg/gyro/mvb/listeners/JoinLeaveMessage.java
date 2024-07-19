package gg.gyro.mvb.listeners;

import gg.gyro.mvb.MVB;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinLeaveMessage implements Listener {
    MVB plugin;

    public JoinLeaveMessage(MVB plugin) {
        this.plugin = plugin;

        if (!(this.plugin.getConfig().getBoolean("features.joinleave"))) {
            return;
        }

        if (this.plugin.getConfig().getString("joinleave.join") == null) {
            plugin.getLogger().severe("Join message is null!");
        }

        if (this.plugin.getConfig().getString("joinleave.leave") == null) {
            plugin.getLogger().severe("Leave message is null!");
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!(this.plugin.getConfig().getBoolean("features.joinleave"))) {
            return;
        }

        String newMessage = this.plugin.getConfig().getString("joinleave.join");
        newMessage = newMessage.replace("$player", event.getPlayer().getName());
        event.setJoinMessage(newMessage);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (!(this.plugin.getConfig().getBoolean("features.joinleave"))) {
            return;
        }

        String newMessage = this.plugin.getConfig().getString("joinleave.leave");
        newMessage = newMessage.replace("$player", event.getPlayer().getName());
        event.setQuitMessage(newMessage);
    }
}
