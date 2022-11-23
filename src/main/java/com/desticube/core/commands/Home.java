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
        command = "home",
        description = "Teleport to a home",
        aliases = {},
        usageARGS = "(name)",
        permissions = {"desticore.home.other"})
public class Home extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) {
            switch (p.teleportHome("home")) {
                case NO_NAME -> p.sendMessage(tl("availableHomes", p.getHomesAsNames().toString()));
                case SUCCESS -> p.sendMessage(tl("teleporting"));
                case EVENT_CANCELLED -> {
                }
                default -> {
                    break;
                }
            }
        } else {
//			if (args[0].contains(":") && p.hasPermission("desticore.home.other")) {
//				String[] split = args[0].split(":");
//				OfflinePlayer target = Bukkit.getOfflinePlayer(split[0]);
//				if (!target.hasPlayedBefore()) return p.sendMessage("Player doesnt exist in database...?");
//				List<com.desticube.core.api.objects.records.Home> playersHome = new ArrayList<com.desticube.core.api.objects.records.Home>();
//				SERVER.getPlayerDatabase().getHomes(target.getUniqueId().toString(), (h) -> {
//					Optional<com.desticube.core.api.objects.records.Home> returned = h.stream().filter(home -> home.name().equalsIgnoreCase(split[1])).findFirst();
//					if (returned.isEmpty()) playersHome.add(null);
//					else playersHome.add(returned.get());
//				});
//				if (playersHome.get(0) == null) return p.sendMessage("Player doesnt have a home with this name");
//				else p.teleport(playersHome.get(0).location(), TeleportReason.MISC, (b) -> {});
//			} else {
            switch (p.teleportHome(args[0])) {
                case NO_NAME -> p.sendMessage(tl("availableHomes", p.getHomesAsNames().toString()));
                case SUCCESS -> p.sendMessage(tl("teleporting"));
                case EVENT_CANCELLED -> {
                }
                default -> {
                    break;
                }
            }
//			}
        }
        return false;
    }

//	@Override
//	public List<String> onTabComplete(CommandSender sender, org.bukkit.command.Command cmd, String label,
//			String[] args) {
//		DestiPlayer p = player(((Player) sender));
//		if (args.length >= 1) {
//			List<String> cmds = new ArrayList<String>();
//			if (args[0].contains(":")) {
//				if (p.hasPermission("desticore.home.other")) {
//					String[] split = args[0].split(":");
//					OfflinePlayer target = Bukkit.getOfflinePlayer(split[0]);
//					if (!target.hasPlayedBefore()) return Collections.emptyList();
//					SERVER.getPlayerDatabase().getHomes(target.getUniqueId().toString(), (h) -> {
//						h.forEach((home) -> cmds.add(target.getName() + ":" + home.name()));
//					});
//				}
//			} else p.getHomes().forEach(h -> cmds.add(h.name()));
//
//			return cmds;
//		} else {
//			return Collections.emptyList();
//		}
//	}


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            DestiPlayer p = player(((Player) sender));
            List<String> homes = Lists.newArrayList();
            p.getHomes().forEach(h -> homes.add(h.name()));
            List<String> newHomes = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], homes, newHomes);
            return newHomes;
        } else {
            return Collections.emptyList();
        }
    }
}
