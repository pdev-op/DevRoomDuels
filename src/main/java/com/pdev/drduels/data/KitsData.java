package com.pdev.drduels.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.pdev.drduels.api.Kit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class KitsData {
    private String defaultKit;
    private List<KitData> kits;

    public KitsData(String defaultKit, List<KitData> kits) {
        this.defaultKit = defaultKit;
        this.kits = kits;
    }

    public String getDefaultKit() {
        return defaultKit;
    }

    public Map<String, Kit> getKits() {
        Map<String, Kit> newKits = new HashMap<>();

        for (KitData kd : kits) {
            newKits.put(kd.getName(), kd.getKit());
        }

        return newKits;
    }

    public class KitData {
        private String name;
        private ArmorContent armorContent;
        private List<JsonObject> inventoryContent;

        public KitData(String name, ArmorContent armorContent, List<JsonObject> inventoryContent) {
            this.name = name;
            this.armorContent = armorContent;
            this.inventoryContent = inventoryContent;
        }

        public Kit getKit() {
            Map<Integer, ItemStack> armor = armorContent.getArmor();
            Map<Integer, ItemStack> items = new HashMap<>();

            for (JsonObject jso : inventoryContent) {
                Material mat = Material.getMaterial(jso.get("material").getAsString());
                ItemStack item = new ItemStack(mat, jso.get("amount").getAsInt());
                int slot = jso.get("slot").getAsInt();

                items.put(slot, item);
            }

            return new Kit(name, items, armor);
        }

        public String getName() {
            return name;
        }
    }

    public class ArmorContent {
        private Armor helmet;
        private Armor chestplate;
        private Armor leggings;
        private Armor boots;

        public ArmorContent(Armor helmet, Armor chestplate, Armor leggings, Armor boots) {
            this.helmet = helmet;
            this.chestplate = chestplate;
            this.leggings = leggings;
            this.boots = boots;
        }

        public Map<Integer, ItemStack> getArmor() {
            Map<Integer, ItemStack> armor = new HashMap<>();

            if (helmet != null) {
                armor.put(3, helmet.getArmorItem());
            } else {
                armor.put(3, (ItemStack) null);
            }

            if (chestplate != null) {
                armor.put(2, chestplate.getArmorItem());
            } else {
                armor.put(2, (ItemStack) null);
            }

            if (leggings != null) {
                armor.put(1, leggings.getArmorItem());
            } else {
                armor.put(1, (ItemStack) null);
            }

            if (boots != null) {
                armor.put(0, boots.getArmorItem());
            } else {
                armor.put(0, (ItemStack) null);
            }

            return armor;
        }
    }

    public class Armor {
        public String material;
        public Integer amount;

        public Armor(String material, Integer amount) {
            this.material = material;
            this.amount = amount;
        }

        public ItemStack getArmorItem() {
            if (material == null || amount == null) {
                return (ItemStack) null;
            }

            Material mat = Material.matchMaterial(material);

            return new ItemStack(mat, amount);
        }
    }
}
