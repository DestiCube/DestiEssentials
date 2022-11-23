package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Strings;
import com.google.common.collect.Lists;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "enchant",
        description = "Enchant any item",
        aliases = {"ench"},
        usageARGS = "(enchant) (level)",
        permissions = {"desticore.enchant"})
public class Enchant extends DestiCommand {

    final ArrayList<String> firstArg;

    public Enchant() {
        firstArg = Lists.newArrayList();
        for (Enchantment enchant : Enchantment.values().clone()) firstArg.add(enchant.getKey().getKey());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.enchant")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length <= 1) p.sendMessage(USAGE);
        else {
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0]));
            if (ench != null) {
                int level = Integer.valueOf(args[1]);
                p.getInventory().getItemInMainHand().addUnsafeEnchantment(ench, level);
                p.sendMessage(tl("enchantItem",
                        Strings.capitalizeEach(p.getInventory().getItemInMainHand().getType().name().replaceAll("_", " ")), args[0], level));
            } else {
                p.sendMessage(tl("enchantDoesntExist", args[0]));
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            List<String> newFirstArg = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], firstArg, newFirstArg);
            return newFirstArg;
        } else if (args.length == 2) {
            List<String> secondArg = Lists.newArrayList();
            Enchantment ench = Enchantment.getByKey(NamespacedKey.minecraft(args[0]));
            for (int i = 1; i <= ench.getMaxLevel(); i++) secondArg.add(String.valueOf(i));
            List<String> newSecondArg = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[1], secondArg, newSecondArg);
            return newSecondArg;
        } else {
            return Collections.emptyList();
        }
    }

}
