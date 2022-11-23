package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "createworld",
        description = "Become a God and create your own world",
        aliases = {},
        usageARGS = "(name)",
        permissions = {"desticore.createworld"})
public class CreateWorld extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.createworld")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length < 1) p.sendMessage(USAGE);
        else {
            if (Bukkit.getWorld(args[0]) != null) return p.sendMessage(tl("worldExists", args[0]));
            Bukkit.createWorld(new WorldCreator(args[0]));
            return p.sendMessage(tl("worldCreated", args[0]));
        }
        return false;
    }
}
