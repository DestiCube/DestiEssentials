package com.desticube.core.commands.admin;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.text;

@DuckCommand(
        command = "teleportoffline",
        description = "Teleport to anyone even if they are offline!",
        aliases = {"tpoffline", "tpo", "otp", "offlinetp", "offlineteleport"},
        usageARGS = "(name)",
        permissions = {"desticore.teleport.offline"})
public class TeleportOffline extends DestiCommand {
    Location loc = null;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.teleport.offline")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
            if (!target.hasPlayedBefore()) return p.sendMessage(tl("playerNeverJoined", args[0]));
            SERVER.getPlayerDatabase().getLogoutLocation(target.getUniqueId().toString()).thenAccept((location) -> {
                if (location == null) p.sendMessage(text("Something went wrong"));
                loc = location;
            });
            while (loc == null) {
            }
            p.teleport(loc, TeleportReason.MISC, (b) -> {
            });
            return p.sendMessage(tl("teleportOffline", args[0]));
        }
    }

}
