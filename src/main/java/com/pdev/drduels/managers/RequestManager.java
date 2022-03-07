package com.pdev.drduels.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pdev.drduels.api.Kit;
import com.pdev.drduels.api.Request;
import com.pdev.drduels.utils.StringUtils;

import org.bukkit.entity.Player;

public class RequestManager {
    private Map<UUID, Request> requests;

    public RequestManager() {
        this.requests = new HashMap<>();
    }

    public Request getRequest(Player player, Player target) {
        Request request = requests.get(player.getUniqueId());

        if (request != null) {
            if (request.getTarget().getUniqueId() == target.getUniqueId()) {
                // Timing
                if (System.currentTimeMillis() - request.getCreatedAt() > 60000L) {
                    requests.remove(player.getUniqueId());
                    return null;
                }

                return request;
            }
        }

        return null;
    }

    public void sendRequest(Player sender, Player target, Kit kit) {
        Request request = new Request(sender, target, kit);

        requests.put(sender.getUniqueId(), request);

        sender.sendMessage(StringUtils.colorize(
                "&7You have send a duel request to &e" + target.getName() + " &7(&a" + kit.getName() + "&7)"));
        target.sendMessage(StringUtils.colorize("&7You have been sent duel request by &e" + sender.getName() + " &7(&a"
                + kit.getName() + " kit&7). Type &d/accept " + sender.getName() + " &7to accept the challenge!"));
    }

    public boolean hasRequest(Player player, Player target) {
        return getRequest(player, target) != null;
    }

    public void handlePlayerQuit(Player player) {
        requests.forEach((sender, request) -> {
            if (request.getTarget().getUniqueId() == player.getUniqueId()) {
                requests.remove(sender);
            }
        });

        requests.remove(player.getUniqueId());
    }
}
