package com.pdev.drduels.api.duel;

import java.util.UUID;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.Kit;
import com.pdev.drduels.api.player.DuelPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Duel {
    private DuelPlayer playerOne;
    private DuelPlayer playerTwo;
    private Kit kit;
    private DuelState state;
    private Integer timerID;
    private int seconds;
    private DuelResult result;

    public Duel(Player playerOne, Player playerTwo, Kit kit) {
        // Players
        this.playerOne = new DuelPlayer(playerOne);
        this.playerTwo = new DuelPlayer(playerTwo);
        this.seconds = 1;

        // Result (initialized to be null, set at the conclusion of the duel via event)
        this.result = null;

        // Kit to use
        this.kit = kit;
    }

    public Kit getKit() {
        return kit;
    }

    public DuelPlayer getPlayerOne() {
        return playerOne;
    }

    public DuelPlayer getPlayerTwo() {
        return playerTwo;
    }

    public DuelState getState() {
        return state;
    }

    public void setResult(DuelResult result) {
        this.result = result;
    }

    public DuelResult getDuelResult() {
        return result;
    }

    public DuelPlayer getLoser() {
        if (result != null) {
            if (playerOne.isLoser(result.getLoser())) {
                return playerOne;
            } else if (playerTwo.isLoser(result.getLoser())) {
                return playerTwo;
            }
        }

        return null;
    }

    public DuelPlayer getWinner() {
        if (result != null) {
            if (!playerOne.isLoser(result.getLoser())) {
                return playerOne;
            } else {
                return playerTwo;
            }
        }

        return null;
    }

    public void setState(DuelState state) {
        // Start the timer
        if (state == DuelState.ONGOING) {
            timerID = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
                playerOne.sendTimeMessage(seconds);
                playerTwo.sendTimeMessage(seconds);
                seconds++;
            }, 20L, 20L);
        } else if (state.equals(DuelState.FINISHED)) {
            if (timerID != null) {
                Bukkit.getScheduler().cancelTask(timerID);
            }
        }
        this.state = state;
    }

    public boolean isInDuel(Player player) {
        UUID uuid = player.getUniqueId();

        return uuid.equals(playerOne.getUuid()) || uuid.equals(playerTwo.getUuid());
    }

    public void sendWaitingMessage() {
        playerOne.sendWaitingMessage();
        playerTwo.sendWaitingMessage();
    }
}
