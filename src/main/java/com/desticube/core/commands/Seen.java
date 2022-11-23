package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@DuckCommand(
        command = "seen",
        description = "See when a player was last on, or how long they have been on",
        aliases = {},
        usageARGS = "(player)",
        permissions = {"desticore.seen.admin"})
public class Seen extends DestiCommand {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            if (Bukkit.getOfflinePlayer(args[0]).isOnline()) {
                DestiPlayer target = SERVER.getPlayer(Bukkit.getPlayer(args[0]));
                return p.sendMessage(tl("seenOnline", target.getNickName(), formatTime(target.getLastLogin())));
            } else {
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
                if (!target.hasPlayedBefore()) return p.sendMessage(tl("playerNeverJoined", args[0]));
                return p.sendMessage(tl("seenOffline", target.getName(), formatTime(target.getLastPlayed())));
            }
        }
    }

    private String formatTime(long time) {
        StringBuilder builder = new StringBuilder();
        long uptime = (System.currentTimeMillis() - time);
        builder.append((TimeUnit.MILLISECONDS.toDays(uptime) > 0) ? TimeUnit.MILLISECONDS.toDays(uptime) + "D " : "");
        uptime -= TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(uptime));
        builder.append((TimeUnit.MILLISECONDS.toHours(uptime) > 0) ? TimeUnit.MILLISECONDS.toHours(uptime) + "H " : "");
        uptime -= TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(uptime));
        builder.append((TimeUnit.MILLISECONDS.toMinutes(uptime) > 0) ? TimeUnit.MILLISECONDS.toMinutes(uptime) + "M " : "");
        uptime -= TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(uptime));
        builder.append((TimeUnit.MILLISECONDS.toSeconds(uptime) > 0) ? TimeUnit.MILLISECONDS.toSeconds(uptime) + "S " : "");
        return builder.toString();
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
        } else {
            return Collections.emptyList();
        }
    }

}
	