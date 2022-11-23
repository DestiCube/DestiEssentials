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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.kyori.adventure.text.serializer.gson.GsonComponentSerializer.gson;

@DuckCommand(
        command = "msg",
        description = "Message another player",
        aliases = {"message", "pm", "dm", "privatemessage", "directmessage"},
        usageARGS = "(name) (message)",
        permissions = {})
public class Message extends DestiCommand {

    private DestiPlayer tempPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length <= 1) return p.sendMessage(USAGE);
        else {
            String message = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            switch (p.message(Bukkit.getPlayer(args[0]), message, (player) -> tempPlayer = player)) {
                case PLAYER_NOT_ONLINE -> p.sendMessage(tl("playerNotOnline", args[0]));
                case CANT_BE_YOURSELF -> p.sendMessage(tl("cantMessageYourself"));
                case SUCCESS -> {
                    p.sendMessage(tl("messageFormatSender", p.getNickName(), tempPlayer.getNickName(), message));
                    tempPlayer.sendMessage(tl("messageFormatReciever", p.getNickName(), tempPlayer.getNickName(), message));
                    Bukkit.getLogger().info(
                            gson().serialize(tl("messageFormatConsole", p.getNickName(), tempPlayer.getNickName(), message)));
                }
                case EVENT_CANCELLED -> {
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
	