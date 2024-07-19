package gg.gyro.mvb.listeners;

import gg.gyro.mvb.MVB;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class DurabilityPing implements Listener {
    MVB plugin;
    public DurabilityPing(MVB plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDurabilityPing(PlayerItemDamageEvent e) {
        Player p = e.getPlayer();
        ItemStack item = e.getItem();
        int durability = item.getDurability();
        durability = (item.getType().getMaxDurability() - durability) - e.getDamage();

        if (!(this.plugin.getConfig().getBoolean("features.durability_ping"))) { return; }
        double trigger = this.plugin.getConfig().getDouble("durability_ping.trigger");

        p.sendMessage(String.valueOf(trigger));
        p.sendMessage(String.valueOf(durability));
        if ((trigger > 0) && (trigger < 1)) {
            trigger = item.getType().getMaxDurability() * trigger;
            trigger = Math.round(trigger);
        }
        trigger = (short) trigger;
        if (!(durability <= trigger)){ return; }
        Sound warningSound = Sound.valueOf(Objects.requireNonNull(this.plugin.getConfig().getString("durability_ping.warning_sound"), "BLOCK_ANVIL_LAND"));
        String warningMessage = Objects.requireNonNull(this.plugin.getConfig().getString("durability_ping.warning_message"), "§cYour item will break soon!");

        p.playSound(p.getEyeLocation(), warningSound, 16, 1);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(warningMessage));
    }
}
