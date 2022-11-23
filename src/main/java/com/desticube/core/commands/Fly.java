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
        command = "fly",
        description = "Fly like a bird",
        aliases = {"flight"},
        usageARGS = "[player]",
        permissions = {"desticore.fly", "desticore.fly.other"})
public class Fly extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.fly")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) {
            switch (p.fly()) {
                case ENABLED -> p.sendMessage(tl("flyEnabled"));
                case DISABLED -> p.sendMessage(tl("flyDisabled"));
                default -> {
                }
            }
        } else {
            if (!p.hasPermission("desticore.fly.other")) return p.sendMessage(NO_PERMISSIONS);
            DestiPlayer target = Bukkit.getPlayer(args[0]) != null ? player(Bukkit.getPlayer(args[0])) : null;
            if (target == null) return p.sendMessage(tl("playerNotOnline", args[0]));
            switch (target.fly()) {
                case ENABLED -> {
                    p.sendMessage(tl("flyEnabledOther", args[0]));
                    target.sendMessage(tl("flyEnabled"));
                }
                case DISABLED -> {
                    p.sendMessage(tl("flyDisabledOther", args[0]));
                    target.sendMessage(tl("flyDisabled"));
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
        if (args.length == 1 && sender.hasPermission("desticore.fly.other")) {
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
	