package com.desticube.core.commands.admin;

import com.desticube.core.api.exceptions.WarpNotFoundException;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@DuckCommand(
        command = "setwarp",
        description = "Create a warp",
        aliases = {"createwarp", "newwarp"},
        usageARGS = "(name)",
        permissions = {"desticore.setwarp"})
public class SetWarp extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.setwarp")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else {
            try {
                SERVER.getWarp(args[0]);
                return p.sendMessage(tl("warpAlreadyExists", args[0]));
            } catch (WarpNotFoundException e) {
                SERVER.setWarp(args[0], p.getLocation(), p.getInventory().getItemInMainHand() == null ? new ItemStack(Material.GRASS_BLOCK) : p.getInventory().getItemInMainHand());
                return p.sendMessage(tl("warpCreated", args[0]));
            }
        }
        return false;
    }

}
