package com.pdev.drduels.listeners;

import com.pdev.drduels.Main;
import com.pdev.drduels.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinQuit implements Listener {
    private Main plugin;

    public JoinQuit(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        PlayerManager pm = plugin.getPlayerManager();

        // Create new player or update their name in the DB incase they changed it
        if (!player.hasPlayedBefore()) {
            pm.createNewPlayer(player.getUniqueId(), player.getName());
        } else {
            pm.updateName(player.getUniqueId(), player.getName());
        }
    }
}
