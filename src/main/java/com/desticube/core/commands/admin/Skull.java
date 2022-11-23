package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.items.DuckSkull;
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
        command = "skull",
        description = "Get a skull of yourself or someone else",
        aliases = {},
        usageARGS = "[player]",
        permissions = {"desticore.skull"})
public class Skull extends DestiCommand {

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.skull")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.getInventory().addItem(new DuckSkull().fromUuid(p.getUniqueId()));
        else
            p.getInventory().addItem(args[0].startsWith("http") ? new DuckSkull().fromUrl(args[0]) : new DuckSkull().fromUuid(Bukkit.getOfflinePlayer(args[0]).getUniqueId()));
        return p.sendMessage(tl("skullGiven"));
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
