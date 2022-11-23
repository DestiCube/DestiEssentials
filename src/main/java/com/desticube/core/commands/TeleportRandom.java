package com.desticube.core.commands;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Random;
import java.util.concurrent.CompletableFuture;

@DuckCommand(
        command = "tprandom",
        description = "Teleport to a random location",
        aliases = {"tpr", "randomtp", "rtp"},
        usageARGS = "",
        permissions = {"desticore.teleport.warmupbypass"})
public class TeleportRandom extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.canUseCooldown("randomtp")) {
            return p.sendMessage(tl("commandOnCooldown", p.getCommandCooldownLeft("randomtp")));
        } else {
            p.sendMessage(tl("findingNewLocation"));
            findLocation(SERVER.getTPRWorld()).thenAcceptAsync(l -> {
                p.teleport(l, TeleportReason.MISC, (b) -> {
                    if (b) p.useCooldown("randomtp");
                });
            }, SERVER.getExecutor());
            return true;
        }
    }


    private CompletableFuture<Location> findLocation(World world) {
        return CompletableFuture.supplyAsync(() -> {
            Random rand = new Random();
            int x, z;
            Location location = new Location(SERVER.getTPRWorld(), 0, 150, 0);
            for (int i = 0; i < 5; i++) {
                x = rand.ints(SERVER.getTPRMin(), SERVER.getTPRMax()).findAny().getAsInt();
                z = rand.ints(SERVER.getTPRMin(), SERVER.getTPRMax()).findAny().getAsInt();
                x = rand.nextBoolean() ? -x : x;
                z = rand.nextBoolean() ? -z : z;
                location = new Location(SERVER.getTPRWorld(), x, 150, z);
                location.setY(world.getHighestBlockYAt(location.getBlockX(), location.getBlockZ()));
                if (world.getBlockAt(location).getType() == Material.LAVA || world.getBlockAt(location).getType() == Material.WATER)
                    continue;
                else break;
            }
            return location;
        }, SERVER.getExecutor());
    }
}
	