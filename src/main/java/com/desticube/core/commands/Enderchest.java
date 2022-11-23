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
        command = "enderchest",
        description = "Access your enderchest",
        aliases = {"ec"},
        usageARGS = "",
        permissions = {"desticore.enderchest", "desticore.enderchest.other"})
public class Enderchest extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
//		if (args.length > 0 && !p.hasPermission("desticore.enderchest.other")) {
        if (!p.hasPermission("desticore.enderchest")) return p.sendMessage(NO_PERMISSIONS);
        p.openInventory(p.player().getEnderChest());
//			p.player().getOpenInventory().title().replaceText(TextReplacementConfig.builder().match(MessageUtils.URL_PATTERN).replacement(url).build());
        p.sendMessage(tl("enderchestOpened"));
//		}
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1 && sender.hasPermission("desticore.enderchest.other")) {
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
	