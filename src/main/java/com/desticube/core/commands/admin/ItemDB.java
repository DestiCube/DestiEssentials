package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import static net.kyori.adventure.text.Component.text;

@DuckCommand(
        command = "itemdatabase",
        description = "",
        aliases = {"itemdb"},
        usageARGS = "",
        permissions = {"desticore.itemdatabase"})
public class ItemDB extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.itemdatabase")) return p.sendMessage(NO_PERMISSIONS);
        ItemStack item = p.getInventory().getItemInMainHand();
        p.sendMessage(text("&7[&c&l!&7] Item: &c" + item.getType().toString()));
        if (item.getItemMeta() instanceof Damageable) p.sendMessage(text("&7[&c&l!&7] This tool has &c" +
                ((Damageable) item.getItemMeta()).getDamage() + " &7uses left"));
        p.sendMessage(text("&7[&c&l!&7] Item short names: &fTODO"));
        return false;
    }
}
