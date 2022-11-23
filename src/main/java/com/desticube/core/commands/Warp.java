package com.desticube.core.commands;

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
        command = "warp",
        description = "Teleport to a home",
        aliases = {},
        usageARGS = "(name)",
        permissions = {})
public class Warp extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) {
            SERVER.openWarpMenu(p);
        } else {
            switch (p.warp(args[0])) {
                case NO_NAME -> p.sendMessage(tl("warpDoesntExist", args[0]));
                case SUCCESS -> p.sendMessage(tl("teleporting"));
                case EVENT_CANCELLED -> {
                    break;
                }
                default -> {
                    break;
                }
            }
        }
        return false;
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
