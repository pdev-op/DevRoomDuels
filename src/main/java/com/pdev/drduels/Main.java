package com.pdev.drduels;

import java.sql.SQLException;

import com.pdev.drduels.config.ArenaFile;
import com.pdev.drduels.config.Config;
import com.pdev.drduels.config.KitFile;
import com.pdev.drduels.listeners.DuelAccept;
import com.pdev.drduels.listeners.DuelListener;
import com.pdev.drduels.listeners.DuelRequest;
import com.pdev.drduels.listeners.JoinQuit;
import com.pdev.drduels.managers.ArenaManager;
import com.pdev.drduels.managers.CommandManager;
import com.pdev.drduels.managers.KitManager;
import com.pdev.drduels.managers.PlayerManager;
import com.pdev.drduels.managers.RequestManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static Main instance;

    private CommandManager commandManager;
    private PlayerManager playerManager;
    private KitManager kitManager;
    private RequestManager requestManager;
    private ArenaManager arenaManager;

    private Config config;
    private ArenaFile arenaFile;
    private KitFile kitFile;

    public Main() {
        instance = this;
    }

    @Override
    public void onEnable() {
        // Timings
        long start = System.currentTimeMillis();

        // Logging
        Bukkit.getConsoleSender()
                .sendMessage("§eDevRoomDuels §7v" + getDescription().getVersion() + " by pdev enabling...");

        // Files
        config = new Config(this);
        arenaFile = new ArenaFile(this);
        kitFile = new KitFile(this);

        // Managers
        commandManager = new CommandManager(this);

        try {
            playerManager = new PlayerManager(this);
        } catch (SQLException e) {
            e.printStackTrace();
            onDisable();
        }

        kitManager = new KitManager(this);
        requestManager = new RequestManager();
        arenaManager = new ArenaManager(this);

        // Listeners
        getServer().getPluginManager().registerEvents(new JoinQuit(this), this);
        getServer().getPluginManager().registerEvents(new DuelAccept(this), this);
        getServer().getPluginManager().registerEvents(new DuelRequest(this), this);
        getServer().getPluginManager().registerEvents(new DuelListener(this), this);

        // Log load time
        Bukkit.getConsoleSender().sendMessage("§aEnabled §7in " + (System.currentTimeMillis() - start) + "ms");
    }

    @Override
    public void onDisable() {
        // Close Inventories to prevent glitching
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.closeInventory();
        }

        // Logging
        Bukkit.getConsoleSender()
                .sendMessage("§eDevRoomDuels §7v" + getDescription().getVersion() + " by pdev disabled.");
    }

    public static Main getInstance() {
        return instance;
    }

    public Config getConfigFile() {
        return config;
    }

    public ArenaFile getArenaFile() {
        return arenaFile;
    }

    public KitFile getKitFile() {
        return kitFile;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public KitManager getKitManager() {
        return kitManager;
    }

    public RequestManager getRequestManager() {
        return requestManager;
    }

    public ArenaManager getArenaManager() {
        return arenaManager;
    }
}
