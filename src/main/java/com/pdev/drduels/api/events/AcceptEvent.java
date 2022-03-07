package com.pdev.drduels.api.events;

import com.pdev.drduels.api.Request;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class AcceptEvent extends Event implements Cancellable {
    private static HandlerList handlers;

    private Request request;
    private boolean canceled;
    private String cancelMessage;

    public AcceptEvent(Request request, boolean canceled, String cancelMessage) {
        this.request = request;
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

    public Request getRequest() {
        return request;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    static {
        AcceptEvent.handlers = new HandlerList();
    }
}
