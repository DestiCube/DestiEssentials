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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "unfreeze",
        description = "Except it actually freezes someone (not shows the stupid image)",
        aliases = {},
        usageARGS = "(player)",
        permissions = {"desticore.unfreeze"})
public class UnFreeze extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.unfreeze")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else {
            switch (SERVER.unFreeze(Bukkit.getPlayer(args[0]))) {
                case SUCCESS -> p.sendMessage(tl("playerUnfrozen", args[0]));
                case PLAYER_NOT_ONLINE -> p.sendMessage(tl("playerNotOnline", args[0]));
                case ALREADY_DONE -> p.sendMessage(tl("playerNotFrozen", args[0]));
                default -> {
                }
            }
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
