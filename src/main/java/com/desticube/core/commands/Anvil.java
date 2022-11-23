package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "anvil",
        description = "Opens a virtual anvil",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.anvil"})
public class Anvil extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.anvil")) return p.sendMessage(NO_PERMISSIONS);
        p.player().openAnvil(null, true);
        p.sendMessage(tl("anvilOpened"));
        return false;
    }
}
	