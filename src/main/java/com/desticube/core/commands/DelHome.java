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
        command = "deletehome",
        description = "Delete a home",
        aliases = {"delhome"},
        usageARGS = "(home)",
        permissions = {})
public class DelHome extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) {
            p.sendMessage(USAGE);
            return p.sendMessage(tl("availableHomes", p.getHomesAsNames().toString()));
        } else {
            switch (p.delHome(args[0])) {
                case NO_NAME -> p.sendMessage(tl("homeDoesntExist", args[0]));
                case SUCCESS -> p.sendMessage(tl("homeDeletedSuccessfully", args[0]));
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
            DestiPlayer p = player(((Player) sender));
            List<String> homes = Lists.newArrayList();
            p.getHomes().forEach(h -> homes.add(h.name()));
            List<String> newHomes = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], homes, newHomes);
            return newHomes;
        } else {
            return Collections.emptyList();
        }
    }
}
