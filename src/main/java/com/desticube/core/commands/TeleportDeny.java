package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "tpdeny",
        description = "Accept a teleport from another player",
        aliases = {"tpdecline", "tpadeny", "tpadecline"},
        usageARGS = "",
        permissions = {})
public class TeleportDeny extends DestiCommand {

    private DestiPlayer tempPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.declineTeleportRequest((player) -> tempPlayer = player)) {
            case NULL -> p.sendMessage(tl("noIncomingTeleportRequests"));
            case SUCCESS -> {
                p.sendMessage(tl("teleportDenyReciever", tempPlayer.getNickName()));
                tempPlayer.sendMessage(tl("teleportDenySender", p.getNickName()));
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
	