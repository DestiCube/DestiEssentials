package com.desticube.core.api.objects.economy;

import com.google.common.util.concurrent.AtomicDouble;
import org.bukkit.OfflinePlayer;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

public record Account(UUID uuid, String name, AtomicDouble balance, AtomicBoolean canReceive) {

    public Account(UUID uuid, String name) {
        this(uuid, name, new AtomicDouble(0.00), new AtomicBoolean(true));
    }

    public Account(OfflinePlayer player) {
        this(player.getUniqueId(), player.getName(), new AtomicDouble(0.00), new AtomicBoolean(true));
    }

    public UUID getUUID() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance.get();
    }

    public void setBalance(double bal) {
        balance.set(bal);
    }

    public AtomicBoolean canReceive() {
        return canReceive;
    }

    public void setReceive(boolean bool) {
        canReceive.set(bool);
    }

}
