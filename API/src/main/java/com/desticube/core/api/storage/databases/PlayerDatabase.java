package com.desticube.core.api.storage.databases;

import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.records.*;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.storage.Database;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mysql.cj.jdbc.MysqlConnectionPoolDataSource;
import com.mysql.cj.jdbc.MysqlDataSource;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class PlayerDatabase {
    static PlayerDatabase instance;

    public static PlayerDatabase a() {
        return instance;
    }

    private Database database;
    private Connection connection;
    public MysqlDataSource dataSource;

    public PlayerDatabase(Plugin pl, String host, String db, String username, String password, int port) throws SQLException {
        instance = this;
        Bukkit.getLogger().warning(Strings.color("&a - Loading Player database..."));
//		try {database = new Database(reconnect, host, db, username, password, port);}
//		catch (Exception e) {
//			Bukkit.getLogger().warning(Strings.color("&cPlayer MySQL has failed to load: " + e.getMessage()));
//			pl.getServer().getPluginManager().disablePlugin(pl);
//			return;
//		}
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
        createTables(pl);
    }

    public PlayerDatabase(Plugin pl) {
        instance = this;
        Bukkit.getLogger().warning(Strings.color("&a - Loading Player database..."));
        try {
            database = new Database("playerdb", pl.getDataFolder() + File.separator + "storage" + File.separator + "databases");
        } catch (Exception e) {
            Bukkit.getLogger().warning(Strings.color("&cPlayer SQLite has failed to load: " + e.getMessage()));
            pl.getServer().getPluginManager().disablePlugin(pl);
            return;
        }
        connection = database.connection();
        createTables(pl);
    }

    public void close() {
        database.close();
    }

    private void createTables(Plugin pl) {
        try {
            database.createTable("CREATE TABLE IF NOT EXISTS homes (UUID VARCHAR(36), HOMENAME VARCHAR(36), WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2), LOCYAW FLOAT(64,2), LOCPITCH FLOAT(64,2));");
            Bukkit.getLogger().info("Homes Table Created");
            database.createTable("CREATE TABLE IF NOT EXISTS kits (UUID VARCHAR(36), KITNAME VARCHAR(36), DATEUSED DATE, TIMEUSED TIME);");
            Bukkit.getLogger().info("Kits Table Created");
            database.createTable("CREATE TABLE IF NOT EXISTS logout (UUID VARCHAR(36) UNIQUE, USERNAME VARCHAR(36), IPADDRESS VARCHAR(36), WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2), LOCYAW FLOAT(64,2), LOCPITCH FLOAT(64,2));");
            Bukkit.getLogger().info("Player Data Table Created");
            database.createTable("CREATE TABLE IF NOT EXISTS other (UUID VARCHAR(36) UNIQUE, NICKNAME VARCHAR(36), TPTOGGLED BOOLEAN, TPAUTO BOOLEAN, GODMODE BOOLEAN, ANTIPHANTOM BOOLEAN, BADGE VARCHAR(36));");
            Bukkit.getLogger().info("Other Data Table Created");
            database.createTable("CREATE TABLE IF NOT EXISTS cooldowns (UUID VARCHAR(36), COMMAND VARCHAR(36), DATEUSED DATE, TIMEUSED TIME);");
            Bukkit.getLogger().info("Cooldowns Table Created");
        } catch (SQLException e) {
            Bukkit.getLogger().warning("The table has failed to load: "  + e.getMessage());
            pl.getServer().getPluginManager().disablePlugin(pl);
            return;
        }

    }

    public CompletableFuture<Void> setHomes(String UUID, List<Home> homes) {
        return CompletableFuture.supplyAsync(() -> {
            ArrayList<String> savedHomes = Lists.newArrayList();
            for (Home home : homes) {
                if (savedHomes.contains(home.name())) continue;
                try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO homes VALUES (?, ?, ?, ?, ?, ?, ?, ?);")) {
                    statement.setString(1, UUID);
                    statement.setString(2, home.name());
                    statement.setString(3, home.location().getWorld().getName());
                    statement.setDouble(4, home.location().getX());
                    statement.setDouble(5, home.location().getY());
                    statement.setDouble(6, home.location().getZ());
                    statement.setDouble(7, home.location().getYaw());
                    statement.setDouble(8, home.location().getPitch());
                    statement.executeUpdate();
                    savedHomes.add(home.name());
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<List<Home>> getHomes(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            List<Home> homes = Lists.newArrayList();
            if (hasData(UUID, "homes")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT HOMENAME FROM homes WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT HOMENAME, WORLD, LOCX, LOCY, LOCZ, LOCYAW, LOCPITCH FROM homes WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("HOMENAME").equalsIgnoreCase(result.getString("HOMENAME"))) {
                                            homes.add(new Home(result.getString("HOMENAME"),
                                                    new Location(Bukkit.getWorld(result2.getString("WORLD")),
                                                            result2.getDouble("LOCX"),
                                                            result2.getDouble("LOCY"),
                                                            result2.getDouble("LOCZ"),
                                                            result2.getFloat("LOCYAW"),
                                                            result2.getFloat("LOCPITCH"))));
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return homes;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> clearHomes(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            if (hasData(UUID, "homes")) {
                try (PreparedStatement insert = connection.prepareStatement("DELETE FROM homes WHERE UUID = ?;")) {
                    insert.setString(1, UUID);
                    insert.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }

//	public void deleteHomes(String UUID, ArrayList<Home> homesToDelete) {
//		DestiServer.getInstance().getExecutor().execute(() -> {
//			for (Home home : homesToDelete) {
//				if (hasData(UUID, "homes")) {
//					try (PreparedStatement insert = connection.prepareStatement("DELETE FROM homes WHERE UUID = ? AND HOMENAME = ?;")) {
//						insert.setString(1, UUID);
//						insert.setString(2, home.name());
//						insert.executeUpdate();
//					} catch (SQLException ex) {
//						ex.printStackTrace();
//					}
//				}
//			}
//		});
//	}

    public CompletableFuture<HashMap<Kit, LocalDateTime>> getKits(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<Kit, LocalDateTime> kits = Maps.newHashMap();
            if (hasData(UUID, "kits")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT KITNAME FROM kits WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT KITNAME, DATEUSED, TIMEUSED FROM kits WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("KITNAME").equalsIgnoreCase(result.getString("KITNAME"))) {
                                            kits.put(DestiServer.getInstance().getKit(result.getString("KITNAME")),
                                                    LocalDateTime.of(result2.getDate("DATEUSED").toLocalDate(),
                                                            result2.getTime("TIMEUSED").toLocalTime()));
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return kits;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> setKits(String UUID, Map<Kit, LocalDateTime> usedKits) {
        return CompletableFuture.supplyAsync(() -> {
            for (Kit kit : usedKits.keySet()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO kits VALUES (?, ?, ?, ?);")) {
                    statement.setString(1, UUID);
                    statement.setString(2, kit.name());
                    statement.setDate(3, Date.valueOf(usedKits.get(kit).toLocalDate()));
                    statement.setTime(4, Time.valueOf(usedKits.get(kit).toLocalTime()));
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> clearKits(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            if (hasData(UUID, "kits")) {
                try (PreparedStatement insert = connection.prepareStatement("DELETE FROM kits WHERE UUID = ?;")) {
                    insert.setString(1, UUID);
                    insert.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }


//	cooldowns (UUID VARCHAR(36), COMMAND VARCHAR(36), DATEUSED DATE, TIMEUSED TIME);");

    public CompletableFuture<HashMap<CommandCooldown, LocalDateTime>> getCooldowns(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            HashMap<CommandCooldown, LocalDateTime> cooldowns = new HashMap<CommandCooldown, LocalDateTime>();
            if (hasData(UUID, "cooldowns")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT COMMAND FROM cooldowns WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT COMMAND, DATEUSED, TIMEUSED FROM cooldowns WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("COMMAND").equalsIgnoreCase(result.getString("COMMAND"))) {
                                            cooldowns.put(
                                                    DestiServer.getInstance().getCooldown(result.getString("COMMAND")),
                                                    LocalDateTime.of(result2.getDate("DATEUSED").toLocalDate(),
                                                            result2.getTime("TIMEUSED").toLocalTime()));
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return cooldowns;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> setCooldowns(String UUID, Map<CommandCooldown, LocalDateTime> cooldowns) {
        return CompletableFuture.supplyAsync(() -> {
            for (CommandCooldown cc : cooldowns.keySet()) {
                try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO cooldowns VALUES (?, ?, ?, ?);")) {
                    statement.setString(1, UUID);
                    statement.setString(2, cc.name());
                    statement.setDate(3, Date.valueOf(cooldowns.get(cc).toLocalDate()));
                    statement.setTime(4, Time.valueOf(cooldowns.get(cc).toLocalTime()));
                    statement.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> clearCooldowns(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            if (hasData(UUID, "cooldowns")) {
                try (PreparedStatement insert = connection.prepareStatement("DELETE FROM cooldowns WHERE UUID = ?;")) {
                    insert.setString(1, UUID);
                    insert.executeUpdate();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }
    //logout (UUID VARCHAR(36), USERNAME VARCHAR(36), IPADDRESS VARCHAR(36), WORLD VARCHAR(36), LOCX DOUBLE(64,2), LOCY DOUBLE(64,2), LOCZ DOUBLE(64,2), LOCYAW FLOAT(64,2), LOCPITCH FLOAT(64,2));");

    public CompletableFuture<LogOutData> getLogoutData(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            String username = "INVALID";
            String ipAddress = "INVALID";
            if (hasData(UUID, "logout")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM logout WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT UUID, USERNAME, IPADDRESS FROM logout WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("UUID").equalsIgnoreCase(result.getString("UUID"))) {
                                            username = result2.getString("USERNAME");
                                            ipAddress = result2.getString("IPADDRESS");
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return new LogOutData(username, ipAddress);
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Location> getLogoutLocation(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            Location logOutLoc = null;
            if (hasData(UUID, "logout")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM logout WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT UUID, WORLD, LOCX, LOCY, LOCZ, LOCYAW, LOCPITCH FROM logout WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("UUID").equalsIgnoreCase(result.getString("UUID"))) {
                                            logOutLoc = new Location(Bukkit.getWorld(result2.getString("WORLD")),
                                                    result2.getDouble("LOCX"),
                                                    result2.getDouble("LOCY"),
                                                    result2.getDouble("LOCZ"),
                                                    result2.getFloat("LOCYAW"),
                                                    result2.getFloat("LOCPITCH"));
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return logOutLoc;
        }, DestiServer.getInstance().getExecutor());
    }

    public CompletableFuture<Void> setLogoutData(String uuid, String username, String ipAddress, Location loc) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO logout VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
                statement.setString(1, uuid);
                statement.setString(2, username);
                statement.setString(3, ipAddress);
                statement.setString(4, loc.getWorld().getName());
                statement.setDouble(5, loc.getX());
                statement.setDouble(6, loc.getY());
                statement.setDouble(7, loc.getZ());
                statement.setFloat(8, loc.getYaw());
                statement.setFloat(9, loc.getPitch());
                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }

//	public void clearLogoutData(String UUID) {
//		DestiServer.getInstance().getExecutor().execute(() -> {
//			if (hasData(UUID, "logout")) {
//				try (PreparedStatement insert = connection.prepareStatement("DELETE FROM logout WHERE UUID = ?;")) {
//					insert.setString(1, UUID);
//					insert.executeUpdate();
//				} catch (SQLException ex) {
//					ex.printStackTrace();
//				}
//			}            
//		});
//	}

    public CompletableFuture<OtherData> getOtherData(String UUID) {
        return CompletableFuture.supplyAsync(() -> {
            String nick = "INVALID";
            Boolean tptoggled = true;
            Boolean tpauto = false;
            Boolean god = false;
            Boolean phantom = false;
            String badge = DestiServer.getInstance().getConfig().getString("Badges.Default");
            if (hasData(UUID, "other")) {
                try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM other WHERE UUID = ?")) {
                    select.setString(1, UUID);
                    try (ResultSet result = select.executeQuery()) {
                        while (result.next()) {
                            try (PreparedStatement select2 = connection.prepareStatement("SELECT UUID, NICKNAME, TPTOGGLED, TPAUTO, GODMODE, ANTIPHANTOM, BADGE FROM other WHERE UUID = ?")) {
                                select2.setString(1, UUID);
                                try (ResultSet result2 = select2.executeQuery()) {
                                    while (result2.next()) {
                                        if (result2.getString("UUID").equalsIgnoreCase(result.getString("UUID"))) {
                                            nick = result2.getString("NICKNAME");
                                            tptoggled = result2.getBoolean("TPTOGGLED");
                                            tpauto = result2.getBoolean("TPAUTO");
                                            god = result2.getBoolean("GODMODE");
                                            phantom = result2.getBoolean("ANTIPHANTOM");
                                            badge = result2.getString("BADGE");
                                        }
                                    }
                                }
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            return new OtherData(nick, tptoggled, tpauto, god, phantom, badge);
        }, DestiServer.getInstance().getExecutor());
    }
    //other (UUID VARCHAR(36), NICKNAME VARCHAR(36), TPTOGGLED BIT(1), TPAUTO BIT(1), GODMODE BIT(1), ANTIPHANTOM BIT(1);");

    public CompletableFuture<Void> setOtherData(String uuid, String nickname, Boolean tpToggled, Boolean tpAuto, Boolean godMode, Boolean antiPhantom, String badge) {
        return CompletableFuture.supplyAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement("INSERT OR REPLACE INTO other VALUES (?, ?, ?, ?, ?, ?, ?);")) {
                statement.setString(1, uuid);
                statement.setString(2, nickname);
                statement.setBoolean(3, tpToggled);
                statement.setBoolean(4, tpAuto);
                statement.setBoolean(5, godMode);
                statement.setBoolean(6, antiPhantom);
                statement.setString(7, badge);
                statement.executeUpdate();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return null;
        }, DestiServer.getInstance().getExecutor());
    }
//
//	public void clearOtherData(String UUID) {
//		DestiServer.getInstance().getExecutor().execute(() -> {
//			if (hasData(UUID, "other")) {
//				try (PreparedStatement insert = connection.prepareStatement("DELETE FROM other WHERE UUID = ?;")) {
//					insert.setString(1, UUID);
//					insert.executeUpdate();
//				} catch (SQLException ex) {
//					ex.printStackTrace();
//				}
//			}            
//		});
//	}


    public boolean hasData(String UUID, String table) {
        try (PreparedStatement select = connection.prepareStatement("SELECT UUID FROM " + table + " WHERE UUID = ?")) {
            select.setString(1, UUID);
            try (ResultSet result = select.executeQuery()) {
                return result.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


}
