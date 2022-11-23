package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "StoneCutter",
        description = "Opens a virtual StoneCutter",
        aliases = {"sc"},
        usageARGS = "",
        permissions = {"desticore.stonecutter"})
public class StoneCutter extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.stonecutter")) return p.sendMessage(NO_PERMISSIONS);
        p.player().openStonecutter(null, true);
        p.sendMessage(tl("stoneCutterOpened"));
        return false;
    }
}
	