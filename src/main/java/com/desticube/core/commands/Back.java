package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "back",
        description = "Back Command",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.back"})
public class Back extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.back")) return p.sendMessage(NO_PERMISSIONS);
        switch (p.back()) {
            case NONE -> p.sendMessage(tl("noBackLocation"));
            case SUCCESS -> p.sendMessage(tl("goingBack"));
            case EVENT_CANCELLED -> {
            }
            default -> {
            }
        }
        return false;
    }
}
	