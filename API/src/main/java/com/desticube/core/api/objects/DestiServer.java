package com.desticube.core.api.objects;

import com.desticube.core.api.enums.Return;
import com.desticube.core.api.exceptions.*;
import com.desticube.core.api.objects.economy.Account;
import com.desticube.core.api.objects.records.*;
import com.desticube.core.api.serializers.*;
import com.desticube.core.api.storage.databases.EconomyDatabase;
import com.desticube.core.api.storage.databases.PlayerDatabase;
import com.desticube.placeholders.api.Placeholders;
import com.gamerduck.commons.general.CenteredMessage;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.inventory.DuckInventory;
import com.gamerduck.commons.items.DuckItem;
import com.gamerduck.commons.logger.DuckLogger;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.AbstractEconomy;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

import static com.gamerduck.commons.general.Components.translate;
import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Maps.newHashMap;
import static java.io.File.separator;
import static net.kyori.adventure.text.Component.empty;
import static net.kyori.adventure.text.Component.space;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.Bukkit.*;

@SuppressWarnings("CommentedOutCode")
public class DestiServer {
    private static DestiServer instance;
    private transient ExecutorService executor;
    private transient EconomyDatabase ecoDatabase;
    private transient PlayerDatabase playerDatabase;
    private transient FileConfiguration config;
    private transient ResourceBundle messages;
    private transient JavaPlugin plugin;
    private final transient ArrayList<CommandCooldown> cooldowns = newArrayList();
    private final transient CopyOnWriteArrayList<DestiPlayer> playerCache = Lists.newCopyOnWriteArrayList();
    private transient int TPRMax, TPRMin;
    private transient World TPRWorld;
    private transient final HashMap<String, DestiPlayer> nickNames = newHashMap();
    private transient NamespacedKey spawnerKey, destiCoreKey, seatKey, godKey;
    private transient Scoreboard scoreboard;
    private transient LuckPerms luckPerms;
    private transient double defaultMoney;
    private transient final DecimalFormat formatter = new DecimalFormat("#,##0.00");
    private transient AbstractEconomy vaultHook;
    public transient final HashMap<String, Animation> animations = newHashMap();
    public transient final HashMap<String, Team> teams = newHashMap();
    public transient final String[] alphabet = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k",
            "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
    private transient final Map<UUID, Account> bankCache = newHashMap();
    private HashMap<UUID, Location> jailed;
    private Location spawn;
//    private ArrayList<Badge> badges;
    private ArrayList<Warp> warps;
    private ArrayList<Jail> jails;
    private ArrayList<Kit> kits;
    private ArrayList<UUID> socialSpy, frozen;
    private ArrayList<UUID> antiPhantom;
    private ArrayList<UUID> vanished;
    private ArrayList<UUID> godMode;


    public static DestiServer getInstance() {return instance;}
    public final ExecutorService getExecutor() {return executor;}
    public final EconomyDatabase getEcoDatabase() {return ecoDatabase;}
    public final PlayerDatabase getPlayerDatabase() {return playerDatabase;}
    public final FileConfiguration getConfig() {return config;}
    public final JavaPlugin getPlugin() {return plugin;}
    public final CopyOnWriteArrayList<DestiPlayer> getPlayerCache() {return playerCache;}
    public final ArrayList<UUID> getSocialSpy() {return socialSpy;}
    public final ArrayList<UUID> getFrozen() {return frozen;}
    public final HashMap<UUID, Location> getJailed() {return jailed;}
    public final int getTPRMax() {return TPRMax;}
    public final int getTPRMin() {return TPRMin;}
    public final World getTPRWorld() {return TPRWorld;}
    public final HashMap<String, DestiPlayer> getNickNames() {return nickNames;}
    public final NamespacedKey getSeatKey() {return seatKey;}
    public final NamespacedKey getSpawnerKey() {return spawnerKey;}
    public final NamespacedKey getDestiCoreKey() {return destiCoreKey;}
    public final NamespacedKey getGodKey() {return godKey;}
    public final ArrayList<UUID> getAntiPhantom() {return antiPhantom;}
    public final ArrayList<UUID> getVanished() {return vanished;}
    public final ArrayList<UUID> getGodMode() {return godMode;}
    public LuckPerms getPermissions() {
        return luckPerms;
    }
    public ArrayList<Kit> getKits() {return kits;}
    public AbstractEconomy getVaultHook() {return vaultHook;}
    public final HashMap<String, Animation> getAnimations() {return animations;}
    public DestiServer setup(FileConfiguration config, ResourceBundle messages, AbstractEconomy hook, JavaPlugin plugin) {
        instance = this;
        this.plugin = plugin;
        this.destiCoreKey = new NamespacedKey(plugin, "desticore_key");
        this.spawnerKey = new NamespacedKey(plugin, "spawnerKey");
        this.seatKey = new NamespacedKey(plugin, "seatKey");
        this.godKey = new NamespacedKey(plugin, "godKey");
        this.executor = Executors.newSingleThreadExecutor();
        this.config = config;
        this.messages = messages;
        this.spawn = spawn == null ? new Location(Bukkit.getWorlds().get(0), 0,0,0) : spawn;
        this.warps = warps == null ? newArrayList() : warps;
        this.kits = kits == null ? newArrayList() : kits;
        this.jails = jails == null ? newArrayList() : jails;
        this.frozen = frozen == null ? newArrayList() : frozen;
        this.socialSpy = socialSpy == null ? newArrayList() : socialSpy;
        this.jailed = jailed == null ? newHashMap() : jailed;
        this.antiPhantom = antiPhantom == null ? newArrayList() : antiPhantom;
        this.vanished = vanished == null ? newArrayList() : vanished;
        this.godMode = godMode == null ? newArrayList() : godMode;
        this.TPRMax = config.getInt("RandomTP.Max");
        this.TPRMin = config.getInt("RandomTP.Min");
        this.TPRWorld = getWorld(config.getString("RandomTP.World"));
        this.luckPerms =  getServicesManager().getRegistration(LuckPerms.class) != null ? getServicesManager().getRegistration(LuckPerms.class).getProvider() : null;
        this.scoreboard = getScoreboardManager().getMainScoreboard();
        ServerListeners.setup(this, plugin);
        setupDatabases();
        enableEconomy(hook);
        startTab();
        startAntiPhantomTimer();
        createCooldown("randomtp", 300L);
        createCooldown("fix", 28800L);
        createCooldown("fixall", 86400L);
        createCooldown("heal", 300L);
        createCooldown("feed", 300L);
        return this;
    }
    public static DestiServer loadServerData(FileConfiguration config, ResourceBundle messages, AbstractEconomy hook, JavaPlugin plugin) {
        File file = new File(plugin.getDataFolder() + separator + "storage", "server.json");
        DestiServer serverData = new DestiServer();
        if (!file.exists()) {return serverData.setup(config, messages, hook, plugin);}
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationSerializer())
                    .registerTypeAdapter(Jail.class, new JailSerializer())
                    .registerTypeAdapter(Warp.class, new WarpSerializer())
                    .registerTypeAdapter(Kit.class, new KitSerializer())
                    .registerTypeAdapter(Badge.class, new BadgeSerializer())
                    .registerTypeAdapter(UUID.class, new UUIDSerializer())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                    .create();
            Reader reader = Files.newBufferedReader(file.toPath());
            serverData = gson.fromJson(reader, DestiServer.class);
            reader.close();
        } catch (Exception ex) {ex.printStackTrace();}
        return serverData.setup(config,messages,hook, plugin);
    }

    public void shutDown() {
        File file = new File(plugin.getDataFolder() + separator + "storage",  "server.json");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try {file.createNewFile();}
            catch (IOException e) {throw new RuntimeException(e);}
        }
        try {
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(Location.class, new LocationSerializer())
                    .registerTypeAdapter(Jail.class, new JailSerializer())
                    .registerTypeAdapter(Warp.class, new WarpSerializer())
                    .registerTypeAdapter(Kit.class, new KitSerializer())
                    .registerTypeAdapter(Badge.class, new BadgeSerializer())
                    .registerTypeAdapter(UUID.class, new UUIDSerializer())
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeSerializer())
                    .create();
            Writer writer = Files.newBufferedWriter(file.toPath());
            gson.toJson(this, writer);
            writer.close();
        } catch (Exception ex) {ex.printStackTrace();}
        closeDatabases();
    }

    private void startAntiPhantomTimer() {
        getScheduler().runTaskTimer(plugin, () -> {
            antiPhantom.forEach(p -> {
                if (Bukkit.getPlayer(p) == null) return;
                Bukkit.getPlayer(p).setStatistic(Statistic.TIME_SINCE_REST, 0);
            });
        }, 900 * 20, 900 * 20);
    }

    private void startTab() {
        final List<String> order = config.getStringList("Tab-Order");
        AtomicInteger place = new AtomicInteger(0);
        order.stream().forEachOrdered(s -> {
            if (scoreboard.getTeam(alphabet[place.get()]) != null) teams.put(s, scoreboard.getTeam(alphabet[place.getAndIncrement()]));
            else teams.put(s, scoreboard.registerNewTeam(alphabet[place.getAndIncrement()]));
        });
        for (String sec : config.getConfigurationSection("Animations").getKeys(false))
            animations.put(sec, new Animation(sec, config.getStringList("Animations." + sec + ".Animations")));
        final String format = config.getString("PlayerName.Format");
        final String header = config.getString("Header.Text");
        final String footer = config.getString("Footer.Text");
        getScheduler().runTaskTimer(plugin, () -> playerCache.forEach(p -> p.setTabName(convert(p.player(), format))),
                10, config.getInt("PlayerName.RefreshRate"));
        getScheduler().runTaskTimer(plugin, () -> playerCache.forEach(p -> p.setTabHeader(convert(p.player(), header))),
                10, config.getInt("Header.RefreshRate"));
        getScheduler().runTaskTimer(plugin, () -> playerCache.forEach(p -> p.setTabFooter(convert(p.player(), footer))),
                10, config.getInt("Footer.RefreshRate"));

        getScheduler().runTaskTimer(plugin, () -> {
            for (Player p : Bukkit.getOnlinePlayers()) {
                User user = luckPerms.getUserManager().getUser(p.getUniqueId());
                for (String s : teams.keySet()) {
                    if (user.getPrimaryGroup().equalsIgnoreCase(s)) {
                        teams.get(s).addEntity(p);
                        break;
                    }
                }
            }
            Bukkit.getOnlinePlayers().forEach(p -> p.setScoreboard(scoreboard));
        }, 10, 300);

    }

    private @NotNull Component convert(Player p, String s) {
        return Placeholders.setPlaceholders(p, miniMessage().deserialize(s));
    }
    public void accountsOnJoin(Player p) {
        Account account = new Account(p.getUniqueId(), p.getName());
        account.setBalance(defaultMoney);
        ecoDatabase.getOrInsertAccountAsync(account);
        bankCache.put(p.getUniqueId(), account);
    }

    public void accountsOnQuit(Player p) {
        Account account = bankCache.remove(p.getUniqueId());
        if (account != null) ecoDatabase.updateAccountAsync(account);
    }

    public boolean createAccount(OfflinePlayer player) {
        Account account = bankCache.get(player.getUniqueId());
        if (account == null) {
            account = new Account(player);
            account.setBalance(defaultMoney);
            if (!ecoDatabase.hasAccount(account)) return ecoDatabase.createAccount(account);
        }
        return false;
    }

    public Account getAccount(OfflinePlayer player) {
        Account account = bankCache.get(player.getUniqueId());
        if (account == null) {
            account = new Account(player);
            ecoDatabase.getAccount(account, null);
        }
        return account;
    }

    public boolean hasAccount(OfflinePlayer player) {
        Account account = bankCache.get(player.getUniqueId());
        if (account == null)
            return ecoDatabase.hasAccount(new Account(player));
        return true;
    }

    public void sendMOTD(DestiPlayer p) {
        for (String s : config.getStringList("MOTD")) {
            if (s.contains("<center>")) p.player().sendMessage(convert(p.player(), CenteredMessage.generate(s)));
            else p.player().sendMessage(convert(p.player(), (s)));
        }
    }

    public void addFrozen(UUID p) {
        frozen.add(p);
    }

    public void addFrozen(Player p) {
        addFrozen(p.getUniqueId());
    }

    public void addJailed(UUID p, Location loc) {
        jailed.put(p, loc);
    }

    public void addJailed(Player p) {
        addJailed(p.getUniqueId(), p.getLocation());
    }

    public void removeFrozen(UUID p) {
        frozen.remove(p);
    }

    public void removeFrozen(Player p) {
        removeFrozen(p.getUniqueId());
    }

    public void removeJailed(UUID p) {
        jailed.remove(p);
    }

    public void removeJailed(Player p) {
        removeJailed(p.getUniqueId());
    }

    public Return freeze(Player p) {
        if (!p.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (frozen.contains(p.getUniqueId())) return Return.ALREADY_DONE;
        addFrozen(p);
        return Return.SUCCESS;
    }

    public Return unFreeze(Player p) {
        if (!p.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (!frozen.contains(p.getUniqueId())) return Return.ALREADY_DONE;
        removeFrozen(p);
        return Return.SUCCESS;
    }

    //////////////////////////////////////////////////
    //                                              //
    //      Startup and Shutdown Methods Start      //
    //                                              //
    //////////////////////////////////////////////////

    private void setupDatabases() {
        if (config.getBoolean("EconomyMySQL.Enabled")) {
            try {
                this.ecoDatabase = new EconomyDatabase(plugin,
                        config.getString("EconomyMySQL.Host"),
                        config.getString("EconomyMySQL.Database"),
                        config.getString("EconomyMySQL.Username"),
                        config.getString("EconomyMySQL.Password"),
                        config.getInt("EconomyMySQL.Port"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            ecoDatabase = new EconomyDatabase(plugin);
        }
        if (config.getBoolean("PlayerMySQL.Enabled")) {
            try {
                this.playerDatabase = new PlayerDatabase(plugin,
                        config.getString("PlayerMySQL.Host"),
                        config.getString("PlayerMySQL.Database"),
                        config.getString("PlayerMySQL.Username"),
                        config.getString("PlayerMySQL.Password"),
                        config.getInt("PlayerMySQL.Port"));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            playerDatabase = new PlayerDatabase(plugin);
        }
    }

    public void closeDatabases() {
        if (ecoDatabase != null && playerDatabase != null) {
            Bukkit.getOnlinePlayers().forEach(this::accountsOnQuit);
            getPlayerCache().forEach(player -> player.quit().thenAccept(this::removePlayer));
            ecoDatabase.close();
            playerDatabase.close();
            plugin.getServer().getServicesManager().unregister(vaultHook);
            executor.shutdownNow().forEach(Runnable::run);
        }
    }

    public void enableEconomy(AbstractEconomy vault) {
        DuckLogger.info("&a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
        DuckLogger.info("&a" + plugin.getName() + "'s economy is loading... ");
        DuckLogger.info("&a - Loading config...");
        try {
            defaultMoney = config.getDouble("Settings.DefaultMoney");
        } catch (Exception e) {
            e.printStackTrace();
            plugin.getServer().getPluginManager().disablePlugin(plugin);
            return;
        }
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setDecimalSeparator('.');
        symbols.setGroupingSeparator(',');
        formatter.setDecimalFormatSymbols(symbols);
        if (plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
            DuckLogger.info("&a - Hooking into Vault...");
            plugin.getServer().getServicesManager().register(Economy.class, vaultHook = vault, plugin, ServicePriority.High);
        }

        Bukkit.getOnlinePlayers().forEach(this::accountsOnJoin);
        DuckLogger.info("&a" + plugin.getName() + "'s economy has been loaded!");
        DuckLogger.info("&a=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");
    }


    public String getCurrency(double amount) {
        return amount == 1.0 ? config.getString("Settings.CurrencySingular") : config.getString("Settings.CurrencyPlural");
    }

    public String formatCurrency(double amount) {
        return config.getString("Settings.CurrencyFormat").replace("%money%", formatter.format(amount)).replace("%currency%", getCurrency(amount));
    }


    //////////////////////////////////////////////////
    //                                              //
    //       Startup and Shutdown Methods End       //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //            Warps Methods Start               //
    //                                              //
    //////////////////////////////////////////////////

    public void setSpawn(Location loc) {
        spawn = loc;
    }

    public Location getSpawn() {
        return spawn;
    }

    public ArrayList<Warp> getWarps() {
        return warps;
    }

    public Warp getWarp(String name) throws WarpNotFoundException {

        return warps.stream().filter(w -> w.name().equalsIgnoreCase(name)).findFirst().orElseThrow(WarpNotFoundException::new);
    }

    public boolean setWarp(String name, Location loc, ItemStack displayItem) {
        Warp warp = new Warp(name, loc, displayItem);
        warps.add(warp);
        return warps.contains(warp);
    }

    public boolean delWarp(String name) throws WarpNotFoundException {
        Warp warp = getWarp(name);
        warps.remove(warp);
        return warps.contains(warp);
    }

    //////////////////////////////////////////////////
    //                                              //
    //            Warps Methods End                 //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //            Badges Methods Start              //
    //                                              //
    //////////////////////////////////////////////////

//    public Badge getBadge(String name) throws BadgeNotFoundException {
//        return badges.stream().filter(b -> b.name().equalsIgnoreCase(name)).findFirst().orElseThrow(BadgeNotFoundException::new);
//    }
//
//    public boolean createBadge(String name, String character) {
//        Badge badge = new Badge(name, character, name, "desticore.badge." + name);
//        badges.add(badge);
//        return badges.contains(badge);
//    }
//
//    public boolean delBadge(String name) throws BadgeNotFoundException {
//        Badge badge = getBadge(name);
//        badges.remove(badge);
//        return badges.contains(badge);
//    }

    //////////////////////////////////////////////////
    //                                              //
    //            Badges Methods End                //
    //                                              //
    //////////////////////////////////////////////////
    //////////////////////////////////////////////////
    //                                              //
    //         Cooldowns Methods Start              //
    //                                              //
    //////////////////////////////////////////////////

    public CommandCooldown getCooldown(String name) {
        return cooldowns.stream().filter(k -> k.name().equalsIgnoreCase(name)).findFirst().orElseThrow(CooldownNotFoundException::new);
    }

    public void createCooldown(String name, Long cooldown) {
        CommandCooldown coolDown = new CommandCooldown(name, cooldown);
        cooldowns.add(coolDown);
    }

    //////////////////////////////////////////////////
    //                                              //
    //            Kits Methods Start                //
    //                                              //
    //////////////////////////////////////////////////

    public Kit getKit(String name) throws KitNotFoundException {
        return kits.stream().filter(k -> k.name().equalsIgnoreCase(name)).findFirst().orElseThrow(KitNotFoundException::new);
    }

    public boolean createKit(String name, Integer slot, ItemStack[] items, Long cooldown) {
        List<ItemStack> itemsList = Arrays.asList(items.clone());
        Kit kit = new Kit(name, slot, itemsList.toArray(new ItemStack[0]), cooldown);
        kits.add(kit);
        return kits.contains(kit);
    }

    public boolean delKit(String name) throws KitNotFoundException {
        Kit kit = getKit(name);
        kits.remove(kit);
        return kits.contains(kit);
    }

    //////////////////////////////////////////////////
    //                                              //
    //            Kits Methods End                  //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //        Player Cache Methods Start            //
    //                                              //
    //////////////////////////////////////////////////


    public void addPlayer(Player p, Consumer<DestiPlayer> finished) {
        new DestiPlayer(p, (b) -> {
            playerCache.add(b);
            finished.accept(b);
        });
    }

    public void removePlayer(UUID uuid) {
        playerCache.remove(getPlayer(uuid));
    }

    public DestiPlayer getPlayer(UUID uuid) {
        return playerCache.stream().filter(p -> p.getUniqueId() == uuid).findFirst().get();
    }

    public DestiPlayer getPlayer(Player player) {
        return getPlayer(player.getUniqueId());
    }

    //////////////////////////////////////////////////
    //                                              //
    //        Player Cache Methods End              //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //            Jails Methods Start               //
    //                                              //
    //////////////////////////////////////////////////

    public ArrayList<Jail> getJails() {
        return jails;
    }

    public Jail getJail(String name) throws JailNotFoundException {
        return jails.stream().filter(j -> j.name().equalsIgnoreCase(name)).findFirst().orElseThrow(JailNotFoundException::new);
    }

    public boolean setJail(String name, Location loc) {
        Jail jail = new Jail(name, loc);
        jails.add(jail);
        return jails.contains(jail);
    }

    public boolean delJail(String name) throws JailNotFoundException {
        Jail jail = getJail(name);
        jails.remove(jail);
        return jails.contains(jail);
    }

    public Return jail(Player player, String jail) {
        if (!player.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (jailed.containsKey(player.getUniqueId())) return Return.ALREADY_DONE;
        try {
            Jail j = getJail(jail);
            addJailed(player);
            player.teleport(j.location());
            return Return.SUCCESS;
        } catch (JailNotFoundException e) {
            return Return.NO_NAME;
        }
    }

    public Return unJail(Player player) {
        if (!player.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (!jailed.containsKey(player.getUniqueId())) return Return.ALREADY_DONE;
        player.teleport(jailed.get(player.getUniqueId()));
        removeJailed(player);
        return Return.SUCCESS;
    }

    //////////////////////////////////////////////////
    //                                              //
    //            Jails Methods End                 //
    //                                              //
    //////////////////////////////////////////////////


    public DuckInventory openWarpMenu(DestiPlayer p) {
        DuckInventory inv = new DuckInventory(plugin, 54, Strings.color("<dark_gray><ul>Warps Menu"));
        inv.fillRow(6, new DuckItem().withMaterial(Material.BLACK_STAINED_GLASS_PANE).withDisplayName(" "));
        warps.stream().forEach(w -> {
            DuckItem item = new DuckItem()
                    .withDisplayName(translate("<yellow>" + w.name())).withMaterial(w.displayItem().getType()).addToLore(Strings.color("&7Click to teleport"), Strings.color("&7to this warp!"));
            inv.addButton(item, e -> p.warp(w));
        });
        return inv.open(p.player());
    }

    public DuckInventory openKitsMenu(DestiPlayer p) {
        DuckInventory inv = new DuckInventory(plugin, 27, Strings.color("<dark_gray><ul>Kits Menu"));
        inv.fill(new DuckItem().withMaterial(Material.BLACK_STAINED_GLASS_PANE).withDisplayName(" "));
        kits.stream().forEach(k -> {
            if (k.invSlot() == -1) {
            } else {
                DuckItem item = new DuckItem()
                        .withMaterial(p.canUseKit(k.name()) ? Material.CHEST_MINECART : Material.MINECART)
                        .withDisplayName(translate("<red>" + k.name() + " Kit"))
                        .addToLore(space(), !p.hasPermission("desticore.kit." + k.name()) ? translate("<dark_gray> <gray>No Perms") : empty());
                inv.setButton(k.invSlot(), item, e -> {
                    p.player().closeInventory();
                    switch (p.useKit(k.name())) {
                        case NO_PERMISSIONS -> p.sendMessage(tl("noPermissionsForKit", k.name()));
                        case CANT_USE -> p.sendMessage(tl("kitNotAvaliable", p.getKitCooldownLeftFormatted(k.name())));
                        case DOESNT_EXIST -> p.sendMessage(tl("kitNotValid", k.name()));
                        case SUCCESS -> p.sendMessage(tl("kitRecieved", k.name()));
                        default -> {
                            break;
                        }
                    }
                });
            }
        });
        return inv.open(p.player());
    }

    private transient final HashMap<String, MessageFormat> formats = newHashMap();

    public Component tl(String s, Object... objects) {
        MessageFormat messageFormat;
        if (formats.containsKey(s)) messageFormat = formats.get(s);
        else {
            messageFormat = new MessageFormat(messages.getString(s));
            formats.put(s, messageFormat);
        }
        return messages.getString(s).isBlank() ? Component.empty() :
                convert(null, messageFormat.format(objects).replace(' ', ' '));
    }
}