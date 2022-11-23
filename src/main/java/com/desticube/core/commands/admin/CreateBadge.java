package com.desticube.core.commands.admin;//package com.desticube.core.com.desticube.core.commands.admin;
//
//import com.desticube.core.api.exceptions.BadgeNotFoundException;
//import com.desticube.core.api.objects.DestiPlayer;
//import com.desticube.core.com.desticube.core.commands.handlers.DestiCommand;
//import com.gamerduck.commons.com.desticube.core.commands.DuckCommand;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//
//@DuckCommand(
//        command = "createbadge",
//        description = "Create a new badge",
//        aliases = {"newbadge"},
//        usageARGS = "(name) (character)",
//        permissions = {"desticore.createbadge"})
//public class CreateBadge extends DestiCommand {
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        DestiPlayer p = player(((Player) sender));
//        if (!p.hasPermission("desticore.createbadge")) return p.sendMessage(NO_PERMISSIONS);
//        if (args.length <= 1) p.sendMessage(USAGE);
//        else {
//            try {
//                SERVER.getBadge(args[0]);
//                return p.sendMessage(tl("badgeExists", args[0]));
//            } catch (BadgeNotFoundException e) {
//                SERVER.createBadge(args[0], args[1]);
//                return p.sendMessage(tl("badgeCreated", args[0]));
//            }
//        }
//        return false;
//    }
//
//}
