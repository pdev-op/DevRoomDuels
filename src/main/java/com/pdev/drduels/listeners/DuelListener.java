package com.pdev.drduels.listeners;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.duel.Duel;
import com.pdev.drduels.api.duel.DuelResult;
import com.pdev.drduels.api.duel.DuelState;
import com.pdev.drduels.api.events.DuelEndEvent;
import com.pdev.drduels.api.player.QuitPlayer;
import com.pdev.drduels.managers.ArenaManager;
import com.pdev.drduels.managers.PlayerManager;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.spigotmc.event.player.PlayerSpawnLocationEvent;

public class DuelListener implements Listener {
    private Main plugin;

    public DuelListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onResult(DuelEndEvent e) {
        plugin.getArenaManager().handleResult(e.getResult());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerSpawnLocation(PlayerSpawnLocationEvent e) {
        PlayerManager pm = plugin.getPlayerManager();
        Player player = e.getPlayer();

        // Check if they quit during a duel
        if (pm.getQuitPlayers().containsKey(player.getUniqueId())) {
            QuitPlayer qp = pm.getQuitPlayers().get(player.getUniqueId());

            // Inventory & gamemode
            player.getInventory().setContents(qp.getContents());
            player.getInventory().setArmorContents(qp.getArmor());
            player.setGameMode(qp.getGameMode());

            // Location
            System.out.println(qp.getLastLocation());
            e.setSpawnLocation(qp.getLastLocation());

            // Remove Quit player
            pm.removeQuitPlayer(player.getUniqueId());
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ArenaManager am = plugin.getArenaManager();

        if (am.isInDuel(e.getPlayer())) {
            DuelResult result = new DuelResult(false, e.getPlayer().getUniqueId());
            DuelEndEvent event = new DuelEndEvent(result, false, null);

            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        ArenaManager am = plugin.getArenaManager();

        if (am.isInDuel(e.getPlayer())) {
            DuelResult result = new DuelResult(true, e.getPlayer().getUniqueId());
            DuelEndEvent event = new DuelEndEvent(result, false, null);

            e.setCancelled(true);

            plugin.getServer().getPluginManager().callEvent(event);
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        ArenaManager am = plugin.getArenaManager();
        Duel ad = am.getArena().getActiveDuel();

        if (ad != null) {
            if (am.isInDuel(e.getPlayer()) && ad.getState().equals(DuelState.STARTING)) {
                if (!e.getTo().getBlock().equals(e.getFrom().getBlock())) {
                    e.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e) {
        ArenaManager am = plugin.getArenaManager();
        Player damager = null;
        Player damaged = null;
        Duel ad = am.getArena().getActiveDuel();

        if (ad != null) {

            if (e.getDamager() instanceof Player) {
                damager = (Player) e.getDamager();
            }

            if (e.getEntity() instanceof Player) {
                damaged = (Player) e.getEntity();
            }

            if (damager != null) {
                if (am.isInDuel(damager) && ad.getState().equals(DuelState.STARTING)) {
                    e.setCancelled(true);

                    return;
                }
            }

            if (damaged != null) {
                if (am.isInDuel(damaged) && ad.getState().equals(DuelState.STARTING)) {
                    e.setCancelled(true);

                    return;
                }
            }
        }
    }
}
