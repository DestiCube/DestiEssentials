package com.desticube.core.api.storage.databases;

import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.economy.Account;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.storage.Database;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class EconomyDatabase {
    static EconomyDatabase instance;

    public static EconomyDatabase a() {
        return instance;
    }

    private Database database;
    private Connection connection;
    public MysqlDataSource dataSource;
    private Plugin pl;

    public EconomyDatabase(Plugin pl, String host, String db, String username, String password, int port) throws SQLException {
        instance = this;
        this.pl = pl;
        Bukkit.getLogger().warning(Strings.color("&a - Loading Player database..."));
        connection = database.connection();

        dataSource = new MysqlConnectionPoolDataSource();

        dataSource.setServerName(host);
        dataSource.setPortNumber(port);
        dataSource.setDatabaseName(db);
        dataSource.setUser(username);
        dataSource.setPassword(password);

        connection = dataSource.getConnection();

        try (Connection conn = dataSource.getConnection()) {
            if (!conn.isValid(1)) {
                throw new SQLException("Could not establish database connection.");
            }
        }
        createTables();
    }

    public EconomyDatabase(Plugin pl) {
        instance = this;
        this.pl = pl;
        Bukkit.getLogger().warning(Strings.color("&a - Loading Player database..."));
        try {
            database = new Database("economydb", pl.getDataFolder() + File.separator + "storage" + File.separator + "databases");
        } catch (Exception e) {
            Bukkit.getLogger().warning(Strings.color("&cEconomy SQLite has failed to load: " + e.getMessage()));
            pl.getServer().getPluginManager().disablePlugin(pl);
            return;
        }
        connection = database.connection();
        createTables();
    }

    public void close() {
        database.close();
    }

    private void createTables() {
        try {
            database.createTable("CREATE TABLE IF NOT EXISTS economy (UUID VARCHAR(36) UNIQUE, NAME VARCHAR(60), BALANCE DOUBLE(64,2));");
            Bukkit.getLogger().info("Economy Table Created");

        } catch (SQLException e) {
            Bukkit.getLogger().warning(Strings.color("&cThe table has failed to load: " + e.getMessage()));
            pl.getServer().getPluginManager().disablePlugin(pl);
            return;
        }

    }

    public boolean createAccount(Account account) {
        try (PreparedStatement insert = connection.prepareStatement("INSERT INTO economy VALUES (?, ?, ?)")) {
            insert.setString(1, account.getUUID().toString());
            insert.setString(2, account.getName());
            insert.setDouble(3, account.getBalance());
            insert.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean hasAccount(Account account) {
        try (PreparedStatement select = connection.prepareStatement("SELECT UUID, BALANCE FROM economy WHERE UUID = ?")) {
            select.setString(1, account.getUUID().toString());
            try (ResultSet result = select.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void updateAccount(Account account) {
        try (PreparedStatement statement = connection.prepareStatement("UPDATE economy SET UUID = ?, NAME = ?, BALANCE = ? WHERE UUID = ?;")) {
            statement.setString(1, account.getUUID().toString());
            statement.setString(2, account.getName());
            statement.setDouble(3, account.getBalance());
            statement.setString(4, account.getUUID().toString());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getAccount(Account account, Runnable ifFailed) {
        try (PreparedStatement select = connection.prepareStatement("SELECT BALANCE FROM economy WHERE UUID = ?")) {
            select.setString(1, account.getUUID().toString());
            try (ResultSet result = select.executeQuery()) {
                if (result.next()) {
                    account.setBalance(result.getDouble(1));
                } else if (ifFailed != null) {
                    ifFailed.run();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void getOrInsertAccountAsync(Account account) {
        DestiServer.getInstance().getExecutor().execute(() -> getAccount(account, () -> createAccount(account)));
    }

    public void updateAccountAsync(Account account) {
        DestiServer.getInstance().getExecutor().execute(() -> updateAccount(account));
    }

    public void getTopAsync(int page, BiConsumer<Double, List<Account>> consumer) {
        DestiServer.getInstance().getExecutor().execute(() -> {
            try (PreparedStatement select = connection.prepareStatement("SELECT NAME, BALANCE FROM economy ORDER BY BALANCE DESC")) {
                try (ResultSet result = select.executeQuery()) {
                    double all_balance = 0;
                    List<Account> list = new ArrayList<>();
                    for (int i = 0; result.next(); i++) {
                        double balance = result.getDouble(2);
                        if (page <= i && page + 10 > i) {
                            String name = result.getString(1);
                            if (name != null && name.length() > 0) {
                                Account account = new Account(null, name);
                                account.setBalance(balance);
                                list.add(account);
                            }
                        }
                        all_balance += balance;
                    }
                    consumer.accept(all_balance, list);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

}
