package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "socialspy",
        description = "Spy on other peoples messages!",
        aliases = {"spysocial"},
        usageARGS = "",
        permissions = {"desticore.socialspy"})
public class SocialSpy extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.socialspy")) return p.sendMessage(NO_PERMISSIONS);
        if (SERVER.getSocialSpy().contains(p.getUniqueId())) {
            SERVER.getSocialSpy().remove(p.getUniqueId());
            p.sendMessage(tl("socialSpyDisabled"));
        } else {
            SERVER.getSocialSpy().add(p.getUniqueId());
            p.sendMessage(tl("socialSpyEnabled"));
        }
        return false;
    }

}
