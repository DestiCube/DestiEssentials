package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "PayToggle",
        description = "Choose wether to accept money or not",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class PayToggle extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.togglePay()) {
            case ENABLED -> p.sendMessage(tl("payToggleEnabled"));
            case DISABLED -> p.sendMessage(tl("payToggleDisabled"));
            default -> {
            }
        }
        return false;
    }
}
	