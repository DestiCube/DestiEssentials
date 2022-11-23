package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerMessageSendEvent extends DuckPlayerEvent {

    String message;
    DestiPlayer sentTo;

    public PlayerMessageSendEvent(DestiPlayer player, DestiPlayer sentTo, String message) {
        super(player.player());
        this.message = message;
        this.sentTo = sentTo;
    }

    public DestiPlayer getSentTo() {
        return sentTo;
    }

    public String getMessage() {
        return message;
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
