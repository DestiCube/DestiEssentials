package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "SmithingTable",
        description = "Opens a virtual smithing table",
        aliases = {"smt", "st"},
        usageARGS = "",
        permissions = {"desticore.smithingtable"})
public class SmithingTable extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.smithingtable")) return p.sendMessage(NO_PERMISSIONS);
        p.player().openSmithingTable(null, true);
        p.sendMessage(tl("smithingTableOpened"));
        return false;
    }
}
	