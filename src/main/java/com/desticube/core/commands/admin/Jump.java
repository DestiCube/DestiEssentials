package com.desticube.core.commands.admin;

import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Set;

@DuckCommand(
        command = "jump",
        description = "Jump to where you are looking",
        aliases = {"j"},
        usageARGS = "",
        permissions = {"desticore.jump"})
public class Jump extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.jump")) return p.sendMessage(NO_PERMISSIONS);
        p.teleport(p.player().getTargetBlock(Set.of(Material.AIR), 100).getLocation(), TeleportReason.MISC, (b) -> {
        });
        return false;
    }

}
