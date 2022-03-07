package com.pdev.drduels.api.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public class QuitPlayer {
    private Location lastLocation;
    private ItemStack[] contents;
    private ItemStack[] armor;
    private GameMode gameMode;

    public QuitPlayer(Location lastLocation, ItemStack[] contents, ItemStack[] armor, GameMode gameMode) {
        this.lastLocation = lastLocation;
        this.armor = armor;
        this.contents = contents;
        this.gameMode = gameMode;
    }

    public Location getLastLocation() {
        return lastLocation;
    }

    public ItemStack[] getArmor() {
        return armor;
    }

    public ItemStack[] getContents() {
        return contents;
    }

    public GameMode getGameMode() {
        return gameMode;
    }
}
