package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "sethome",
        description = "Sets a home",
        aliases = {},
        usageARGS = "(name)",
        permissions = {"desticore.sethome.(amount)"})
public class SetHome extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) {
            switch (p.setHome("home")) {
                case MAX -> p.sendMessage(tl("maxHomesSet", p.getMaxHomes()));
                case HAS_NAME -> p.sendMessage(tl("homeWithNameAlreadySet", "home"));
                case SUCCESS -> p.sendMessage(tl("homeSet", "home"));
                case EVENT_CANCELLED -> {
                }
                default -> {
                    break;
                }
            }
        } else {
            switch (p.setHome(args[0])) {
                case MAX -> p.sendMessage(tl("maxHomesSet", p.getMaxHomes()));
                case HAS_NAME -> p.sendMessage(tl("homeWithNameAlreadySet", args[0]));
                case SUCCESS -> p.sendMessage(tl("homeSet", args[0]));
                case EVENT_CANCELLED -> {
                }
                default -> {
                    break;
                }
            }
        }
        return false;
    }

}
