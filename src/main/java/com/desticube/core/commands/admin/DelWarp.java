package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.WarpNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "delwarp",
        description = "Create a warp",
        aliases = {"deletewarp"},
        usageARGS = "(name)",
        permissions = {"desticore.delwarp"})
public class DelWarp extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.delwarp")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            try {
                SERVER.delWarp(args[0]);
                return p.sendMessage(tl("warpDeleted", args[0]));
            } catch (WarpNotFoundException e) {
                return p.sendMessage(tl("warpDoesntExist", args[0]));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            List<String> warps = Lists.newArrayList();
            SERVER.getWarps().forEach(w -> warps.add(w.name()));
            List<String> newWarps = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], warps, newWarps);
            return newWarps;
        } else {
            return Collections.emptyList();
        }
    }
}
