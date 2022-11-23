package com.desticube.core.listeners;

import com.desticube.core.listeners.handlers.DestiListener;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.listeners.DuckListener;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;

@DuckListener
public class OreAnnouncer extends DestiListener {

    final ArrayList<Player> onCooldown = Lists.newArrayList();

    @EventHandler
    public void onOreMine(BlockBreakEvent e) {
        if (e.getPlayer().hasPermission("desticore.oreannouncer.exempt")) return;
        if (onCooldown.contains(e.getPlayer())) return;
        if (e.getBlock().getType().toString().contains("ORE")) {
            int amount = getAmount(e.getBlock());
            Bukkit.broadcast(tl("OreAnnouncer", e.getPlayer().getName(), translateName(e.getBlock().getType().toString()), amount, e.getBlock().getLocation().getBlockX(), e.getBlock().getLocation().getBlockY(), e.getBlock().getLocation().getBlockX()),
                    "desticore.oreannouncer");
            onCooldown.add(e.getPlayer());
            startTimer(e.getPlayer(), amount * 60);
        }
    }

    private int getAmount(Block block) {
        int amount = 0;
        for (int x = -3; x <= 3; x++) {
            for (int y = -3; y <= 3; y++) {
                for (int z = -3; z <= 3; z++) {
                    if (block.getLocation().add(x, y, z).getBlock().getType() == block.getType()) amount++;
                }
            }
        }
        return amount;
    }

    private final String translateName(String name) {
        return Strings.capitalizeEach(name.toLowerCase().replaceAll("_", " "));
    }

    private BukkitTask startTimer(Player p, int amount) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(PLUGIN, () -> onCooldown.remove(p), amount);
    }
}
