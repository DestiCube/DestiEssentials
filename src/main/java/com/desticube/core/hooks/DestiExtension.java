package com.desticube.core.hooks;

import com.desticube.core.api.exceptions.KitNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.records.Animation;
import com.desticube.placeholders.api.PlaceholderExtension;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class DestiExtension extends PlaceholderExtension {

    private final DestiServer server;

    public DestiExtension(DestiServer server) {
        this.server = server;
    }

    @Override
    public @NotNull String getAuthor() {
        return "GamerDuck123";
    }

    @Override
    public @NotNull String getIdentifier() {
        return "core";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0";
    }

    @Override
    public CompletableFuture<String> onRequest(Player player, String params) {
        return CompletableFuture.supplyAsync(() -> {
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
            return null;
        });
    }
}
