package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "TeleportAuto",
        description = "Automatically accept teleport requests",
        aliases = {"tpaauto"},
        usageARGS = "",
        permissions = {})
public class TeleportAuto extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.toggleTPAuto()) {
            case ENABLED -> p.sendMessage(tl("tpAutoEnabled"));
            case DISABLED -> p.sendMessage(tl("tpAutoDisabled"));
            default -> {
            }
        }
        return false;
    }
}
	