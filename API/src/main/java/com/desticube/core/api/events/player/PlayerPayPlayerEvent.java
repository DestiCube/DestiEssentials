package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerPayPlayerEvent extends DuckPlayerEvent {

    DestiPlayer reciever;
    double amount;

    public PlayerPayPlayerEvent(DestiPlayer player, DestiPlayer reciever, double amount) {
        super(player.player());
        this.reciever = reciever;
        this.amount = amount;
    }

    public DestiPlayer getReciever() {
        return reciever;
    }

    public double getAmount() {
        return amount;
    }

    private boolean cancelled = false;

    public boolean isCancelled() {
        return cancelled;
    }

    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }

    private static HandlerList HANDLERS = new HandlerList();
    public static HandlerList getHandlerList() {return HANDLERS;}
    public HandlerList getHandlers() {return HANDLERS;}
}
