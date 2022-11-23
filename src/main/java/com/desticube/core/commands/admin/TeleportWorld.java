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

import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "teleportworld",
        description = "Travel between worlds",
        aliases = {"tpworld"},
        usageARGS = "(name)",
        permissions = {"desticore.tpworld"})
public class TeleportWorld extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.tpworld")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length < 1) p.sendMessage(USAGE);
        else {
            if (Bukkit.getWorld(args[0]) == null) return p.sendMessage(tl("worldDoesntExist", args[0]));
            p.teleport(Bukkit.getWorld(args[0]).getSpawnLocation() != null ?
                    Bukkit.getWorld(args[0]).getSpawnLocation() : new Location(Bukkit.getWorld(args[0]), 0, 0, 0), TeleportReason.MISC, (b) -> {
            });
            return p.sendMessage(tl("teleporting", args[0]));
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            List<String> worlds = Lists.newArrayList();
            Bukkit.getWorlds().forEach(w -> worlds.add(w.getName()));
            List<String> newWorlds = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], worlds, newWorlds);
            return newWorlds;
        } else {
            return Collections.emptyList();
        }
    }
}
