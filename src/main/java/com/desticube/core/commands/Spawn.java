package com.desticube.core.commands;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "Spawn",
        description = "Teleport to spawn",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class Spawn extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.teleport(SERVER.getSpawn(), TeleportReason.SPAWN, (b) -> {
        })) {
            case SUCCESS -> {
                p.sendMessage(tl("teleporting"));
            }
            case EVENT_CANCELLED -> {
                break;
            }
            default -> {
                break;
            }
        }
        return false;
    }
}
	