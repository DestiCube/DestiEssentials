package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import net.kyori.adventure.text.Component;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

@DuckCommand(
        command = "itemname",
        description = "",
        aliases = {"rename"},
        usageARGS = "(name)",
        permissions = {"desticore.itemname"})
public class ItemName extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.itemname")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0) p.sendMessage(USAGE);
        else {
            ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
            meta.displayName(Component.text(color(String.join(" ", Arrays.copyOfRange(args, 0, args.length)))));
            p.getInventory().getItemInMainHand().setItemMeta(meta);
        }
        return false;
    }

}
