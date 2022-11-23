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
        command = "feed",
        description = "Make the gods feed you",
        aliases = {"eat"},
        usageARGS = "[player]",
        permissions = {"desticore.feed", "desticore.feed.other"})
public class Feed extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.feed")) return p.sendMessage(NO_PERMISSIONS);
        if (!p.canUseCooldown("feed")) return p.sendMessage(tl("commandOnCooldown", p.getKitCooldownLeft("feed")));
        if (args.length == 0) {
            p.player().setFoodLevel(20);
            p.player().setSaturation(20);
            p.sendMessage(tl("feed"));
            p.useCooldown("feed");
        } else {
            if (!p.hasPermission("desticore.feed.other")) return p.sendMessage(NO_PERMISSIONS);
            Player target = Bukkit.getPlayer(args[0]) != null ? Bukkit.getPlayer(args[0]) : null;
            if (target == null) return p.sendMessage(tl("playerNotOnline", args[0]));
            target.setFoodLevel(20);
            target.setSaturation(20);
            p.sendMessage(tl("feedOther"));
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1 && sender.hasPermission("desticore.feed.other")) {
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
	