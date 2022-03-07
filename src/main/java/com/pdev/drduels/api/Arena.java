package com.pdev.drduels.api;

import java.time.Duration;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.player.DuelPlayer;
import com.pdev.drduels.api.player.QuitPlayer;
import com.pdev.drduels.api.player.StatsPlayer;
import com.pdev.drduels.managers.PlayerManager;
import com.pdev.drduels.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class Arena {
    private Main plugin;
    private String worldName;
    private Location spawnOne;
    private Location spawnTwo;
    private Duel duel;

    public Arena(String worldName, Location spawnOne, Location spawnTwo) {
        this.plugin = Main.getInstance();
        this.worldName = worldName;
        this.spawnOne = spawnOne;
        this.spawnTwo = spawnTwo;
        this.duel = null;
    }

    public Arena() {
        this.worldName = null;
        this.spawnOne = null;
        this.spawnTwo = null;
        this.duel = null;
    }

    public World getWorld() {
        return Bukkit.getWorld(worldName);
    }

    public void setWorldName(String worldName) {
        this.worldName = worldName;
    }

    public Location getSpawnOne() {
        return spawnOne;
    }

    public void setSpawnOne(Location spawnOne) {
        this.spawnOne = spawnOne;
    }

    public Location getSpawnTwo() {
        return spawnTwo;
    }

    public void setSpawnTwo(Location spawnTwo) {
        this.spawnTwo = spawnTwo;
    }

    public Duel getDuel() {
        return duel;
    }

    public Duel getActiveDuel() {
        if (duel != null) {
            if (!duel.getState().equals(DuelState.FINISHED)) {
                return duel;
            } else {
                // Set duel to null since it's finished
                duel = null;
            }
        }

        return null;
    }

    private void prepPlayer(Player player, Location location, Kit kit) {
        player.teleportAsync(location, TeleportCause.PLUGIN);
        player.getInventory().clear();
        kit.equip(player);
        player.setGameMode(GameMode.SURVIVAL);
        player.setFoodLevel(20);
        player.setSaturation(5);
        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
    }

    public void startDuel(Duel duel) {
        // Set the Duel, Queuing is handled by the Manager
        this.duel = duel;

        // Start Timing, Teleport & Prevent Movement
        Player playerOne = Bukkit.getPlayer(duel.getPlayerOne().getUuid());
        Player playerTwo = Bukkit.getPlayer(duel.getPlayerTwo().getUuid());

        if (playerOne == null || playerTwo == null) {
            if (playerOne != null) {
                playerOne.sendMessage(
                        StringUtils.colorize("&cYour opponent is no longer online! Duel has been cancelled."));
            }

            if (playerTwo != null) {
                playerTwo.sendMessage(
                        StringUtils.colorize("&cYour opponent is no longer online! Duel has been cancelled."));
            }

            return;
        }

        // Prep Players
        prepPlayer(playerOne, getSpawnOne(), duel.getKit());
        prepPlayer(playerTwo, getSpawnTwo(), duel.getKit());

        // Countdown
        Countdown countdown = new Countdown(plugin,
                5,
                () -> {
                    // Upon completion
                    Title title = Title.title(
                            Component.text(StringUtils.colorize("&c&lFIGHT!!!")),
                            Component.empty(),
                            Title.Times.of(Duration.ofMillis(0), Duration.ofMillis(1000), Duration.ofMillis(500)));

                    playerOne.showTitle(title);
                    playerTwo.showTitle(title);

                    // Release players, Let them fight!
                    duel.setState(DuelState.ONGOING);
                },
                (time) -> {
                    // Every second
                    Duration d = Duration.ofMillis(0);

                    if (time.getSecondsLeft() == 5) {
                        d = Duration.ofMillis(500);
                    }

                    Title title = Title.title(
                            Component.text(StringUtils.colorize("&eFighting in &a" + time.getSecondsLeft())),
                            Component.text(StringUtils.colorize("&fGet ready to duel!")),
                            Title.Times.of(d, Duration.ofMillis(1001), Duration.ofMillis(0)));

                    playerOne.showTitle(title);
                    playerTwo.showTitle(title);
                });

        countdown.schedule();
    }

    public void handleResult(DuelResult result) {
        // Set Result
        duel.setResult(result);

        // PlayerManager
        PlayerManager pm = plugin.getPlayerManager();

        // Players
        DuelPlayer winner = duel.getWinner();
        DuelPlayer loser = duel.getLoser();

        // Stats
        StatsPlayer winnerStats = pm.getPlayer(winner.getUuid());
        StatsPlayer loserStats = pm.getPlayer(loser.getUuid());

        // Winner
        pm.setWins(winner.getUuid(), winnerStats.getWins() + 1);
        pm.setWinStreak(winner.getUuid(), winnerStats.getWinStreak() + 1);

        // Loser
        pm.setLosses(loser.getUuid(), loserStats.getLosses() + 1);
        pm.setWinStreak(loser.getUuid(), 0);

        // Adjust Kills / Deaths if from a kill
        if (result.fromKill()) {
            pm.setKills(winner.getUuid(), winnerStats.getKills() + 1);
            pm.setDeaths(loser.getUuid(), loserStats.getDeaths() + 1);
        }

        // Reset Players / messaging
        loser.resetAfterDuel(true);
        winner.resetAfterDuel(false);

        // If not from a kill then it is from a quit, there for we add a quit player
        if (!result.fromKill()) {
            QuitPlayer qp = new QuitPlayer(loser.getLastLocation(), loser.getContents(), loser.getArmor(),
                    loser.getGameMode());
            pm.addQuitPlayer(loser.getUuid(), qp);
        }

        // Duel state
        duel.setState(DuelState.FINISHED);
    }
}
