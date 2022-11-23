package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Numbers;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.items.DuckItem;
import com.gamerduck.commons.persistent.DataTypes;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gamerduck.commons.general.Components.translate;

@DuckCommand(
        command = "spawner",
        description = "Spy on other peoples messages!",
        aliases = {"spawner"},
        usageARGS = "give (type) [amount] [player]",
        permissions = {"desticore.spawner"})
public class Spawner extends DestiCommand {

    final List<String> secondArg;
    final List<String> thirdArg;
    final Component format;
    final List<Component> loreFormat = Lists.newArrayList();

    public Spawner() {
        format = translate(PLUGIN.getConfig().getString("MineableSpawners.SpawnerFormat"));
        loreFormat.addAll(translate(PLUGIN.getConfig().getStringList("MineableSpawners.SpawnerLoreFormat")));
        secondArg = Lists.newArrayList();
        thirdArg = Lists.newArrayList("1", "2", "4", "8", "16", "32", "64");
        for (EntityType ent : EntityType.values()) secondArg.add(ent.toString());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.spawner")) return p.sendMessage(NO_PERMISSIONS);
        if (args.length <= 1) p.sendMessage(USAGE);
        else if (args[0].equalsIgnoreCase("give")) {
            DuckItem spawner = new DuckItem();
            if (args.length >= 3 && Numbers.isNumber(args[2])) spawner.withAmount(Integer.valueOf(args[2]));
            if (args.length == 4 && Bukkit.getPlayer(args[3]) != null)
                Bukkit.getPlayer(args[3]).getInventory().addItem(spawner);
            else {
                p.getInventory().addItem(spawner
                        .withMaterial(Material.SPAWNER)
                        .withDisplayName(format.replaceText(b -> b.match("%spawner%")
                                .replacement(translateName(Strings.capitalizeEach(args[1].toLowerCase().replaceAll("_", " "))))))
                        .withLore(loreFormat));
                spawner.editMeta(meta ->
                        meta.getPersistentDataContainer().set(SERVER.getSpawnerKey(), DataTypes.ENTITY_TYPE, EntityType.valueOf(args[1].toUpperCase())));
            }
        }
        return false;
    }

    private String translateName(String name) {
        return Strings.capitalizeEach(name.toLowerCase().replaceAll("_", " "));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (!sender.hasPermission("desticore.spawner")) return Collections.emptyList();
        if (args.length == 1) return Lists.newArrayList("give");
        if (args.length == 2) {
            ArrayList<String> finalSecondArg = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[1], secondArg, finalSecondArg);
            return finalSecondArg;
        } else if (args.length == 3) {
            ArrayList<String> finalThirdArg = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[2], thirdArg, finalThirdArg);
            return finalThirdArg;
        } else if (args.length == 4) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[3], players, finalPlayers);
            return finalPlayers;
        } else {
            return Collections.emptyList();
        }
    }
}
