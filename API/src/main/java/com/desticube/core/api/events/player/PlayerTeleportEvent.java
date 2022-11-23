package com.desticube.core.api.events.player;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class PlayerTeleportEvent extends DuckPlayerEvent {

    Location location;

    TeleportReason reason;

    public PlayerTeleportEvent(DestiPlayer player, Location location, TeleportReason reason) {
        super(player.player());
        this.location = location;
        this.reason = reason;
    }

    public Location getLocation() {
        return location;
    }

    public TeleportReason getReason() {
        return reason;
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
