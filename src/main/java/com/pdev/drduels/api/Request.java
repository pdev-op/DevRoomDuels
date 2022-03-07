package com.pdev.drduels.api;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class Request {
    private UUID sender;
    private UUID target;
    private Kit kit;
    private long createdAt;

    public Request(Player sender, Player target, Kit kit) {
        this.sender = sender.getUniqueId();
        this.target = target.getUniqueId();
        this.kit = kit;
        this.createdAt = System.currentTimeMillis();
    }

    public Kit getKit() {
        return kit;
    }

    public Player getSender() {
        return Bukkit.getPlayer(sender);
    }

    public Player getTarget() {
        return Bukkit.getPlayer(target);
    }

    public long getCreatedAt() {
        return createdAt;
    }


}
