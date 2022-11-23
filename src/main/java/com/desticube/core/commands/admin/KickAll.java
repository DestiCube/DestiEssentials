package com.desticube.core.commands.admin;

import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

@DuckCommand(
        command = "kickall",
        description = "Kicks all online players",
        aliases = {},
        usageARGS = "(message)",
        permissions = {"desticore.kickall"})
public class KickAll extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("desticore.kickall")) {
            sender.sendMessage(NO_PERMISSIONS);
            return false;
        }
        if (args.length == 0) sender.sendMessage(USAGE);
        else Bukkit.getOnlinePlayers().forEach(p -> p.kick(
                Component.text(String.join(" ", Arrays.copyOfRange(args, 0, args.length)))));
        return false;
    }
}
