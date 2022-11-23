package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "TeleportToggle",
        description = "Toggle wether or not you can recieve teleport requests",
        aliases = {"tptoggle"},
        usageARGS = "",
        permissions = {})
public class TeleportToggle extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.toggleTP()) {
            case ENABLED -> p.sendMessage(tl("tpToggleEnabled"));
            case DISABLED -> p.sendMessage(tl("tpToggleDisabled"));
            default -> {
            }
        }
        return false;
    }
}
	