package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "sit",
        description = "Sit Command",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class Sit extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.isOnGround()) return p.sendMessage(tl("haveToBeOnGround"));
        if (p.isSitting()) p.unSit();
        else p.sit(p.getLocation().subtract(0, 1, 0));
        return false;
    }
}
	