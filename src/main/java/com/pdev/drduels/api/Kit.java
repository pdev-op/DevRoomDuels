package com.pdev.drduels.api;

import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Kit {
    private Map<Integer, ItemStack> inventoryContent;
    private Map<Integer, ItemStack> armorContent;
    private String name;
    
    public Kit(String name, Map<Integer, ItemStack> inventoryContent, Map<Integer, ItemStack> armorContent) {
        this.name = name;
        this.inventoryContent = inventoryContent;
        this.armorContent = armorContent;
    }

    public String getName() {
        return name;
    }

    public void equip(Player player) {
        // Armor
        ItemStack[] armor = new ItemStack[4];
        armorContent.forEach((slot, piece) -> {
            armor[slot] = piece;
        });

        player.getInventory().setArmorContents(armor);

        // Inventory
        inventoryContent.forEach((slot, item) -> {
            player.getInventory().setItem(slot, item);
        });
    }
}
