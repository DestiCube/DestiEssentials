package com.desticube.core.hooks;

import com.desticube.core.api.exceptions.KitNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.records.Animation;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

public class DestiExpansion extends PlaceholderExpansion {

    private final DestiServer server;

    public DestiExpansion(DestiServer server) {
        this.server = server;
    }

    @Override
    public String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public String getIdentifier() {
        return "core";
    }

    @Override
    public String getVersion() {
        return "Eh-Who-Cares";
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {
        if (player == null || !Bukkit.getPlayer(player.getUniqueId()).isOnline()) return null;
        DestiPlayer p = server.getPlayer(Bukkit.getPlayer(player.getUniqueId()));
        if (params.startsWith("animation")) {
            String[] split = params.split(":");
            Animation animation = server.getAnimations().get(split[1]);
            return animation.next();
        }
        if (params.equalsIgnoreCase("nickname"))
            return (p.getNickName() == null ? p.player().getName() : p.getNickName());
        if (params.equalsIgnoreCase("balance") || params.equalsIgnoreCase("money"))
            return String.valueOf(p.getBalance());
        if (params.equalsIgnoreCase("balance_formatted") || params.equalsIgnoreCase("money_formatted"))
            return server.formatCurrency(p.getBalance());
        if (params.startsWith("kit_time_until_available_")) {
            if (params.startsWith("kit_time_until_available_formatted_")) {
                String[] split = params.split("_");
                try {
                    server.getKit(split[5]);
                    return String.valueOf(p.getKitCooldownLeftFormatted(split[5]));
                } catch (KitNotFoundException e) {
                    return "Not a Kit";
                }
            }
            String[] split = params.split("_");
            try {
                server.getKit(split[4]);
                return String.valueOf(p.getKitCooldownLeft(split[4]));
            } catch (KitNotFoundException e) {
                return "Not a Kit";
            }
        }
        if (params.startsWith("kit_is_available_")) {
            String[] split = params.split("_");
            try {
                server.getKit(split[3]);
                return p.canUseKit(split[3]) ? "yes" : "no";
            } catch (KitNotFoundException e) {
                return "Not a Kit";
            }
        }
//        if (params.equalsIgnoreCase("badge")) {
//            if (p.getCurrentBadge() == null) p.setCurrentBadge(server.getBadge("default"));
//            return p.getCurrentBadge().badge();
//        }
        return null;
    }
}