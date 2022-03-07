package com.pdev.drduels.listeners;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.Kit;
import com.pdev.drduels.api.events.RequestEvent;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DuelRequest implements Listener {
    private Main plugin;

    public DuelRequest(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRequest(RequestEvent e) {
        Player player = e.getPlayer();
        Player target = e.getTarget();
        Kit kit = e.getKit();

        plugin.getRequestManager().sendRequest(player, target, kit);
    }
}
