package com.pdev.drduels.api.events;

import com.pdev.drduels.api.duel.DuelResult;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DuelEndEvent extends Event implements Cancellable {
    private static HandlerList handlers;

    private DuelResult result;
    private boolean canceled;
    private String cancelMessage;

    public DuelEndEvent(DuelResult result, boolean canceled, String cancelMessage) {
        this.result = result;
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

    public DuelResult getResult() {
        return result;
    }

    public String getCancelMessage() {
        return cancelMessage;
    }

    static {
        DuelEndEvent.handlers = new HandlerList();
    }
}
