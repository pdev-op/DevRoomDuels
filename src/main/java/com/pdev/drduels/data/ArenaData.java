package com.pdev.drduels.data;

import com.pdev.drduels.api.Arena;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class ArenaData {
    public String world;
    public Spawn spawnOne;
    public Spawn spawnTwo;

    public ArenaData(String world, Spawn spawnOne, Spawn spawnTwo) {
        this.world = world;
        this.spawnOne = spawnOne;
        this.spawnTwo = spawnTwo;
    }

    public Arena getArena() {
        return new Arena(
                world,
                new Location(Bukkit.getWorld(world), spawnOne.getX(), spawnOne.getY(), spawnOne.getZ()),
                new Location(Bukkit.getWorld(world), spawnTwo.getX(), spawnTwo.getY(), spawnTwo.getZ()));
    }

    public class Spawn {
        private Double x;
        private Double y;
        private Double z;

        public Spawn(Double x, Double y, Double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Double getZ() {
            return z;
        }
    }
}