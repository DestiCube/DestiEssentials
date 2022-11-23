package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.records.Kit;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerKitClaimEvent extends DuckPlayerEvent {

    Kit kit;

    public PlayerKitClaimEvent(DestiPlayer player, Kit kit) {
        super(player.player());
        this.kit = kit;
    }

    public Kit getKit() {
        return kit;
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
