package com.desticube.core.api.objects;

import com.desticube.core.api.enums.RequestType;
import com.desticube.core.api.enums.Return;
import com.desticube.core.api.enums.TeleportReason;
import com.desticube.core.api.events.player.*;
import com.desticube.core.api.exceptions.CooldownNotFoundException;
import com.desticube.core.api.exceptions.KitNotFoundException;
import com.desticube.core.api.exceptions.WarpNotFoundException;
import com.desticube.core.api.objects.inclass.InClassListener;
import com.desticube.core.api.objects.records.*;
import com.desticube.core.api.storage.databases.PlayerDatabase;
import com.gamerduck.commons.events.DuckPlayerEvent;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.persistent.DataTypes;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import net.luckperms.api.cacheddata.CachedPermissionData;
import net.luckperms.api.model.user.User;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static com.gamerduck.commons.general.Numbers.isFloat;
import static net.kyori.adventure.text.minimessage.MiniMessage.miniMessage;
import static org.bukkit.Bukkit.getScheduler;

public class DestiPlayer implements InventoryHolder {

    private final Player p;
    private final DestiServer SERVER = DestiServer.getInstance();
    private final Plugin PLUGIN = SERVER.getPlugin();
    private final UUID uniqueId;
    public final UUID getUniqueId() {return uniqueId;}
    private String ipAddress, username;
    public String getIpAddress() {return ipAddress;}
    public String getUsername() {return username;}
    private final long lastLogin = System.currentTimeMillis();
    public final long getLastLogin() {return lastLogin;}
    private final String uuidString;
    public final String getUuidString() {return uuidString;}
    private final CopyOnWriteArrayList<Home> homes = Lists.newCopyOnWriteArrayList();
    public final CopyOnWriteArrayList<Home> getHomes() {return homes;}
    private final CopyOnWriteArrayList<Home> deletedHomes = Lists.newCopyOnWriteArrayList();
    public final CopyOnWriteArrayList<Home> getDeletedHomes() {return deletedHomes;}
    private final ConcurrentHashMap<Kit, LocalDateTime> usedKits = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Kit, LocalDateTime> getUsedKits() {return usedKits;}
    private final ConcurrentHashMap<CommandCooldown, LocalDateTime> commandCooldowns = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<CommandCooldown, LocalDateTime> getCommandCooldowns() {return commandCooldowns;}
    private TeleportRequest currentTeleportRequest = null;
    public TeleportRequest getCurrentTeleportRequest() {return currentTeleportRequest;}
    public void setCurrentTeleportRequest(TeleportRequest request) {currentTeleportRequest = request;}
    private boolean tpToggled, tpAuto, isAFK, sitting;
//    public boolean isGodMode() {return godMode;}
//    public void setGodMode(boolean bool) {godMode = bool;}
    public boolean isTpToggled() {return tpToggled;}
    public void setTpToggled(boolean bool) {tpToggled = bool;}
//    public boolean isAntiPhantom() {return antiPhantom;}
//    public void setAntiPhantom(boolean bool) {antiPhantom = bool;}
//    public boolean isAFK() {return isAFK;}
//    public void setIsAFK(boolean bool) {isAFK = bool;}
    public boolean isSitting() {return sitting;}
    public void setSitting(boolean bool) {sitting = bool;}
    private Location backLocation, lastLocation;
    public Location getBackLocation() {return backLocation;}
    public void setBackLocation(Location loc) {backLocation = loc;}
    private String nickName;
    public Location getLastLocation() {return lastLocation;}
    public void setLastLocation(Location loc) {lastLocation = loc;}
    public Badge currentBadge;
    public Badge getCurrentBadge() {return currentBadge;}
    public void setCurrentBadge(Badge badge) {currentBadge = badge;}

    public DestiPlayer(Player p, Consumer<DestiPlayer> loaded) {
        this.p = p;
        p.setCustomNameVisible(true);
        this.uniqueId = p.getUniqueId();
        this.uuidString = p.getUniqueId().toString();
        PlayerDatabase.a().getHomes(uuidString).thenAcceptAsync(homes::addAll);
        PlayerDatabase.a().getKits(uuidString).thenAcceptAsync(usedKits::putAll);
        PlayerDatabase.a().getCooldowns(uuidString).thenAccept(commandCooldowns::putAll);
        PlayerDatabase.a().getLogoutData(uuidString).thenAccept((data) -> {
            this.username = data.username().equalsIgnoreCase("INVALID") ? p.getName() : data.username();
            this.ipAddress = data.ip().equalsIgnoreCase("INVALID") ? p.getAddress().getHostString() : data.ip();
        });
        PlayerDatabase.a().getOtherData(uuidString).thenAccept((data) -> {
            this.nickName = data.nickName().equalsIgnoreCase("INVALID") ? null : data.nickName();
            this.tpToggled = data.tpToggled();
            this.tpAuto = data.tpAuto();
//            this.godMode = data.godMode();
//            this.antiPhantom = data.antiPhantom();
//            try {
//                this.currentBadge = SERVER.getBadge(data.currentBadge());
//            } catch (BadgeNotFoundException e) {
                this.currentBadge = null;
//            }
//            if (antiPhantom) SERVER.getAntiPhantom().add(uniqueId);
        });
        loaded.accept(this);
    }

    public CompletableFuture<UUID> quit() {
        if (isSitting()) unSit();
        if (nickName != null) SERVER.getNickNames().remove(Strings.stripColor(nickName.toLowerCase()));
        if (SERVER.getAntiPhantom().contains(uniqueId)) SERVER.getAntiPhantom().remove(uniqueId);
        if (SERVER.getSocialSpy().contains(uniqueId)) SERVER.getSocialSpy().remove(uniqueId);
        PlayerDatabase.a().clearHomes(uuidString).thenAccept((v) -> PlayerDatabase.a().setHomes(uuidString, homes));
        PlayerDatabase.a().clearKits(uuidString).thenAccept((v) -> PlayerDatabase.a().setKits(uuidString, usedKits));
        PlayerDatabase.a().clearCooldowns(uuidString).thenAccept((v) -> PlayerDatabase.a().setCooldowns(uuidString, commandCooldowns));
        PlayerDatabase.a().setLogoutData(uuidString, username == null ? "INVALID" : p.getName(), ipAddress == null ? "INVALID" : p.getAddress().getHostString(), p.getLocation());
        PlayerDatabase.a().setOtherData(uuidString, nickName == null ? "INVALID" : nickName, tpToggled, tpAuto, false, false, currentBadge == null ? "default" : currentBadge.name());
        return CompletableFuture.completedFuture(uniqueId);
    }

    public Player player() {
        return p;
    }

    public Location getLocation() {
        return p.getLocation();
    }

    public PlayerInventory getInventory() {
        return p.getInventory();
    }

    public Return openInventory(Inventory inv) {
        p.openInventory(inv);
        return Return.SUCCESS;
    }

    public void setTab(Component header, Component footer, Component name) {
        p.sendPlayerListHeaderAndFooter(header, footer);
        p.playerListName(name);
    }

    public void setTabFooter(Component footer) {
        p.sendPlayerListFooter(footer);
    }

    public void setTabHeader(Component header) {
        p.sendPlayerListHeader(header);
    }

    public void setTabName(Component name) {
        p.playerListName(name);
    }

    public boolean isOnline() {
        if (uniqueId == null || p == null) return false;
        return p.isOnline();
    }

    public Return setNickName(String nickName) {
        if (callEvent(new PlayerChangeNicknameEvent(this, nickName))) return Return.EVENT_CANCELLED;
        this.nickName = nickName;
        return Return.SUCCESS;
    }

    public String getNickName() {
        return nickName == null ? p.getName() : nickName;
    }

    public Return fly() {
        p.setAllowFlight(!p.getAllowFlight());
        p.setFlying(p.getAllowFlight());
        return p.getAllowFlight() ? Return.ENABLED : Return.DISABLED;
    }

    public Return toggleTP() {
        tpToggled = !tpToggled;
        return tpToggled ? Return.ENABLED : Return.DISABLED;
    }

    public Return toggleAntiPhantom() {
        if (SERVER.getAntiPhantom().contains(uniqueId)) SERVER.getAntiPhantom().remove(uniqueId);
        else SERVER.getAntiPhantom().add(uniqueId);
        return SERVER.getAntiPhantom().contains(uniqueId) ? Return.ENABLED : Return.DISABLED;
    }

    public Return toggleTPAuto() {
        tpAuto = !tpAuto;
        return tpAuto ? Return.ENABLED : Return.DISABLED;
    }

    public Return toggleGodMode() {
        final PersistentDataContainer container = p.getPersistentDataContainer();
        if (container.has(SERVER.getGodKey())) container.remove(SERVER.getGodKey());
        else container.set(SERVER.getGodKey(), DataTypes.BOOLEAN, true);
        return container.has(SERVER.getGodKey()) ? Return.ENABLED : Return.DISABLED;
//        if (SERVER.getGodMode().contains(uniqueId)) SERVER.getGodMode().remove(uniqueId);
//        else SERVER.getGodMode().add(uniqueId);
//        return SERVER.getGodMode().contains(uniqueId) ? Return.ENABLED : Return.DISABLED;
    }
    public Return togglePay() {
        setCanRecieveMoney(!canRecieveMoney());
        return canRecieveMoney() ? Return.DISABLED : Return.ENABLED;
    }

    public boolean hasPermission(String permission) {
        if (SERVER.getPermissions() == null)
            return p.hasPermission("*") || p.hasPermission("desticore.admin") || p.hasPermission(permission);
        User user = SERVER.getPermissions().getUserManager().getUser(p.getUniqueId());
        CachedPermissionData data = user.getCachedData().getPermissionData();
        return data.checkPermission("*").asBoolean() ||
                data.checkPermission("desticore.admin").asBoolean() || data.checkPermission(permission).asBoolean();
    }

    public boolean sendMessage(Component message) {
        if (message == null) return false;
        p.sendMessage(message);
        return true;
    }
    public boolean isOnGround() {
        return ((LivingEntity) p).isOnGround();
    }

    public void setWalkSpeed(float amount) {
        p.setWalkSpeed(amount / 10);
    }

    public void setFlySpeed(float amount) {
        p.setFlySpeed(amount / 10);
    }

    public void setWalkSpeed(String amount) {
        if (!isFloat(amount)) return;
        setWalkSpeed(Float.valueOf(amount));
    }

    public void setFlySpeed(String amount) {
        if (!isFloat(amount)) return;
        setFlySpeed(Float.valueOf(amount));
    }


    BukkitTask sitTask;
    ArmorStand seat;

    public Return sit(Location loc) {
        sendMessage(miniMessage().deserialize("<red>Sitting is now disabled due to a bug"));
        return Return.SUCCESS;
//        if (callEvent(new PlayerSitEvent(this, getLocation()))) return Return.EVENT_CANCELLED;
//        seat = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
//        seat.setInvulnerable(true);
//        seat.setGravity(false);
//        seat.addPassenger(p);
//        seat.setVisible(false);
//        seat.setSmall(true);
//        seat.setAI(false);
//        seat.getPersistentDataContainer().set(SERVER.getSeatKey(), PersistentDataType.STRING, "chair");
//        sitting = true;
//        sitTask = runRepeatingTask(20, () ->
//                seat.setRotation(p.getLocation().getYaw(), seat.getLocation().getPitch())
//        );
//        return Return.SUCCESS;
    }

    public Return unSit() {
        if (seat.getPersistentDataContainer().has(SERVER.getSeatKey(), PersistentDataType.STRING) &&
                seat.getPersistentDataContainer().get(SERVER.getSeatKey(), PersistentDataType.STRING).equalsIgnoreCase("chair")) {
            seat.removePassenger(p);
            p.teleport(getLocation().add(0, 1, 0));
            seat.remove();
            sitting = true;
            sitTask.cancel();
            p.teleport(getLocation().add(0, 1, 0));
        }
        return Return.SUCCESS;
    }

    public Return teleport(Location loc, TeleportReason reason, Consumer<Boolean> finished) {
        AtomicReference<Return> returnValue = new AtomicReference<>(Return.SUCCESS);
        getScheduler().runTask(PLUGIN, () -> {
            if (callEvent(new PlayerTeleportEvent(this, loc, reason)))
                returnValue.set(Return.EVENT_CANCELLED);
            else if (hasPermission("desticore.teleport.warmupbypass"))
                returnValue.set(teleportInstant(loc));
            else {
                new InClassListener(p, PLUGIN) {
                    BukkitTask task = runTaskLater(3 * 20, () -> {
                        p.teleportAsync(loc);
                        unregister();
                        finished.accept(true);
                    });

                    @EventHandler
                    public void onMove(PlayerMoveEvent e) {
                        if (e.getPlayer().getUniqueId() == uniqueId
                                && ((e.getFrom().getX() != e.getTo().getX())
                                || (e.getFrom().getZ() != e.getTo().getZ()))) {
                            task.cancel();
                            e.getPlayer().sendMessage(SERVER.tl("movedWhileTeleporting"));
                            unregister();
                            finished.accept(false);
                        }
                    }
                };
            }
        });
        return returnValue.get();
    }


    public Return teleportInstant(Location loc) {
        p.teleportAsync(loc);
        return Return.SUCCESS;
    }

    public Return warp(String name) {
        try {
            return teleport(SERVER.getWarp(name).location(), TeleportReason.WARP, (b) -> {});
        } catch(WarpNotFoundException e) {
            return Return.NO_NAME;
        }
    }

    public Return warp(Warp warp) {
        return warp(warp.name());
    }

    public Return back() {
        if (backLocation == null) return Return.NONE;
        return teleport(backLocation.clone(), TeleportReason.BACK, b -> backLocation = b ? null : backLocation);
    }

    //////////////////////////////////////////////////
    //                                              //
    //             Home Methods Start               //
    //                                              //
    //////////////////////////////////////////////////

    public Location getHomeLocation(String name) {
        return getHome(name).location();
    }

    public Return teleportHome(String name) {
        if (!hasHome(name)) return Return.NO_NAME;
        return teleport(getHomeLocation(name), TeleportReason.HOME, (b) -> {
        });
    }

    public Home getHome(String name) {
        Optional<Home> home = homes.stream().filter(h -> h.name().equalsIgnoreCase(name)).findFirst();
        return home.isEmpty() ? null : home.get();
    }

    public Return setHome(String name) {
        if (!homes.isEmpty() && homes.size() >= getMaxHomes()) return Return.MAX;
        if (hasHome(name)) return Return.HAS_NAME;
        Home home = new Home(name, p.getLocation());
        if (callEvent(new PlayerHomeSetEvent(this, home))) return Return.EVENT_CANCELLED;
        if (homes.contains(home)) return Return.HAS_NAME;
        homes.add(home);
        return Return.SUCCESS;
    }

    public Return delHome(String name) {
        if (!hasHome(name)) return Return.NO_NAME;
        Home home = getHome(name);
        if (callEvent(new PlayerHomeDeleteEvent(this, home))) return Return.EVENT_CANCELLED;
        homes.remove(home);
        deletedHomes.add(home);
        return Return.SUCCESS;
    }

    public int getMaxHomes() {
        int max = SERVER.getConfig().getInt("SetHome-Multiple.default");
        if (hasPermission("desticore.admin") || hasPermission("*")) return 10000;
        Set<String> multipleList = SERVER.getConfig().getConfigurationSection("SetHome-Multiple").getKeys(false);
        if (multipleList != null) {
            for (String multiple : multipleList) {
                if (hasPermission("desticore.sethome.multiple." + multiple)) {
                    max = Math.max(max, SERVER.getConfig().getInt("SetHome-Multiple." + multiple));
                }
            }
        }
        return max;
    }

    public boolean hasHome(String name) {
        Optional<Home> home = homes.stream().filter(h -> h.name().equalsIgnoreCase(name)).findFirst();
        return !home.isEmpty();
    }

    public ArrayList<String> getHomesAsNames() {
        ArrayList<String> names = Lists.newArrayList();
        homes.forEach(home -> names.add(home.name()));
        return names;
    }

    //////////////////////////////////////////////////
    //                                              //
    //             Home Methods End                 //
    //                                              //
    //////////////////////////////////////////////////


    //////////////////////////////////////////////////
    //                                              //
    //            Kit Methods Start                 //
    //                                              //
    //////////////////////////////////////////////////

    public Return useKit(String name) {
        try {
            Kit kit = SERVER.getKit(name);
            if (!canUseKit(name)) return Return.CANT_USE;
            if (!hasPermission("desticore.kit." + name)) return Return.NO_PERMISSIONS;
            if (callEvent(new PlayerKitClaimEvent(this, kit))) return Return.EVENT_CANCELLED;
            usedKits.put(kit, LocalDateTime.now());
            for (ItemStack item : kit.items()) {
                if (item == null || item.getType() == Material.AIR || item.getType() == null) continue;
                if (p.getInventory().firstEmpty() != -1) p.getInventory().addItem(item);
                else p.getWorld().dropItem(p.getLocation(), item);
            }
            return Return.SUCCESS;
        } catch (KitNotFoundException e) {
            return Return.DOESNT_EXIST;
        }
    }

    public boolean canUseKit(String name) {
        try {
            Kit kit = SERVER.getKit(name);
            if (!usedKits.containsKey(kit)) return true;
            LocalDateTime date = usedKits.get(kit);
            return ChronoUnit.SECONDS.between(date, LocalDateTime.now()) >= kit.cooldown();
        } catch (KitNotFoundException e) {
            return false;
        }
    }

    public long getKitCooldownLeft(String name) {
        try {
            Kit kit = SERVER.getKit(name);
            LocalDateTime date = usedKits.get(kit);
            return (kit.cooldown() - ChronoUnit.SECONDS.between(date, LocalDateTime.now()));
        } catch (KitNotFoundException e) {
            return 0;
        }
    }

    public String getKitCooldownLeftFormatted(String name) {
        try {
            Kit kit = SERVER.getKit(name);
            if (!usedKits.containsKey(kit)) return "null";
            return formatTime(usedKits.get(kit));
        } catch (KitNotFoundException e) {
            return e.getMessage();
        }
    }


    public Return useCooldown(String name) {
        try {
            if (!canUseCooldown(name)) return Return.CANT_USE;
            CommandCooldown coolDown = SERVER.getCooldown(name);
            commandCooldowns.put(coolDown, LocalDateTime.now());
            return Return.SUCCESS;
        } catch (CooldownNotFoundException e) {
            return Return.DOESNT_EXIST;
        }
    }

    public boolean canUseCooldown(String name) {
        try {
            if (hasPermission("desticore.cooldown.bypass")) return true;
            CommandCooldown coolDown = SERVER.getCooldown(name);
            if (!commandCooldowns.containsKey(coolDown)) return true;
            LocalDateTime date = commandCooldowns.get(coolDown);
            return ChronoUnit.SECONDS.between(date, LocalDateTime.now()) >= coolDown.cooldown();
        } catch (CooldownNotFoundException e) {
            return false;
        }
    }

    public long getCommandCooldownLeft(String name) {
        try {
            CommandCooldown coolDown = SERVER.getCooldown(name);
            LocalDateTime date = commandCooldowns.get(coolDown);
            return (coolDown.cooldown() - ChronoUnit.SECONDS.between(date, LocalDateTime.now()));
        } catch (CooldownNotFoundException e) {
            return 0;
        }
    }

    //////////////////////////////////////////////////
    //                                              //
    //              Kit Methods End                 //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //              Eco Methods Start               //
    //                                              //
    //////////////////////////////////////////////////

    private void setCanRecieveMoney(boolean canRecieve) {
        SERVER.getAccount(p).setReceive(canRecieve);
    }

    public boolean canRecieveMoney() {
        return SERVER.getAccount(p).canReceive().get();
    }

    public EconomyResponse depositMoney(double amount) {
        return SERVER.getVaultHook().depositPlayer(p, amount);
    }

    public EconomyResponse withdrawMoney(double amount) {
        return SERVER.getVaultHook().withdrawPlayer(p, amount);
    }
    public double getBalance() {
        return SERVER.getVaultHook().getBalance(p);
    }

    //////////////////////////////////////////////////
    //                                              //
    //              Eco Methods End                 //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //           Message Methods Start              //
    //                                              //
    //////////////////////////////////////////////////

    DestiPlayer previousMsgPlayer = null;

    public Return message(OfflinePlayer player, String message, Consumer<DestiPlayer> otherPlayer) {
        if (player == null || !player.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (player.getUniqueId() == uniqueId) return Return.CANT_BE_YOURSELF;
        DestiPlayer tempPlayer = SERVER.getPlayer(player.getUniqueId());
        if (previousMsgPlayer == null || previousMsgPlayer.getUniqueId() != tempPlayer.getUniqueId())
            previousMsgPlayer = tempPlayer;
        if (tempPlayer.previousMsgPlayer == null || tempPlayer.previousMsgPlayer.getUniqueId() != uniqueId)
            tempPlayer.previousMsgPlayer = this;
        if (callEvent(new PlayerMessageSendEvent(this, tempPlayer, message))) return Return.EVENT_CANCELLED;
        otherPlayer.accept(tempPlayer);
        return Return.SUCCESS;
    }

    public Return reply(String message, Consumer<DestiPlayer> otherPlayer) {
        if (previousMsgPlayer == null || !previousMsgPlayer.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (callEvent(new PlayerMessageSendEvent(this, previousMsgPlayer, message))) return Return.EVENT_CANCELLED;
        otherPlayer.accept(previousMsgPlayer);
        return Return.SUCCESS;
    }

    //////////////////////////////////////////////////
    //                                              //
    //           Message Methods End                //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //      Teleport Requests Methods Start         //
    //                                              //
    //////////////////////////////////////////////////

    BukkitTask teleportRequestTimer;

    public void startTeleportRequestTimer() {
        teleportRequestTimer = runTaskLater(120 * 20, () -> {
            if (currentTeleportRequest == null) return;
            DestiPlayer tempPlayer = (currentTeleportRequest.sender().getUniqueId() == uniqueId)
                    ? currentTeleportRequest.reciever() : currentTeleportRequest.sender();
            if (tempPlayer.getCurrentTeleportRequest() == null) return;
            if (tempPlayer.getCurrentTeleportRequest().reciever().getUniqueId() == uniqueId
                    || tempPlayer.getCurrentTeleportRequest().sender().getUniqueId() == uniqueId)
                tempPlayer.clearTeleportRequest();
            sendMessage(Component.text("Teleport request cancelled due to time running out"));
            clearTeleportRequest();
        });
    }

    public Return sendTeleportRequest(OfflinePlayer player, RequestType type) {
        if (!player.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (player.getUniqueId() == uniqueId) return Return.CANT_BE_YOURSELF;
        if (currentTeleportRequest != null) return Return.CANT_USE;
        DestiPlayer tempPlayer = SERVER.getPlayer(player.getUniqueId());
        if (!tempPlayer.isTpToggled()) return Return.NOT_ALLOWED;
        currentTeleportRequest = new TeleportRequest(this, tempPlayer, type);
        if (callEvent(new PlayerTeleportRequestEvent(this, currentTeleportRequest))) return Return.EVENT_CANCELLED;
        tempPlayer.setCurrentTeleportRequest(currentTeleportRequest);
        startTeleportRequestTimer();
        return Return.SUCCESS;
    }

    public Return acceptTeleportRequest(Consumer<DestiPlayer> otherPlayer) {
        if (currentTeleportRequest == null
                || currentTeleportRequest.sender().getUniqueId() == uniqueId) return Return.NULL;
        if (!currentTeleportRequest.sender().isOnline()) return Return.PLAYER_NOT_ONLINE;
        DestiPlayer tempPlayer = currentTeleportRequest.sender();
        if (callEvent(new PlayerTeleportRequestAcceptEvent(this, currentTeleportRequest)))
            return Return.EVENT_CANCELLED;
        if (currentTeleportRequest.type() == RequestType.TO)
            tempPlayer.teleport(p.getLocation(), TeleportReason.PLAYER_TELEPORT_REQUEST, (b) -> {
            });
        if (currentTeleportRequest.type() == RequestType.HERE)
            teleport(tempPlayer.getLocation(), TeleportReason.PLAYER_TELEPORT_REQUEST, (b) -> {
            });
        clearTeleportRequest();
        if (tempPlayer.getCurrentTeleportRequest().reciever().getUniqueId() == uniqueId)
            tempPlayer.clearTeleportRequest();
        tempPlayer.teleportRequestTimer.cancel();
        otherPlayer.accept(tempPlayer);
        return Return.SUCCESS;
    }

    public Return declineTeleportRequest(Consumer<DestiPlayer> otherPlayer) {
        if (currentTeleportRequest == null
                || currentTeleportRequest.sender().getUniqueId() == uniqueId) return Return.NULL;
        DestiPlayer tempPlayer = currentTeleportRequest.sender();
        if (callEvent(new PlayerTeleportRequestDeclineEvent(this, currentTeleportRequest)))
            return Return.EVENT_CANCELLED;
        clearTeleportRequest();
        if (tempPlayer.getCurrentTeleportRequest().reciever().getUniqueId() == uniqueId)
            tempPlayer.clearTeleportRequest();
        tempPlayer.teleportRequestTimer.cancel();
        otherPlayer.accept(tempPlayer);
        return Return.SUCCESS;
    }

    public Return cancelTeleportRequest(Consumer<DestiPlayer> otherPlayer) {
        if (currentTeleportRequest == null
                || currentTeleportRequest.reciever().getUniqueId() == uniqueId) return Return.NULL;
        DestiPlayer tempPlayer = currentTeleportRequest.reciever();
        if (callEvent(new PlayerTeleportRequestCancelEvent(this, currentTeleportRequest)))
            return Return.EVENT_CANCELLED;
        clearTeleportRequest();
        if (tempPlayer.getCurrentTeleportRequest().sender().getUniqueId() == uniqueId)
            tempPlayer.clearTeleportRequest();
        teleportRequestTimer.cancel();
        otherPlayer.accept(tempPlayer);
        return Return.SUCCESS;
    }

    public void clearTeleportRequest() {
        currentTeleportRequest = null;
    }


    //////////////////////////////////////////////////
    //                                              //
    //       Teleport Requests Methods End          //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //               Pay Methods Start              //
    //                                              //
    //////////////////////////////////////////////////

    public Return pay(Player pl, double amount) {
        if (!pl.isOnline()) return Return.PLAYER_NOT_ONLINE;
        if (pl.getUniqueId() == uniqueId) return Return.CANT_BE_YOURSELF;
        DestiPlayer player = SERVER.getPlayer(pl);
        if (!player.canRecieveMoney()) return Return.NOT_ALLOWED;
        if (getBalance() < amount) return Return.MAX;
        if (callEvent(new PlayerPayPlayerEvent(this, player, amount))) return Return.EVENT_CANCELLED;
        withdrawMoney(amount);
        player.depositMoney(amount);
        return Return.SUCCESS;
    }

    //////////////////////////////////////////////////
    //                                              //
    //               Pay Methods End                //
    //                                              //
    //////////////////////////////////////////////////

    //////////////////////////////////////////////////
    //                                              //
    //             Private Methods Start            //
    //                                              //
    //////////////////////////////////////////////////

    private boolean callEvent(DuckPlayerEvent event) {
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) return true;
        return false;
    }

    private String formatTime(LocalDateTime time) {
        StringBuilder builder = new StringBuilder();
        long uptime = (ChronoUnit.SECONDS.between(time, LocalDateTime.now())) * 1000;
        builder.append((TimeUnit.MILLISECONDS.toDays(uptime) > 0) ? TimeUnit.MILLISECONDS.toDays(uptime) + "D " : "");
        uptime -= TimeUnit.DAYS.toMillis(TimeUnit.MILLISECONDS.toDays(uptime));
        builder.append((TimeUnit.MILLISECONDS.toHours(uptime) > 0) ? TimeUnit.MILLISECONDS.toHours(uptime) + "H " : "");
        uptime -= TimeUnit.HOURS.toMillis(TimeUnit.MILLISECONDS.toHours(uptime));
        builder.append((TimeUnit.MILLISECONDS.toMinutes(uptime) > 0) ? TimeUnit.MILLISECONDS.toMinutes(uptime) + "M " : "");
        uptime -= TimeUnit.MINUTES.toMillis(TimeUnit.MILLISECONDS.toMinutes(uptime));
        builder.append((TimeUnit.MILLISECONDS.toSeconds(uptime) > 0) ? TimeUnit.MILLISECONDS.toSeconds(uptime) + "S " : "");
        return builder.toString();
    }

    private BukkitTask runTaskLater(int time, Runnable runnable) {
        return getScheduler().runTaskLater(SERVER.getPlugin(), runnable, time);
    }


}
