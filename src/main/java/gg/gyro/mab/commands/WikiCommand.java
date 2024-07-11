package gg.gyro.mab.commands;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TranslatableComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import revxrsal.commands.annotation.Command;

public class WikiCommand {

    @Command({"mcwiki", "wiki"})
    public void onCommand(Player player) {
        if (player.getInventory().getItemInMainHand().getItemMeta() != null) {
            ItemStack heldItem = player.getInventory().getItemInMainHand();
            String link = "https://minecraft.wiki/w/"+heldItem.getType().name().toLowerCase();
            TranslatableComponent itemName = new TranslatableComponent(player.getInventory().getItemInMainHand().getType().getTranslationKey());
            BaseComponent[] component =
                    new ComponentBuilder("Page du wiki de "+ChatColor.YELLOW+heldItem.getType().name())
                            .event(new ClickEvent(ClickEvent.Action.OPEN_URL, link))
                            .create();
            player.spigot().sendMessage(component);
        } else {
            player.sendMessage(ChatColor.RED+"Vous devez tenir un item en main."+ChatColor.RESET);
        }
    }
}
