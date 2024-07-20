package gg.gyro.mvb.listeners;

import gg.gyro.mvb.MVB;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class DeathCompass implements Listener {
    MVB plugin;

    HashMap<Player, Location> deathLocations = new HashMap<>();

    public DeathCompass(MVB plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        deathLocations.put(p, p.getLocation());

        e.getDrops().removeIf(d -> Objects.requireNonNull(d.getItemMeta()).getItemName().equals("Death Compass"));
        String deathMessage = plugin.getConfig().getString("death_compass.message");
        deathMessage = Objects.requireNonNull(deathMessage, "");

        deathMessage = deathMessage.replace("$x", String.valueOf(p.getLocation().getBlockX()));
        deathMessage = deathMessage.replace("$y", String.valueOf(p.getLocation().getBlockY()));
        deathMessage = deathMessage.replace("$z", String.valueOf(p.getLocation().getBlockZ()));

        p.sendMessage(deathMessage);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (!(plugin.getConfig().getBoolean("death_compass.compass"))){ return; }

        Player p = e.getPlayer();
        Location deathLocation = deathLocations.get(p);
        int deathX = deathLocation.getBlockX();
        int deathY = deathLocation.getBlockY();
        int deathZ = deathLocation.getBlockZ();

        ItemStack compass = new ItemStack(Material.RECOVERY_COMPASS, 1);
        ItemMeta itemMeta = compass.getItemMeta();
        assert itemMeta != null;

        itemMeta.setItemName("Death Compass");
        itemMeta.addEnchant(Enchantment.BINDING_CURSE, 1, false);
        itemMeta.setLore(List.of(
                "Corpse of "+p.getName(),
                "X: "+deathX,
                "Y: "+deathY,
                "Z: "+deathZ,
                "§cCurse of vanishing§r"
        ));

        compass.setItemMeta(itemMeta);
        p.getInventory().addItem(compass);
    }
}
