package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;

@DuckCommand(
        command = "Near",
        description = "Check who is near you",
        aliases = {"nearby"},
        usageARGS = "",
        permissions = {"desticore.near"})
public class Near extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.near")) return p.sendMessage(NO_PERMISSIONS);
        ArrayList<Player> players = new ArrayList<Player>();
        for (Entity ent : p.player().getNearbyEntities(20, 20, 20)) {
            if (ent instanceof Player) players.add((Player) ent);
        }
        StringBuilder builder = new StringBuilder();
        players.forEach(player -> {
            if (player.getUniqueId() != p.getUniqueId()) {
                double distance = p.getLocation().distance(player.getLocation());
                builder.append(tl("nearFormat", player.getName(), distance));
            }
        });
        p.sendMessage(tl("near", players.size(), builder.toString()));
        return false;
    }
}
	