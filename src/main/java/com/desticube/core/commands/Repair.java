package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;

import java.util.ArrayList;

@DuckCommand(
        command = "fix",
        description = "Fix a single item, or all your items",
        aliases = {"fixall"},
        usageARGS = "[all]",
        permissions = {"desticore.repair", "desticore.repair.all"})
public class Repair extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.repair")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length == 0 && !label.equalsIgnoreCase("fixall")) {
            if (!p.canUseCooldown("fix"))
                return p.sendMessage(tl("commandOnCooldown", p.getCommandCooldownLeft("fix")));
            ItemStack item = p.getInventory().getItemInMainHand();
            if (item == null || item.getType() == Material.AIR) return p.sendMessage(tl("cannotRepairAir"));
            if (!(item.getItemMeta() instanceof Damageable)) return p.sendMessage(tl("cannotRepairItem"));
            Damageable meta = (Damageable) item.getItemMeta();
            if (!(meta.hasDamage())) return p.sendMessage(tl("cannotRepairItem", item.getType().toString()));
            meta.setDamage(0);
            item.setItemMeta(meta);
            p.sendMessage(tl("repairedItem", item.getType().toString()));
            p.useCooldown("fix");
        } else if (args[0].equalsIgnoreCase("all") || label.equalsIgnoreCase("fixall")) {
            if (!p.canUseCooldown("fixall"))
                return p.sendMessage(tl("commandOnCooldown", p.getCommandCooldownLeft("fixall")));
            ArrayList<String> itemsRepaired = Lists.newArrayList();
            for (ItemStack item : p.getInventory().getContents()) {
                if (item == null || item.getType() == Material.AIR) continue;
                if (!(item.getItemMeta() instanceof Damageable)) continue;
                Damageable meta = (Damageable) item.getItemMeta();
                if (!(meta.hasDamage())) continue;
                meta.setDamage(0);
                item.setItemMeta(meta);
                itemsRepaired.add(item.getType().toString());
            }
            p.sendMessage(tl("repairedInventory", itemsRepaired.toString()));
            p.useCooldown("fixall");
        }
        return false;
    }

}
	