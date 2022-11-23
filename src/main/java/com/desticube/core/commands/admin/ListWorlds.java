package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "listworlds",
        description = "List all worlds",
        aliases = {"listworlds"},
        usageARGS = "",
        permissions = {"desticore.listworlds"})
public class ListWorlds extends DestiCommand {

    String path = Bukkit.getWorldContainer().getAbsolutePath();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.listworlds")) return p.sendMessage(NO_PERMISSIONS);

        StringBuilder builder = new StringBuilder();
        Bukkit.getWorlds().stream().forEach(w -> builder.append(w.getName() + " "));
        return p.sendMessage(tl("listWorlds", builder.toString()));

    }

}
