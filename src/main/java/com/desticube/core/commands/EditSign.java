package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@DuckCommand(
        command = "editsign",
        description = "Edit a sign",
        aliases = {"es"},
        usageARGS = "(name)",
        permissions = {"desticore.editsign"})
public class EditSign extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.editsign")) return p.sendMessage(NO_PERMISSIONS);
        Block block = p.player().getTargetBlock(null, 10);
        if (block.getType().toString().toLowerCase().contains("sign")) {
            Sign sign = (Sign) block.getState();
            p.player().openSign(sign);
        } else {
            p.sendMessage(tl("isntASign", block.getType().toString()));
        }
        return false;
    }

}