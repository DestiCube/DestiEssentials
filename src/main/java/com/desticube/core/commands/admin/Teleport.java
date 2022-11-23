package com.desticube.core.commands.admin;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "teleport",
        description = "Teleport to anyone!",
        aliases = {"tp"},
        usageARGS = "(player)",
        permissions = {"desticore.teleport", "desticore.teleport.warmupbypass"})
public class Teleport extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.teleport")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else if (args.length == 1) {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) return p.sendMessage(tl("playerNotOnline"));
            p.teleport(target.getLocation(), TeleportReason.MISC, (b) -> {
            });
            return p.sendMessage(tl("teleporting"));
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("~")) args[0] = String.valueOf(p.getLocation().getX());
            if (args[1].equalsIgnoreCase("~")) args[1] = String.valueOf(p.getLocation().getY());
            if (args[2].equalsIgnoreCase("~")) args[2] = String.valueOf(p.getLocation().getZ());
            p.teleport(
                    new Location(p.getLocation().getWorld(), Double.valueOf(args[0]), Double.valueOf(args[1]), Double.valueOf(args[2])),
                    TeleportReason.MISC, (b) -> {
                    });
            return p.sendMessage(tl("teleporting"));
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], players, finalPlayers);
            return finalPlayers;
        } else return Collections.emptyList();
    }
}
