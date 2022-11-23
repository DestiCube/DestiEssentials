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
        command = "Heal",
        description = "Make the gods heal you",
        aliases = {},
        usageARGS = "[player]",
        permissions = {"desticore.heal"})
public class Heal extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.heal")) return p.sendMessage(NO_PERMISSIONS);
        if (!p.canUseCooldown("heal")) return p.sendMessage(tl("commandOnCooldown", p.getCommandCooldownLeft("heal")));
        if (args.length == 0) {
            p.player().setHealth(p.player().getHealthScale());
            p.player().setFoodLevel(20);
            p.player().setSaturation(20);
            p.sendMessage(tl("heal"));
            p.useCooldown("heal");
        } else {
            if (!p.hasPermission("desticore.heal.other")) return p.sendMessage(NO_PERMISSIONS);
            Player target = Bukkit.getPlayer(args[0]) != null ? Bukkit.getPlayer(args[0]) : null;
            if (target == null) return p.sendMessage(tl("playerNotOnline", args[0]));
            target.setHealth(target.getHealthScale());
            target.setFoodLevel(20);
            target.setSaturation(20);
            p.sendMessage(tl("healOther"));
        }
        return false;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1 && sender.hasPermission("desticore.heal.other")) {
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
	