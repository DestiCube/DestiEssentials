package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "antiphantom",
        description = "Check your balance",
        aliases = {"phantom"},
        usageARGS = "",
        permissions = {"desticore.antiphantom"})
public class AntiPhantom extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.antiphantom")) return p.sendMessage(NO_PERMISSIONS);
        switch (p.toggleAntiPhantom()) {
            case ENABLED -> p.sendMessage(tl("antiPhantomEnabled"));
            case DISABLED -> p.sendMessage(tl("antiPhantomDisabled"));
            default -> {
            }
        }
        return false;
    }
}
	