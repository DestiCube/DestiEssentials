package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "Loom",
        description = "Opens a virtual loom",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.loom"})
public class Loom extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.loom")) return p.sendMessage(NO_PERMISSIONS);
        p.player().openLoom(null, true);
        p.sendMessage(tl("loomOpened"));
        return false;
    }
}
	