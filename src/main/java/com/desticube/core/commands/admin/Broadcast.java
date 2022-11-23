package com.desticube.core.commands.admin;

import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import java.util.Arrays;

import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;

@DuckCommand(
        command = "broadcast",
        description = "Broadcast a message to the entire server",
        aliases = {"bc"},
        usageARGS = "(message)",
        permissions = {"desticore.broadcast"})
public class Broadcast extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof ConsoleCommandSender) && !sender.hasPermission("desticore.broadcast")) {
            sender.sendMessage(NO_PERMISSIONS);
            return false;
        }
        if (args.length == 0) sender.sendMessage(USAGE);
        else {
            String[] msg = String.join(" ", Arrays.copyOfRange(args, 0, args.length)).split("%n");
            for (String s : msg)
                PLUGIN.getServer().broadcast(miniMessage().deserialize(s));
        }
        return false;
    }

}
