package com.desticube.core.commands;

import com.desticube.core.api.enums.RequestType;
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
        command = "tpahere",
        description = "Request to teleport to another player",
        aliases = {"tprequesthere"},
        usageARGS = "(name)",
        permissions = {})
public class TeleportRequestHere extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            Player tempPlayer = Bukkit.getPlayer(args[0]);
            switch (p.sendTeleportRequest(tempPlayer, RequestType.HERE)) {
                case PLAYER_NOT_ONLINE -> p.sendMessage(tl("playerNotOnline", args[0]));
                case CANT_BE_YOURSELF -> p.sendMessage(tl("cannotTeleportToYourself"));
                case CANT_USE -> p.sendMessage(tl("teleportRequestAlreadySent"));
                case NOT_ALLOWED -> p.sendMessage(tl("playerNotAcceptingTpRequests"));
                case SUCCESS -> {
                    p.sendMessage(tl("teleportRequestHereSender", args[0]));
                    tempPlayer.sendMessage(tl("teleportRequestHereReciever", p.getNickName()));
                }
                case EVENT_CANCELLED -> {
                    break;
                }
                default -> {
                    break;
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
        } else {
            return Collections.emptyList();
        }
    }
}
	