package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.Location;
import org.bukkit.event.HandlerList;

public class PlayerSitEvent extends DuckPlayerEvent {

    Location location;

    public PlayerSitEvent(DestiPlayer player, Location location) {
        super(player.player());
        this.location = location;
    }

    public Location getLocation() {
        return location;
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
