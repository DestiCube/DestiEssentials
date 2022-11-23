package com.desticube.core.listeners;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.listeners.handlers.DestiListener;
import com.gamerduck.commons.listeners.DuckListener;
import org.bukkit.Material;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Slab.Type;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.spigotmc.event.entity.EntityDismountEvent;

@DuckListener
public class SitHandler extends DestiListener {

    public SitHandler() {
    }

    @EventHandler
    public void onShift(EntityDismountEvent e) {
        if (e.getEntity() instanceof Player) {
            if (e.getEntity().hasMetadata("NPC")) return;
            DestiPlayer p = player((Player) e.getEntity());
            if (p.isSitting()) p.unSit();
            else return;
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e) {
        if (e.getAction() == Action.LEFT_CLICK_AIR || e.getAction() == Action.LEFT_CLICK_BLOCK || e.getAction() == Action.RIGHT_CLICK_AIR)
            return;
        if (e.getPlayer().getInventory().getItemInMainHand().getType() != Material.AIR ||
                e.getClickedBlock().getWorld().getBlockAt(e.getClickedBlock().getLocation().add(0, 1, 0)).getType() != Material.AIR)
            return;
        DestiPlayer player = player(e.getPlayer());
        if (player == null) return;

        if (e.getClickedBlock().getType().toString().contains("SLAB")) {
            Slab slab = (Slab) e.getClickedBlock().getBlockData();
            if (slab.isWaterlogged() || slab.getType() == Type.DOUBLE) return;
            e.setCancelled(true);
            if (player.isSitting()) player.unSit();
            if (slab.getType() == Type.BOTTOM) player.sit(e.getClickedBlock().getLocation().add(0.50, -0.50, 0.50));
            else if (slab.getType() == Type.TOP) player.sit(e.getClickedBlock().getLocation().add(0.50, 0, 0.50));
        } else if (e.getClickedBlock().getType().toString().contains("STAIR")) {
            if (player.isSitting()) player.unSit();
            player.sit(e.getClickedBlock().getLocation()
                    .subtract(-0.50, 0.50, -0.50));
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (player(e.getPlayer()) == null) return;
        if (player(e.getPlayer()).isSitting()) player(e.getPlayer()).unSit();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (player(e.getEntity()) == null) return;
        if (player(e.getEntity()).isSitting()) player(e.getEntity()).unSit();
    }

    @EventHandler
    public void onKick(PlayerKickEvent e) {
        if (e.getPlayer().hasMetadata("NPC")) return;
        if (player(e.getPlayer()) == null) return;
        if (player(e.getPlayer()).isSitting()) player(e.getPlayer()).unSit();
    }
}
