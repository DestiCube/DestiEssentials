package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Numbers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "PersonalTime",
        description = "Set your personal time",
        aliases = {"playertime", "pt"},
        usageARGS = "(day | noon | midnight | night | time)",
        permissions = {"desticore.personaltime", "desticore.personaltime.other"})
public class PersonalTime extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.personaltime")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            if (args[0].equalsIgnoreCase("reset") || args[0].equalsIgnoreCase("clear")) p.player().resetPlayerTime();
            else if (Numbers.isLong(args[0])) p.player().setPlayerTime(Long.valueOf(args[0]), false);
            else p.player().setPlayerTime(getTime(args[0]), false);
        }
        return false;
    }

    private long getTime(String arg) {
        long time = -1;
        switch (arg.toLowerCase()) {
            case "day" -> time = 0;
            case "noon" -> time = 8000;
            case "midnight" -> time = 18000;
            case "night" -> time = 14000;
        }
        return time;
    }
}
	