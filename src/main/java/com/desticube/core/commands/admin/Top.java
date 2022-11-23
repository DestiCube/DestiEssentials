package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "top",
        description = "Teleport to the highest block",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.top"})
public class Top extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.top")) return p.sendMessage(NO_PERMISSIONS);
        Location loc = p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getLocation().add(0, 1.5, 0);
        loc.setPitch(p.getLocation().getPitch());
        loc.setYaw(p.getLocation().getYaw());
        p.teleportInstant(loc);
        return p.sendMessage(tl("top"));
    }

}
