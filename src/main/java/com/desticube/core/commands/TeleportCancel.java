package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "tpcancel",
        description = "Accept a teleport from another player",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class TeleportCancel extends DestiCommand {

    private DestiPlayer tempPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.cancelTeleportRequest((player) -> tempPlayer = player)) {
            case NULL -> p.sendMessage(tl("noIncomingTeleportRequests"));
            case SUCCESS -> {
                p.sendMessage(tl("teleportCancelSender", args[0]));
                tempPlayer.sendMessage(tl("teleportCancelReciever", p.getNickName()));
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
	