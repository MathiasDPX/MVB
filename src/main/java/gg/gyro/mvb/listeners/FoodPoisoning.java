package gg.gyro.mvb.listeners;

import gg.gyro.mvb.MVB;
import gg.gyro.mvb.utils.Lists;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class FoodPoisoning implements Listener {
    MVB plugin;

    public FoodPoisoning(MVB plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerEat(PlayerItemConsumeEvent event){
        if (!plugin.getConfig().getBoolean("features.food_poisoning")) { return; }

        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        String namespace = item.getType().name().toLowerCase();

        if (Lists.isRaw(namespace)) {
            if (new Random().nextDouble() <= plugin.getConfig().getDouble("food_poisoning.chance")) {
                player.sendMessage("beurk!");
                player.addPotionEffect(new PotionEffect(
                        PotionEffectType.NAUSEA,
                        plugin.getConfig().getInt("food_poisoning.duration")*20,
                        plugin.getConfig().getInt("food_poisoning.amplifier")
                ));
            }
        }
    }
}
