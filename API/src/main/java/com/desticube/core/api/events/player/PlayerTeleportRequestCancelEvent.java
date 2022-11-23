package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.records.TeleportRequest;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerTeleportRequestCancelEvent extends DuckPlayerEvent {

    TeleportRequest request;

    public PlayerTeleportRequestCancelEvent(DestiPlayer player, TeleportRequest request) {
        super(player.player());
        this.request = request;
    }

    public TeleportRequest getRequest() {
        return request;
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
