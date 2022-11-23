package com.desticube.core.commands.admin;

import com.desticube.core.api.objects.economy.Account;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.general.Strings;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@DuckCommand(
        command = "economy",
        description = "Edit anyone's Economy status",
        aliases = {"eco"},
        usageARGS = "(give, set, take) (player) (amount)",
        permissions = {"desticore.economy"})
public class Economy extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        CommandSender p = sender;
        if (!p.hasPermission("desticore.economy")) {
            p.sendMessage(NO_PERMISSIONS);
            return true;
        }
        if (args.length == 0) {
            p.sendMessage(Strings.color("&7[&cDestiEconomy&7] Made by GamerDuck123."));
            p.sendMessage(Strings.color("&c - /eco give <name> <amount>"));
            p.sendMessage(Strings.color("&c - /eco set <name> <amount>"));
            p.sendMessage(Strings.color("&c - /eco take <name> <amount>"));
            return true;
        } else if (args.length == 3) {
            OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
            try {
                double amount = Double.parseDouble(args[2]);
                if (args[0].equals("give")) {
                    if (SERVER.hasAccount(target)) {
                        Account account = SERVER.getAccount(target);
                        account.setBalance(account.getBalance() + amount);
                        SERVER.getEcoDatabase().updateAccountAsync(account);
                        p.sendMessage(Strings.color("&2You gave &f" + SERVER.formatCurrency(amount) + " &2to &f" + target.getName()));
                    } else p.sendMessage(Strings.color("&cThe account doesn't exists!"));
                    return true;
                } else if (args[0].equals("set")) {
                    if (SERVER.hasAccount(target)) {
                        Account account = SERVER.getAccount(target);
                        account.setBalance(amount);
                        SERVER.getEcoDatabase().updateAccountAsync(account);
                        p.sendMessage(Strings.color("&2You set &f" + target.getName() + " &2to &f" + SERVER.formatCurrency(amount)));
                    } else p.sendMessage(Strings.color("&cThe account doesn't exists!"));
                    return true;
                } else if (args[0].equals("take")) {
                    if (SERVER.hasAccount(target)) {
                        Account account = SERVER.getAccount(target);
                        if (account.getBalance() < amount) {
                            p.sendMessage(Strings.color("&cNot enough funds!"));
                        } else {
                            account.setBalance(account.getBalance() - amount);
                            SERVER.getEcoDatabase().updateAccountAsync(account);
                            p.sendMessage(Strings.color("&2You took &f" + SERVER.formatCurrency(amount) + " &2from &f" + target.getName()));
                        }
                    } else p.sendMessage(Strings.color("&cThe account doesn't exists!"));
                    return true;
                }
            } catch (NumberFormatException e) {
                p.sendMessage(Strings.color("&cThe amount must be a number!"));
            }
        }
        p.sendMessage(Strings.color("&cInvalid arguments! Type /eco for available com.desticube.core.commands!"));
        return false;
    }


    final ArrayList<String> firstArg = Lists.newArrayList("give", "set", "take");
    final ArrayList<String> thirdArg = Lists.newArrayList("10", "100", "1000", "10000", "100000", "1000000");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            ArrayList<String> firstArgFinal = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[0], firstArg, firstArgFinal);
            return firstArgFinal;
        } else if (args.length == 2) {
            ArrayList<String> players = Lists.newArrayList();
            Bukkit.getOnlinePlayers().forEach(p -> players.add(p.getName()));
            ArrayList<String> finalPlayers = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[1], players, finalPlayers);
            return finalPlayers;
        } else if (args.length == 3) {
            ArrayList<String> thirdArgFinal = Lists.newArrayList();
            StringUtil.copyPartialMatches(args[2], thirdArg, thirdArgFinal);
            return thirdArgFinal;
        } else return Collections.emptyList();
    }
}
