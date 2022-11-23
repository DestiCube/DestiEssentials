package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.JailNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "setjail",
        description = "Create a jail",
        aliases = {"createjail", "newjail"},
        usageARGS = "(name)",
        permissions = {"desticore.setjail"})
public class SetJail extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.setjail")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else {
            try {
                SERVER.getJail(args[0]);
                return p.sendMessage(tl("jailAlreadyExists", args[0]));
            } catch (JailNotFoundException e) {
                SERVER.setJail(args[0], p.getLocation());
                return p.sendMessage(tl("jailCreated", args[0]));
            }
        }
        return false;
    }

}
