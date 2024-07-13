package gg.gyro.mab.commands;

import gg.gyro.mab.MAB;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import revxrsal.commands.annotation.Command;
import revxrsal.commands.annotation.Subcommand;

@Command("utility")
public class UtilityCommand {
    private final MAB plugin;

    public UtilityCommand(MAB plugin) {
        this.plugin = plugin;
    }

    private void openInventory(CommandSender sender, InventoryType type, String configKey) {
        if (!plugin.getConfig().getBoolean(configKey)) {
            sender.sendMessage("§cThis feature is disabled.");
            return;
        }
        if (!(sender instanceof Player player)) { return; }

        Inventory inv = Bukkit.createInventory(player, type);
        player.openInventory(inv);
    }

    @Subcommand("anvil")
    public void anvil(CommandSender sender) {
        openInventory(sender, InventoryType.ANVIL, "utility_block.anvil");
    }

    @Subcommand("brewing_stand")
    public void brewingStand(CommandSender sender) {
        openInventory(sender, InventoryType.BREWING, "utility_block.brewing_stand");
    }

    @Subcommand("cartography_table")
    public void cartographyTable(CommandSender sender) {
        openInventory(sender, InventoryType.CARTOGRAPHY, "utility_block.cartography_table");
    }

    @Subcommand("crafting_table")
    public void craftingTable(CommandSender sender) {
        openInventory(sender, InventoryType.WORKBENCH, "utility_block.crafting_table");
    }

    @Subcommand("ender_chest")
    public void enderChest(CommandSender sender) {
        if (!(sender instanceof Player player)) { return; }
        if (plugin.getConfig().getBoolean("utility_block.ender_chest")) { return; }

        Inventory ec = player.getEnderChest();
        player.openInventory(ec);
    }

    @Subcommand("grindstone")
    public void grindstone(CommandSender sender) {
        openInventory(sender, InventoryType.GRINDSTONE, "utility_block.grindstone");
    }

    @Subcommand("loom")
    public void loom(CommandSender sender) {
        openInventory(sender, InventoryType.LOOM, "utility_block.loom");
    }

    @Subcommand("smithing_table")
    public void smithingTable(CommandSender sender) {
        openInventory(sender, InventoryType.SMITHING, "utility_block.smithing_table");
    }

    @Subcommand("stonecutter")
    public void stonecutter(CommandSender sender) {
        openInventory(sender, InventoryType.STONECUTTER, "utility_block.stonecutter");
    }
}