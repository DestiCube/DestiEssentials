package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.io.File;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "delworld",
        description = "Delete a world",
        aliases = {"deleteworld"},
        usageARGS = "(name)",
        permissions = {"desticore.delworld"})
public class DelWorld extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.delworld")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            if (Bukkit.getWorld(args[0]) == null) return p.sendMessage(tl("worldDoesntExist", args[0]));
            File folder = new File(Bukkit.getWorldContainer().getAbsolutePath() + File.separator + args[0]);
            Bukkit.unloadWorld(args[0], false);
            folder.delete();
            return p.sendMessage(tl("worldDeleted", args[0]));
        }
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
