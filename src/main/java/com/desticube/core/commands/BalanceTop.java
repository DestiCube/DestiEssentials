package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.economy.Account;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.kyori.adventure.text.Component.space;

@DuckCommand(
        command = "balancetop",
        description = "Check who has the most money so you can exploit them",
        aliases = {"baltop", "moneytop"},
        usageARGS = "",
        permissions = {})
public class BalanceTop extends DestiCommand {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        int[] page = {0};
        try {
            if (args.length == 1) page[0] = Integer.parseInt(args[0]) - 1;
            if (page[0] < 0) return p.sendMessage(tl("baltopPageNeedsToBePositive"));
            page[0] *= 10;
        } catch (Exception e) {
            e.printStackTrace();
        }
        SERVER.getEcoDatabase().getTopAsync(page[0], (all_balance, list) -> {
            if (list.size() == 0) {
                p.sendMessage(tl("baltopPageDoesntExist"));
                return;
            }
            p.sendMessage(tl("baltopHeader", page[0] + 1, (list.size() / 10) + 1));
            p.sendMessage(space());
            for (Account account : list) {
                page[0]++;
                p.sendMessage(tl("baltopPlayer", page[0], account.getName(), SERVER.formatCurrency(account.getBalance())));
            }
            if (page[0] <= 10) {
                p.sendMessage(space());
                p.sendMessage(tl("baltopGlobalBalance", SERVER.formatCurrency(all_balance)));
            }
            p.sendMessage(tl("baltopFooter", page[0] - 9 + 1));
        });
        return false;
    }
}
	