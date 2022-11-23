package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;

public class PlayerIgnorePlayerEvent extends DuckPlayerEvent {

    Player ignored;

    public PlayerIgnorePlayerEvent(DestiPlayer player, Player ignored) {
        super(player.player());
        this.ignored = ignored;
    }

    public Player getIgnored() {
        return ignored;
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
