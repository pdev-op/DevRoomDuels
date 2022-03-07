package com.pdev.drduels.managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.pdev.drduels.Main;
import com.pdev.drduels.api.DRDCommand;
import com.pdev.drduels.commands.Accept;
import com.pdev.drduels.commands.Duel;
import com.pdev.drduels.commands.Stats;
import com.pdev.drduels.utils.StringUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

public class CommandManager implements CommandExecutor, TabCompleter {
    private Main plugin;
    private ArrayList<DRDCommand> commands;

    public CommandManager(Main plugin) {
        // Plugin
        this.plugin = plugin;

        // Commands List
        commands = new ArrayList<DRDCommand>();

        // Commands
        commands.add(new Accept(plugin));
        commands.add(new Duel(plugin));
        commands.add(new Stats(plugin));

        // Register the commands
        registerCommands();
    }

    public void registerCommands() {
        for (DRDCommand c : commands) {
            for (String s : c.getAliases()) {
                if (plugin.getCommand(s) != null) {
                    plugin.getCommand(s).setExecutor(this);
                    plugin.getCommand(s).setTabCompleter(this);
                }
            }
        }
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        ArrayList<String> suggestions = new ArrayList<String>();

        for (DRDCommand c : commands) {
            for (String al : c.getAliases()) {
                if (al.equalsIgnoreCase(alias) && c.hasPermission(sender)) {
                    HashMap<String, Integer> suggestionsMap = c.getSuggestions(sender);

                    for (String s : suggestionsMap.keySet()) {
                        if (args.length == suggestionsMap.get(s)) {
                            if (s.equals("players")) {
                                for (Player player : Bukkit.getOnlinePlayers()) {
                                    suggestions.add(player.getName());
                                }
                            } else if (s.equals("kitlist")) {
                                for (String k : plugin.getKitManager().getKitNames()) {
                                    suggestions.add(k);
                                }
                            } else {
                                suggestions.add(s);
                            }
                        }
                    }
                }
            }
        }

        return suggestions;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        for (DRDCommand c : commands) {
            if (c.getAliases().contains(label.toLowerCase())) {

                String prefix = plugin.getConfigFile().getPrefix();

                // Check Permissions
                if (!c.hasPermission(sender)) {
                    if (!(sender instanceof Player)) {
                        sender.sendMessage(
                                StringUtils.colorize(prefix + "&7This command cannot be run from the console."));
                    } else {
                        sender.sendMessage(StringUtils.colorize(prefix + "&cInsufficient permissions."));
                    }

                    return false;
                }

                // Send it
                try {
                    c.execute(sender, args);
                } catch (Exception e) {
                    if (e.getMessage().equalsIgnoreCase("insufficient-permissions")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&cInsufficient permissions."));
                    } else if (e.getMessage().equalsIgnoreCase("usage")) {
                        sender.sendMessage(
                                StringUtils.colorize(prefix + "&7Incorrect usage (try &e" + c.getUsage() + ")."));
                    } else if (e.getMessage().equalsIgnoreCase("no-player")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&7Player not found."));
                    } else if (e.getMessage().equalsIgnoreCase("no-request")) {
                        sender.sendMessage(StringUtils
                                .colorize(prefix + "&7Could not find a request for this player, please try again."));
                    } else if (e.getMessage().equalsIgnoreCase("no-kit")) {
                        sender.sendMessage(StringUtils
                                .colorize(prefix + "&7Could not find a kit with that name, please try again."));
                    } else if (e.getMessage().equalsIgnoreCase("console-sender")) {
                        sender.sendMessage(
                                StringUtils.colorize(prefix + "&7You cannot use this command from the console."));
                    } else if (e.getMessage().equalsIgnoreCase("self-duel")) {
                        sender.sendMessage(StringUtils.colorize(prefix + "&7You cannot duel yourself."));
                    } else {
                        sender.sendMessage(StringUtils.colorize(
                                "&cAn internal error has occured, please contact an admin. We are sorry for the inconvenience!"));

                        e.printStackTrace();
                    }
                }

                return true;
            }
        }

        return false;
    }
}
