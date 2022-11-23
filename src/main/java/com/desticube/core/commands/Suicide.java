package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "Suicide",
        description = ".....",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class Suicide extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        p.player().setHealth(0.0);

        Bukkit.broadcast(tl("suicide", p.getNickName()));
        return false;
    }
}
	