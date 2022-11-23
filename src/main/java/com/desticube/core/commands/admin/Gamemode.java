package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "Gamemode",
        description = "Set your gamemode",
        aliases = {"gm", "gmc", "gma", "gms", "gmsp", "creative", "survival", "adventure", "spectator"},
        usageARGS = "(gamemode) [player]",
        permissions = {"desticore.gamemode", "desticore.gamemode.other"})
public class Gamemode extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.gamemode")) return p.sendMessage(NO_PERMISSIONS);
        if (!label.equalsIgnoreCase("gamemode") && !label.equalsIgnoreCase("gm") && args.length == 0) {
            p.sendMessage(tl("gamemodeSet", getGamemodeName(label)));
            p.player().setGameMode(getGameMode(label));
        } else if (args.length == 1) {
            p.sendMessage(tl("gamemodeSet", getGamemodeName(args[0])));
            p.player().setGameMode(getGameMode(args[0]));
        } else if (args.length == 2) {
            if (!p.hasPermission("desticore.gamemode.other")) return p.sendMessage(NO_PERMISSIONS);
            Player target = Bukkit.getPlayer(args[1]);
            if (!target.isOnline()) return p.sendMessage(tl("playerNotOnline"));
            p.sendMessage(tl("gamemodeSetOther", getGamemodeName(args[0]), args[1]));
            target.sendMessage(tl("gamemodeSet", getGamemodeName(args[0])));
            target.setGameMode(getGameMode(args[0]));
        }
        return true;
    }

    private String getGamemodeName(String s) {
        String name = null;
        switch (s.toLowerCase()) {
            case "creative" -> name = "creative";
            case "c" -> name = "creative";
            case "gmc" -> name = "creative";
            case "survival" -> name = "survival";
            case "s" -> name = "survival";
            case "gms" -> name = "survival";
            case "spectator" -> name = "spectator";
            case "sp" -> name = "spectator";
            case "gmsp" -> name = "spectator";
            case "adventure" -> name = "adventure";
            case "a" -> name = "adventure";
            case "gma" -> name = "adventure";
            default -> name = "ERROR";
        }
        return name;
    }

    private GameMode getGameMode(String s) {
        GameMode gamemode = null;
        switch (s.toLowerCase()) {
            case "creative" -> gamemode = GameMode.CREATIVE;
            case "c" -> gamemode = GameMode.CREATIVE;
            case "gmc" -> gamemode = GameMode.CREATIVE;
            case "survival" -> gamemode = GameMode.SURVIVAL;
            case "s" -> gamemode = GameMode.SURVIVAL;
            case "gms" -> gamemode = GameMode.SURVIVAL;
            case "spectator" -> gamemode = GameMode.SPECTATOR;
            case "sp" -> gamemode = GameMode.SPECTATOR;
            case "gmsp" -> gamemode = GameMode.SPECTATOR;
            case "adventure" -> gamemode = GameMode.ADVENTURE;
            case "a" -> gamemode = GameMode.ADVENTURE;
            case "gma" -> gamemode = GameMode.ADVENTURE;
            default -> gamemode = GameMode.SURVIVAL;
        }
        return gamemode;
    }


    final ArrayList<String> firstArg = Lists.newArrayList("creative", "c", "survival", "s", "spectator", "sp", "adventure", "a");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            ArrayList<String> finalFirstArg = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], firstArg, finalFirstArg);
            return finalFirstArg;
        } else if (args.length == 2) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[1], players, finalPlayers);
            return finalPlayers;
        } else return Collections.emptyList();
    }
}

