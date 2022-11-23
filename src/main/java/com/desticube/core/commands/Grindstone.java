package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "GrindStone",
        description = "Opens a virtual grindstone",
        aliases = {"gs"},
        usageARGS = "",
        permissions = {"desticore.grindstone"})
public class Grindstone extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.grindstone")) return p.sendMessage(NO_PERMISSIONS);
        p.player().openGrindstone(null, true);
        p.sendMessage(tl("grindstoneOpened"));
        return false;
    }
}
	