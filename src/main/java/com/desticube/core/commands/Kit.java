package com.desticube.core.commands;

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
        command = "kit",
        description = "Use a kit",
        aliases = {},
        usageARGS = "(name)",
        permissions = {"desticore.kit.(name)"})
public class Kit extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) {
            SERVER.openKitsMenu(p);
        } else {
            switch (p.useKit(args[0])) {
                case NO_PERMISSIONS -> p.sendMessage(tl("noPermissionsForKit", args[0]));
                case CANT_USE -> p.sendMessage(tl("kitNotAvaliable", p.getKitCooldownLeftFormatted(args[0])));
                case DOESNT_EXIST -> p.sendMessage(tl("kitNotValid", args[0]));
                case SUCCESS -> p.sendMessage(tl("kitRecieved", args[0]));
                case EVENT_CANCELLED -> {
                }
                default -> {
                    break;
                }
            }
        }
        return false;
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
