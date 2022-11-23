package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Maps;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

@DuckCommand(
        command = "Condense",
        description = "Condense all possible items in your inventory",
        aliases = {"compact"},
        usageARGS = "",
        permissions = {"desticore.condense"})
public class Condense extends DestiCommand {

    final HashMap<Material, Material> condenseList = Maps.newHashMap();

    public Condense() {
        condenseList.put(Material.IRON_INGOT, Material.IRON_BLOCK);
        condenseList.put(Material.IRON_NUGGET, Material.IRON_INGOT);
        condenseList.put(Material.GOLD_INGOT, Material.GOLD_BLOCK);
        condenseList.put(Material.GOLD_NUGGET, Material.GOLD_INGOT);
        condenseList.put(Material.DIAMOND, Material.DIAMOND_BLOCK);
        condenseList.put(Material.EMERALD, Material.EMERALD_BLOCK);
        condenseList.put(Material.NETHERITE_INGOT, Material.NETHERITE_BLOCK);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.condense")) return p.sendMessage(NO_PERMISSIONS);
        HashMap<Material, Integer> returnAmount = Maps.newHashMap();
        HashMap<Material, Integer> giveAmount = Maps.newHashMap();
        for (ItemStack item : p.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            if (!condenseList.keySet().contains(item.getType())) continue;
            else {
                if (item.getAmount() > 9) {
                    int total = item.getAmount() / 9;
                    int remainder = item.getAmount() % 9;
                    Material giveItem = condenseList.get(item.getType());
                    if (returnAmount.containsKey(item.getType())) returnAmount.put(item.getType(), returnAmount.get(item.getType()) + remainder);
                    else returnAmount.put(item.getType(), remainder);
                    if (giveAmount.containsKey(giveItem)) giveAmount.put(giveItem, giveAmount.get(giveItem) + total);
                    else giveAmount.put(giveItem, total);
                    p.getInventory().remove(item);
                } else continue;
            }
        }
        returnAmount.keySet().forEach(mat -> p.getInventory().addItem(new ItemStack(mat, returnAmount.get(mat))));
        giveAmount.keySet().forEach(mat -> p.getInventory().addItem(new ItemStack(mat, returnAmount.get(mat))));
        p.sendMessage(tl("condensed"));
        return false;
    }
}
	