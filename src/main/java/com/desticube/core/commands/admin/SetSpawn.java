package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "setspawn",
        description = "",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.setspawn"})
public class SetSpawn extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.setspawn")) return p.sendMessage(NO_PERMISSIONS);
        p.sendMessage(tl("spawnSet"));
        SERVER.setSpawn(p.getLocation());
        return false;
    }

}
