package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "God",
        description = "Become a god",
        aliases = {"godmode"},
        usageARGS = "[player]",
        permissions = {"desticore.god", "desticore.god.other"})
public class God extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.god")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) {
            switch (p.toggleGodMode()) {
                case ENABLED -> p.sendMessage(tl("godEnabled"));
                case DISABLED -> p.sendMessage(tl("godDisabled"));
                default -> {
                }
            }
        } else {
            if (!p.hasPermission("desticore.god.other")) return p.sendMessage(NO_PERMISSIONS);
            DestiPlayer target = Bukkit.getPlayer(args[0]) != null ? player(Bukkit.getPlayer(args[0])) : null;
            if (target == null) return p.sendMessage(tl("playerNotOnline", args[0]));
            switch (target.toggleGodMode()) {
                case ENABLED -> {
                    p.sendMessage(tl("godEnabledOther", args[0]));
                    target.sendMessage(tl("godEnabled"));
                }
                case DISABLED -> {
                    p.sendMessage(tl("godDisabledOther", args[0]));
                    target.sendMessage(tl("godDisabled"));
                }
                default -> {
                }
            }
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1 && sender.hasPermission("desticore.god.other")) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], players, finalPlayers);
            return finalPlayers;
        } else {
            return Collections.emptyList();
        }
    }
}
	