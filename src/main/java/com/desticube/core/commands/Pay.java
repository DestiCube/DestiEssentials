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
        command = "Pay",
        description = "Pay someone some money",
        aliases = {},
        usageARGS = "(player) (amount)",
        permissions = {})
public class Pay extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length <= 1) return p.sendMessage(USAGE);
        else {
            Player player = Bukkit.getPlayer(args[0]);
            if (!isDouble(args[1])) return p.sendMessage(tl("notANumber", args[1]));
            switch (p.pay(player, Double.valueOf(args[1]))) {
                case PLAYER_NOT_ONLINE -> {
                    p.sendMessage(tl("playerNotOnline"));
                }
                case NOT_ALLOWED -> {
                    p.sendMessage(tl("playerDoesntHavePaymentsEnabled", args[1]));
                }
                case MAX -> {
                    p.sendMessage(tl("cantPayMoreThanYouHave"));
                }
                case CANT_BE_YOURSELF -> p.sendMessage(tl("cantPayYourself"));
                case EVENT_CANCELLED -> {
                }
                case SUCCESS -> {
                    p.sendMessage(tl("paySuccessSender", args[0], SERVER.formatCurrency(Double.valueOf(args[1]))));
                    player.sendMessage(tl("paySuccessReciever", p.getNickName(), SERVER.formatCurrency(Double.valueOf(args[1]))));
                }
                default -> {
                }
            }
        }
        return false;
    }

    public boolean isDouble(String d) {
        try {
            Double.valueOf(d);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    final ArrayList<String> numbers = Lists.newArrayList("10", "100", "1000", "10000", "100000", "1000000");
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], players, finalPlayers);
            return finalPlayers;
        } else if (args.length == 2) {
            ArrayList<String> finalNumbers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], numbers, finalNumbers);
            return finalNumbers;
        } else {
            return Collections.emptyList();
        }
    }

}
	