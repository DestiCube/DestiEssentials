package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.JailNotFoundException;
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
        command = "deljail",
        description = "Create a jail",
        aliases = {"deletejail"},
        usageARGS = "(name)",
        permissions = {"desticore.deljail"})
public class DelJail extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.deljail")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            try {
                SERVER.delJail(args[0]);
                return p.sendMessage(tl("jailDeleted", args[0]));
            } catch (JailNotFoundException e) {
                return p.sendMessage(tl("jailDoesntExist", args[0]));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            List<String> jails = Lists.newArrayList();
            SERVER.getJails().forEach(j -> jails.add(j.name()));
            List<String> newJails = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], jails, newJails);
            return newJails;
        } else {
            return Collections.emptyList();
        }
    }
}
