package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@DuckCommand(
        command = "ClearInventory",
        description = "Clears your inventory",
        aliases = {"ci"},
        usageARGS = "",
        permissions = {"desticore.clearinventory"})
public class ClearInventory extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.clearinventory")) return p.sendMessage(NO_PERMISSIONS);
        for (ItemStack i : p.getInventory().getContents()) {
            if (i == null || i.getType() == Material.AIR) continue;
            i.setAmount(0);
        }
        p.sendMessage(tl("inventoryCleared"));
        return false;
    }
}
	