package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "disposal",
        description = "A trash can",
        aliases = {"trash"},
        usageARGS = "",
        permissions = {"desticore.disposal"})
public class Disposal extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.disposal")) return p.sendMessage(NO_PERMISSIONS);
        p.openInventory(Bukkit.createInventory(p, 54, color("&bDisposal")));
        p.sendMessage(tl("disposalOpened"));
        return false;
    }
}
	