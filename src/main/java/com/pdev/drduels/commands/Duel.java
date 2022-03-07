package com.pdev.drduels.commands;

import java.util.HashMap;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.DuelCommand;
import com.pdev.drduels.api.Kit;
import com.pdev.drduels.api.events.RequestEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Duel extends DuelCommand {
    public Duel(Main plugin) {
        super(plugin);

        this.addAlias("duel");
        this.addAlias("d");
        this.setUsage("/duel <name> [kit]");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("players", 1);
        suggestions.put("kitlist", 2);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender instanceof Player && sender.hasPermission("drduels.duel");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        // Duel with default kit
        if (args.length == 1) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                throw new Exception("no-player");
            }

            if (player.getUniqueId().equals(target.getUniqueId())) {
                throw new Exception("self-duel");
            }

            // Initiate duel & send invitation
            RequestEvent event = new RequestEvent(player, target, plugin.getKitManager().getDefaultKit(), false, null);

            Bukkit.getServer().getPluginManager().callEvent(event);

            // Duel with chosen kit
        } else if (args.length == 2) {
            Player player = (Player) sender;
            Player target = Bukkit.getPlayer(args[0]);

            if (target == null) {
                throw new Exception("no-player");
            }

            Kit kit = plugin.getKitManager().getKit(args[1]);

            if (kit == null) {
                throw new Exception("no-kit");
            }

            // Initiate duel & send invitation
            RequestEvent event = new RequestEvent(player, target, kit, false, null);

            Bukkit.getServer().getPluginManager().callEvent(event);
        } else {
            throw new Exception("usage");
        }

        return true;
    }
}
