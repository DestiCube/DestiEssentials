package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.KitNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "delkit",
        description = "Delete a kit",
        aliases = {"deletekit"},
        usageARGS = "(name)",
        permissions = {"desticore.delkit"})
public class DelKit extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.delkit")) {
            return p.sendMessage(NO_PERMISSIONS);
        }
        if (args.length <= 1) return p.sendMessage(USAGE);
        else {
            try {
                SERVER.delKit(args[0]);
                return p.sendMessage(tl("kitDeleted", args[0]));
            } catch (KitNotFoundException e) {
                return p.sendMessage(tl("kitNotValid", args[0]));
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            List<String> kits = Lists.newArrayList();
            SERVER.getKits().forEach(k -> kits.add(k.name()));
            List<String> newKits = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], kits, newKits);
            return newKits;
        } else {
            return Collections.emptyList();
        }
    }

}
