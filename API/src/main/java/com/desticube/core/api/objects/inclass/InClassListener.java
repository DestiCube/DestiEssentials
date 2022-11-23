package com.desticube.core.api.objects.inclass;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class InClassListener implements Listener {

    Player p;

    public InClassListener(Player p, Plugin plugin) {
        this.p = p;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    public void unregister() {
        HandlerList.unregisterAll(this);
    }
}
