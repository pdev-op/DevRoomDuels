package com.pdev.drduels.api.player;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class StatsPlayer {
    private UUID uuid;
    private int wins;
    private int losses;
    private int kills;
    private int deaths;
    private int winStreak;

    public StatsPlayer(UUID uuid) {
        this.uuid = uuid;
        wins = 0;
        losses = 0;
        kills = 0;
        deaths = 0;
        winStreak = 0;
    }

    public StatsPlayer(UUID uuid, int wins, int losses, int kills, int deaths, int winStreak) {
        this.uuid = uuid;
        this.wins = wins;
        this.losses = losses;
        this.kills = kills;
        this.deaths = deaths;
        this.winStreak = winStreak;
    }

    public Player getBukkitPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public int getKills() {
        return kills;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public void setDeaths(int deaths) {
        this.deaths = deaths;
    }

    public int getWinStreak() {
        return winStreak;
    }

    public void setWinStreak(int winStreak) {
        this.winStreak = winStreak;
    }
}
