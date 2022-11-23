package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Barrel;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Dropper;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Dispenser;
import org.bukkit.block.data.type.EnderChest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.HashMap;

@DuckCommand(
        command = "nuke",
        description = "",
        aliases = {"nukeem"},
        usageARGS = "",
        permissions = {"desticore.nuke"})
public class Nuke extends DestiCommand {

    final NamespacedKey nukeKey = new NamespacedKey(PLUGIN, "nuke");
    Listener listen;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.nuke")) return p.sendMessage(NO_PERMISSIONS);
        ArrayList<Location> locs = new ArrayList<Location>();
        Location pLoc = p.getLocation();
        for (int x = -8; x < 8; x++) {
            for (int z = -8; z < 8; z++) {
                if ((x % 2 == 0) && (z % 2 == 0)) {
                    locs.add(pLoc.clone().add(x, 0, z));
                }
            }
        }
        locs.forEach(loc -> {
            Entity ent = loc.getWorld().spawnEntity(loc, EntityType.PRIMED_TNT);
            PersistentDataContainer container = ent.getPersistentDataContainer();
            container.set(nukeKey, PersistentDataType.INTEGER, 1);
        });
        listen = new Listener() {
            HashMap<Location, NukedBlock> blocks = new HashMap<Location, NukedBlock>();
            BukkitTask task = Bukkit.getScheduler().runTaskLater(PLUGIN, new Runnable() {
                @Override
                public void run() {
                    blocks.keySet().forEach(loc -> {
                        Block block = loc.getBlock();
                        block.setType(blocks.get(loc).type);
                        block.setBlockData(blocks.get(loc).data);
                    });
                    unregister();
                }
            }, 20 * 20);

            @EventHandler
            public void onExplode(EntityExplodeEvent e) {
                if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
                    if (e.getEntity().getPersistentDataContainer().has(nukeKey, PersistentDataType.INTEGER)) {
                        e.blockList().forEach(block -> {
                            block.getDrops().clear();
                            if (block.getState() instanceof Chest
                                    || block.getState() instanceof EnderChest
                                    || block.getState() instanceof Barrel
                                    || block.getState() instanceof Dropper
                                    || block.getState() instanceof Dispenser) e.setCancelled(true);
                            else blocks.put(block.getLocation(), new NukedBlock(block.getType(), block.getBlockData()));
                        });
                    }
                }
            }

            @EventHandler
            public void onDamage(EntityDamageByEntityEvent e) {
                if (e.getEntityType().equals(EntityType.PRIMED_TNT)) {
                    if (e.getEntity().getPersistentDataContainer().has(nukeKey, PersistentDataType.INTEGER)) {
                        e.setCancelled(true);
                    }
                }
            }

            private void unregister() {
                HandlerList.unregisterAll(this);
            }
        };
        PLUGIN.getServer().getPluginManager().registerEvents(listen, PLUGIN);
        return false;
    }

    private record NukedBlock(Material type, BlockData data) {
    }
}
