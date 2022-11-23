package com.desticube.core.api.events.player;

import com.desticube.core.api.objects.DestiPlayer;
import com.gamerduck.commons.events.DuckPlayerEvent;
import org.bukkit.event.HandlerList;

public class PlayerChangeNicknameEvent extends DuckPlayerEvent {

    String nickName;

    public PlayerChangeNicknameEvent(DestiPlayer player, String nickName) {
        super(player.player());
        this.nickName = nickName;
    }

    public String getNickName() {
        return nickName;
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
