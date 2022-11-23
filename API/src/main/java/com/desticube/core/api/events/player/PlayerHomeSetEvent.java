package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.records.Home;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerHomeSetEvent extends DuckPlayerEvent {

    Home home;

    public PlayerHomeSetEvent(DestiPlayer player, Home home) {
        super(player.player());
        this.home = home;
    }

    public Home getHome() {
        return home;
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
