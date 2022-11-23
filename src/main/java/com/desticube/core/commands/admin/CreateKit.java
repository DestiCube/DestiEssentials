package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.KitNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "createkit",
        description = "Create a kit",
        aliases = {"newkit"},
        usageARGS = "(name) (cooldownSeconds) (guiSlot)",
        permissions = {"desticore.createkit"})
public class CreateKit extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.createkit")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length <= 2) p.sendMessage(USAGE);
        else {
            try {
                SERVER.getKit(args[0]);
                return p.sendMessage(tl("kitExists", args[0]));
            } catch (KitNotFoundException e) {
                if (Long.valueOf(args[1]) == null) return p.sendMessage(tl("notANumber", args[1]));
                if (Integer.valueOf(args[2]) == null) return p.sendMessage(tl("notANumber", args[2]));
                SERVER.createKit(args[0], Integer.valueOf(args[2]), p.getInventory().getContents(), Long.valueOf(args[1]));
                return p.sendMessage(tl("kitCreated", args[0]));
            }
        }
        return false;
    }

}
