package gg.gyro.mvb.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Description;

public class WikiCommand {

    @Command({"mcwiki", "wiki"})
    @Description("Open wiki link for item")
    public void onCommand(Player player) {
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            String itemName = heldItem.getType().name();
            String link = "https://minecraft.wiki/w/"+itemName.toLowerCase();
            BaseComponent[] component =
                    new ComponentBuilder("Page of "+ChatColor.YELLOW+itemName)
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
                            .create();
            player.spigot().sendMessage(component);
        } else {
            player.sendMessage(ChatColor.RED+"You need to hold an item in your hand."+ChatColor.RESET);
        }
    }
}
