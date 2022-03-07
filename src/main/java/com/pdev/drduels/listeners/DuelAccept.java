package com.pdev.drduels.listeners;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.Duel;
import com.pdev.drduels.api.DuelState;
import com.pdev.drduels.api.Request;
import com.pdev.drduels.api.events.AcceptEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelAccept implements Listener {
    private Main plugin;

    public DuelAccept(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onAccept(AcceptEvent e) {
        Request request = e.getRequest();

        Duel duel = new Duel(request.getSender(), request.getTarget(), request.getKit());
        duel.setState(DuelState.STARTING);

        plugin.getArenaManager().addDuel(duel);
    }
}
