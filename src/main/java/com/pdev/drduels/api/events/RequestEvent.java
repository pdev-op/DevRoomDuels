package com.pdev.drduels.api.events;

import com.pdev.drduels.api.Kit;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RequestEvent extends Event implements Cancellable {
    private static HandlerList handlers;
    
    private Player player;
    private Player target;
    private Kit kit;
    private boolean canceled;
    private String cancelMessage;

    public RequestEvent(Player player, Player target, Kit kit, boolean canceled, String cancelMessage) {
        this.player = player;
        this.target = target;
        this.kit = kit;
        this.canceled = canceled;
        this.cancelMessage = cancelMessage;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.canceled = cancel;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public Player getPlayer() {
        return player;
    }

    public Player getTarget() {
        return target;
    }

    public Kit getKit() {
        return kit;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    static {
        RequestEvent.handlers = new HandlerList();
    }
}
