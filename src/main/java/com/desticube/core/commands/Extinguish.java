package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "extinguish",
        description = "Put out the flames",
        aliases = {"ex", "ext"},
        usageARGS = "",
        permissions = {"desticore.extinguish"})
public class Extinguish extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.extinguish")) return p.sendMessage(NO_PERMISSIONS);
        p.player().setFireTicks(0);
        p.sendMessage(tl("extinguish"));
        return false;
    }
}
	