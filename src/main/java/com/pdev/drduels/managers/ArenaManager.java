package com.pdev.drduels.managers;

import java.util.LinkedList;
import java.util.Queue;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.Arena;
import com.pdev.drduels.api.duel.Duel;
import com.pdev.drduels.api.duel.DuelResult;

import org.bukkit.entity.Player;

public class ArenaManager {
    private Arena arena;
    private Queue<Duel> duelQueue;

    public ArenaManager(Main plugin) {
        this.arena = plugin.getArenaFile().getArena();
        this.duelQueue = new LinkedList<Duel>();
    }

    public Arena getArena() {
        return arena;
    }

    public boolean isInDuel(Player player) {
        if (arena.getActiveDuel() == null) {
            return false;
        }

        return arena.getActiveDuel().isInDuel(player);
    }

    public void handleResult(DuelResult result) {
        // Handle result
        arena.handleResult(result);

        // Since that duel is over, start a new one from the queue
        if (duelQueue.peek() != null) {
            startDuel(duelQueue.remove());
        }
    }

    public void addDuel(Duel duel) {
        if (arena.getActiveDuel() == null) {
            startDuel(duel);
        } else {
            duelQueue.add(duel);
            duel.sendWaitingMessage();
        }
    }

    public void startDuel(Duel duel) {
        arena.startDuel(duel);
    }
}
