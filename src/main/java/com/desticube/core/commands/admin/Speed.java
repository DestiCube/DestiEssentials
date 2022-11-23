package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "speed",
        description = "Set your speed",
        aliases = {"walkspeed", "flyspeed"},
        usageARGS = "(amount)",
        permissions = {"speed"})
public class Speed extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.speed")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else {
            switch (label.toLowerCase()) {
                case "flyspeed" -> {
                    p.setFlySpeed(args[0]);
                }
                case "walkspeed" -> {
                    p.setWalkSpeed(args[0]);
                }
                case "speed" -> {
                    if (p.isOnGround()) p.setWalkSpeed(args[0]);
                    else p.setFlySpeed(args[0]);
                }
            }
        }
        return false;
    }
}
	