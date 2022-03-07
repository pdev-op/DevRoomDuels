package com.pdev.drduels.commands;

import java.util.HashMap;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.DRDCommand;
import com.pdev.drduels.api.Request;
import com.pdev.drduels.api.events.AcceptEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Accept extends DRDCommand {
    public Accept(Main plugin) {
        super(plugin);

        this.addAlias("accept");
        this.setUsage("/accept <name>");
    }

    @Override
    public HashMap<String, Integer> getSuggestions(CommandSender sender) {
        HashMap<String, Integer> suggestions = new HashMap<>();

        suggestions.put("players", 1);

        return suggestions;
    }

    @Override
    public boolean hasPermission(CommandSender sender) {
        return sender instanceof Player && sender.hasPermission("drduels.duel");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) throws Exception {
        if (args.length == 1) {
            // Check for active invite
            Player target = (Player) sender;
            Player player = Bukkit.getPlayer(args[0]);

            if (player == null) {
                throw new Exception("no-player");
            }

            if (plugin.getRequestManager().hasRequest(player, target)) {
                Request request = plugin.getRequestManager().getRequest(player, target);
                AcceptEvent event = new AcceptEvent(request, false, null);

                Bukkit.getServer().getPluginManager().callEvent(event);
            } else {
                throw new Exception("no-request");
            }

        } else {
            throw new Exception("usage");
        }

        return true;
    }
}
