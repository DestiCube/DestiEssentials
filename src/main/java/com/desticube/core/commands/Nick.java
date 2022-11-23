package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "Nick",
        description = "Give yourself a nickname",
        aliases = {"nickname"},
        usageARGS = "(name)",
        permissions = {"desticore.nick"})
public class Nick extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.nick")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) {
            p.setNickName(null);
            p.sendMessage(tl("nickNameReset"));
        } else {
            if (args[0].length() > 16) return p.sendMessage(tl("nickNameTooLong"));
            p.setNickName(args[0]);
            p.sendMessage(tl("nickNameSet", args[0]));
        }
        return false;
    }
}
	