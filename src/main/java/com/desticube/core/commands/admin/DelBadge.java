package com.desticube.core.commands.admin;//package com.desticube.core.com.desticube.core.commands.admin;
//
//import com.desticube.core.api.exceptions.BadgeNotFoundException;
//import com.desticube.core.api.objects.DestiPlayer;
//import com.desticube.core.com.desticube.core.commands.handlers.DestiCommand;
//import com.gamerduck.commons.com.desticube.core.commands.DuckCommand;
//import com.google.common.collect.Lists;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.entity.Player;
//import org.bukkit.util.StringUtil;
//
//import java.util.Collections;
//import java.util.List;
//
//@DuckCommand(
//        command = "delbadge",
//        description = "Delete a kit",
//        aliases = {"deletebadge"},
//        usageARGS = "(name)",
//        permissions = {"desticore.delbadge"})
//public class DelBadge extends DestiCommand {
//
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        DestiPlayer p = player(((Player) sender));
//        if (!p.hasPermission("desticore.delbadge")) {
//            return p.sendMessage(NO_PERMISSIONS);
//        }
//        if (args.length <= 1) return p.sendMessage(USAGE);
//        else {
//            try {
//                SERVER.delBadge(args[0]);
//                return p.sendMessage(tl("badgeDeleted", args[0]));
//            } catch (BadgeNotFoundException e) {
//                return p.sendMessage(tl("badgeNotValid", args[0]));
//            }
//        }
//    }
//
//    @Override
//    public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
//                                      String[] args) {
//        if (args.length == 1) {
//            List<String> badges = Lists.newArrayList();
//            SERVER.getBadges().forEach(b -> badges.add(b.name()));
//            List<String> newBadges = Lists.newArrayList();
//            StringUtil.copyPartialMatches(args[0], badges, newBadges);
//            return newBadges;
//        } else return Collections.emptyList();
//    }
//
//}
