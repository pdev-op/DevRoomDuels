package com.pdev.drduels.commands;

import java.text.DecimalFormat;
import java.util.HashMap;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.DuelCommand;
import com.pdev.drduels.api.player.StatsPlayer;
import com.pdev.drduels.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Stats extends DuelCommand {
    public Stats(Main plugin) {
        super(plugin);

        this.addAlias("stats");
        this.setUsage("/stats [player]");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("players", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender.hasPermission("drduels.stats");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        // Own Stats
        if (args.length == 0) {
            if (!(sender instanceof Player)) {
                throw new Exception("console-sender");
            }

            Player player = (Player) sender;
            StatsPlayer drPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

            sendStats(sender, drPlayer);

        // Others Stats
        } else if (args.length == 1) {
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                throw new Exception("no-player");
            }

            StatsPlayer drPlayer = plugin.getPlayerManager().getPlayer(player.getUniqueId());

            sendStats(sender, drPlayer);
        } else {
            throw new Exception("usage");
        }

        return true;
    }

    private void sendStats(CommandSender sender, StatsPlayer drPlayer) {
        // Heading
        String name = drPlayer.getBukkitPlayer().getName() + "'s";

        if (sender instanceof Player) {
            if (((Player) sender).getUniqueId().equals(drPlayer.getBukkitPlayer().getUniqueId())) {
                name = "Your";
            }
        }

        sender.sendMessage(StringUtils.colorize("&e&l" + name + " &6&lDuel Stats"));

        // Stats
        sender.sendMessage(StringUtils.colorize("&8- &7Wins&8: &a" + drPlayer.getWins()));
        sender.sendMessage(StringUtils.colorize("&8- &7Losses&8: &c" + drPlayer.getLosses()));
        sender.sendMessage(StringUtils.colorize("&8- &7Kills&8: &a" + drPlayer.getKills()));
        sender.sendMessage(StringUtils.colorize("&8- &7Deaths&8: &c" + drPlayer.getDeaths()));

        DecimalFormat df = new DecimalFormat("#.##");
        String wlRatio = df.format(((double) drPlayer.getWins()) / ((double) drPlayer.getLosses()));
        String kdRatio = df.format(((double) drPlayer.getKills()) / ((double) drPlayer.getDeaths()));

        if (drPlayer.getLosses() == 0) {
            wlRatio = Integer.toString(drPlayer.getWins());
        }

        if (drPlayer.getDeaths() == 0) {
            kdRatio = Integer.toString(drPlayer.getKills());
        }

        sender.sendMessage(StringUtils.colorize("&8- &7K/D Ratio&8: &e" + kdRatio));
        sender.sendMessage(StringUtils.colorize("&8- &7W/L Ratio&8: &e" + wlRatio));
        sender.sendMessage(StringUtils.colorize("&8- &7Win Streak&8: " + (drPlayer.getWinStreak() == 0 ? "&cNone" : "&a" + drPlayer.getWinStreak())));
    }
}
