package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "tpaccept",
        description = "Accept a teleport from another player",
        aliases = {},
        usageARGS = "",
        permissions = {"desticore.teleport.warmupbypass"})
public class TeleportAccept extends DestiCommand {

    private DestiPlayer tempPlayer;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        switch (p.acceptTeleportRequest((player) -> tempPlayer = player)) {
            case PLAYER_NOT_ONLINE -> p.sendMessage(tl("playerNotOnline"));
            case NULL -> p.sendMessage(tl("noIncomingTeleportRequests"));
            case SUCCESS -> {
                p.sendMessage(tl("teleportAcceptReciever", tempPlayer.getNickName()));
                tempPlayer.sendMessage(tl("teleportAcceptSender", p.getNickName()));
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
	