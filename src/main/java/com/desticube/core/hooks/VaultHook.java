package com.desticube.core.hooks;

import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.economy.Account;
import com.desticube.core.api.storage.databases.EconomyDatabase;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class VaultHook extends AbstractEconomy {

    private Plugin main;

    public VaultHook(Plugin main) {
        this.main = main;
    }

    @Override
    public String getName() {
        return main.getDescription().getName();
    }

    @Override
    public boolean isEnabled() {
        return main.isEnabled();
    }

    @Override
    public String currencyNamePlural() {
        return DestiServer.getInstance().getCurrency(2);
    }

    @Override
    public String currencyNameSingular() {
        return DestiServer.getInstance().getCurrency(1);
    }

    @Override
    public int fractionalDigits() {
        return -1;
    }

    @Override
    public String format(double balance) {
        return DestiServer.getInstance().formatCurrency(balance);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot desposit negative funds");
        }
        Account account = DestiServer.getInstance().getAccount(player);
        if (account.getBalance() >= amount) {
            account.setBalance(account.getBalance() - amount);
            return new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, null);
        }
        return new EconomyResponse(0, account.getBalance(), ResponseType.FAILURE, "Insufficient funds");
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, String worldName, double amount) {
        return withdrawPlayer(player, amount);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double amount) {
        if (amount < 0) {
            return new EconomyResponse(0, 0, ResponseType.FAILURE, "Cannot desposit negative funds");
        }
        Account account = DestiServer.getInstance().getAccount(player);
        account.setBalance(account.getBalance() + amount);
        EconomyDatabase.a().updateAccountAsync(account);
        return new EconomyResponse(amount, account.getBalance(), ResponseType.SUCCESS, null);
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, String worldName, double amount) {
        return depositPlayer(player, amount);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        return DestiServer.getInstance().createAccount(player);
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player, String worldName) {
        return createPlayerAccount(player);
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        return DestiServer.getInstance().getAccount(player).getBalance();
    }

    @Override
    public double getBalance(OfflinePlayer player, String worldName) {
        return getBalance(player);
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        return DestiServer.getInstance().getAccount(player).getBalance() >= amount;
    }

    @Override
    public boolean has(OfflinePlayer player, String worldName, double amount) {
        return has(player, amount);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return DestiServer.getInstance().hasAccount(player);
    }

    @Override
    public boolean hasAccount(OfflinePlayer player, String worldName) {
        return hasAccount(player);
    }

    @Override
    public boolean createPlayerAccount(String name) {
        return createPlayerAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean createPlayerAccount(String name, String worldName) {
        return createPlayerAccount(name);
    }

    @Override
    public EconomyResponse depositPlayer(String name, double amount) {
        return depositPlayer(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public EconomyResponse depositPlayer(String name, String worldName, double amount) {
        return depositPlayer(name, amount);
    }

    @Override
    public double getBalance(String name) {
        return getBalance(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public double getBalance(String name, String worldName) {
        return getBalance(name);
    }

    @Override
    public boolean has(String name, double amount) {
        return has(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public boolean has(String name, String worldName, double amount) {
        return has(name, amount);
    }

    @Override
    public boolean hasAccount(String name) {
        return hasAccount(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public boolean hasAccount(String name, String worldName) {
        return hasAccount(name);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, double amount) {
        return withdrawPlayer(Bukkit.getOfflinePlayer(name), amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(String name, String worldName, double amount) {
        return withdrawPlayer(name, amount);
    }

    @Override
    public boolean hasBankSupport() {
        return false;
    }

    @Override
    public List<String> getBanks() {
        return new ArrayList<>();
    }

    @Override
    public EconomyResponse isBankMember(String name, String worldName) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse isBankOwner(String name, String worldName) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse deleteBank(String name) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse bankBalance(String name) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse bankDeposit(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse bankHas(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse bankWithdraw(String name, double amount) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }

    @Override
    public EconomyResponse createBank(String name, String worldName) {
        return new EconomyResponse(0.0, 0.0, ResponseType.FAILURE, "The bank is not supported!");
    }
}
