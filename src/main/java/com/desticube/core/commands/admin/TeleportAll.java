package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "teleportall",
        description = "",
        aliases = {"tpall"},
        usageARGS = "",
        permissions = {"desticore.teleport.all"})
public class TeleportAll extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.teleport.all")) return p.sendMessage(NO_PERMISSIONS);
        p.sendMessage(tl("teleportAll"));
        for (Player targets : Bukkit.getOnlinePlayers()) {
            if (targets.getUniqueId() == p.getUniqueId()) continue;
            targets.teleport(p.getLocation());
        }
        return false;
    }
}
	