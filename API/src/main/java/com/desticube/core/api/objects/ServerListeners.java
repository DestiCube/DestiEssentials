package com.desticube.core.api.objects;

import com.gamerduck.commons.persistent.DataTypes;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import static com.desticube.placeholders.api.Placeholders.setPlaceholders;
import static com.gamerduck.commons.general.Components.translate;

public class ServerListeners implements Listener {
    final DestiServer server;
    final String chatFormat;
    final String welcomeMessage;
    final String joinMessage;
    final String leaveMessage;
    public ServerListeners(DestiServer server) {
        this.server = server;
        this.chatFormat = server.getConfig().getString("Chat-Format");
        this.welcomeMessage = server.getConfig().getString("WelcomeMessage");
        this.joinMessage = server.getConfig().getString("JoinMessage");
        this.leaveMessage = server.getConfig().getString("LeaveMessage");
    }

    @EventHandler
    public void onFly(PlayerToggleFlightEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (!e.getPlayer().hasPermission("desticore.fly"))
            e.getPlayer().setAllowFlight(false);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        DestiPlayer p = server.getPlayer(e.getPlayer());
        if (p.getHomes().size() > 0) e.setRespawnLocation(p.getHomes().get(0).location());
        else if (e.getPlayer().getBedSpawnLocation() != null) e.setRespawnLocation(e.getPlayer().getBedSpawnLocation());
        else e.setRespawnLocation(server.getSpawn());
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player p) || e.getEntity().hasMetadata("NPC")) return;
//        e.setCancelled(server.getGodMode().contains(p.getUniqueId()));
        e.setCancelled(p.getPersistentDataContainer().has(DestiServer.getInstance().getGodKey(), DataTypes.BOOLEAN));
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (server.getPlayer(e.getEntity()) == null) return;
        server.getPlayer(e.getEntity()).setBackLocation(e.getEntity().getLocation());
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (server.getPlayer(e.getPlayer()) == null) return;
        server.getPlayer(e.getPlayer()).setBackLocation(e.getFrom());
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        e.setCancelled(server.getFrozen().contains(e.getPlayer().getUniqueId()));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e) {
        e.setCancelled(server.getFrozen().contains(e.getPlayer().getUniqueId()) || server.getJailed().containsKey(e.getPlayer().getUniqueId()));
        server.getSocialSpy().forEach(p -> {
            if (Bukkit.getPlayer(p).isOnline())
                Bukkit.getPlayer(p).sendMessage(e.getPlayer().getName() + ": " + e.getMessage());
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        server.accountsOnJoin(e.getPlayer());
        server.addPlayer(e.getPlayer(), (p) -> {
            server.sendMOTD(p);
            if (!p.player().hasPlayedBefore()) {
                Bukkit.broadcast(convert(e.getPlayer(), welcomeMessage));
                p.player().teleport(server.getSpawn());
                p.useKit(server.getConfig().getString("StarterKit"));
            }
        });
        if (!server.getVanished().contains(e.getPlayer().getUniqueId()))
            e.joinMessage(convert(e.getPlayer(), joinMessage));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (!server.getVanished().contains(e.getPlayer().getUniqueId()))
            e.quitMessage(convert(e.getPlayer(), leaveMessage));
        server.accountsOnQuit(e.getPlayer());
        server.getPlayer(e.getPlayer().getUniqueId()).quit().thenAccept(server::removePlayer);
    }

    @EventHandler
    public void onQuit(PlayerKickEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        server.accountsOnQuit(e.getPlayer());
        server.getPlayer(e.getPlayer().getUniqueId()).quit().thenAccept(server::removePlayer);
    }

//    @EventHandler(priority = EventPriority.HIGHEST)
//    public void onChat(AsyncChatEvent e) {
//        Bukkit.broadcast(convert(e.getPlayer(), chatFormat.replaceAll("%message%", miniMessage().serialize(e.message()))));
//        e.setCancelled(true);
//    }

    private @NotNull Component convert(Player p, String s) {
        return translate(
                setPlaceholders(p, s));
    }

    public static void setup(DestiServer server, JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(new ServerListeners(server), plugin);
    }

}