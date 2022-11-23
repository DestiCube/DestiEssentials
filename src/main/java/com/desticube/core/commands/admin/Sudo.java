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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "sudo",
        description = "Pretend to be someone else",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.sudo"})
public class Sudo extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.sudo")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length <= 1) p.sendMessage(USAGE);
        else {
            Player target = Bukkit.getPlayer(args[0]);
            if (target == null) return p.sendMessage(tl("playerNotOnline"));
            String baseCMD = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            String cmd = baseCMD.startsWith("/") ? baseCMD.replaceFirst("/", "") : baseCMD;
            target.performCommand(cmd);
            return p.sendMessage(tl("sudoSuccess", args[0], "/" + cmd));
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
