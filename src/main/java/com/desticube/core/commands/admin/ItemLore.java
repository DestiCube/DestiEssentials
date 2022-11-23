package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Numbers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DuckCommand(
        command = "itemlore",
        description = "",
        aliases = {"il"},
        usageARGS = "(add|set) [line] (lore)",
        permissions = {"desticore.itemlore"})
public class ItemLore extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.itemlore")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length <= 1) p.sendMessage(USAGE);
        else if (args[0].equalsIgnoreCase("add")) {
            ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
            String joined = color(String.join(" ", Arrays.copyOfRange(args, 1, args.length)));
            String[] split = joined.split("%n");
            List<String> toSet = meta.getLore().isEmpty() ? new ArrayList<String>() : meta.getLore();
            toSet.addAll(Arrays.asList(split));
            meta.setLore(toSet);
            p.getInventory().getItemInMainHand().setItemMeta(meta);
        } else if (args[0].equalsIgnoreCase("set") && args.length >= 2) {
            if (!Numbers.isInteger(args[1])) return p.sendMessage(USAGE);
            ItemMeta meta = p.getInventory().getItemInMainHand().getItemMeta();
            String joined = color(String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
            List<String> toSet = meta.getLore();
            toSet.set(Integer.valueOf(args[1]), joined);
            meta.setLore(toSet);
            p.getInventory().getItemInMainHand().setItemMeta(meta);
        }
        return false;
    }

}
