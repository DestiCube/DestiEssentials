package com.desticube.core.listeners;

import com.desticube.core.listeners.handlers.DestiListener;
import com.gamerduck.commons.listeners.DuckListener;
import org.bukkit.Material;
import org.bukkit.block.Dispenser;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

@DuckListener
public class DeathMessages extends DestiListener {

    public DeathMessages() {
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        DamageCause cause = e.getEntity().getLastDamageCause().getCause();
        switch (cause) {
            case ENTITY_ATTACK -> {
                Entity damager = ((EntityDamageByEntityEvent) e.getEntity().getLastDamageCause()).getDamager();
                switch (damager.getType()) {
                    case ZOMBIE -> e.deathMessage((tl("killedByZombie", e.getEntity().getName())));
                    case SKELETON -> e.deathMessage((tl("killedBySkeleton", e.getEntity().getName())));
                    case SPIDER -> e.deathMessage((tl("killedBySpider", e.getEntity().getName())));
                    case ENDERMAN -> e.deathMessage((tl("killedByEnderman", e.getEntity().getName())));
                    case ENDER_DRAGON ->
                            e.deathMessage((tl("killedByEnderDragon", e.getEntity().getName())));
                    case CAVE_SPIDER ->
                            e.deathMessage((tl("killedByCaveSpider", e.getEntity().getName())));
                    case ZOMBIFIED_PIGLIN ->
                            e.deathMessage((tl("killedByZombifiedPiglin", e.getEntity().getName())));
                    case EVOKER -> e.deathMessage((tl("killedByEvoker", e.getEntity().getName())));
                    case VEX -> e.deathMessage((tl("killedByVex", e.getEntity().getName())));
                    case PILLAGER -> e.deathMessage((tl("killedByPillager", e.getEntity().getName())));
                    case VINDICATOR ->
                            e.deathMessage((tl("killedByVindicator", e.getEntity().getName())));
                    case ENDERMITE -> e.deathMessage((tl("killedByEndermite", e.getEntity().getName())));
                    case GUARDIAN -> e.deathMessage((tl("killedByGuardian", e.getEntity().getName())));
                    case ELDER_GUARDIAN ->
                            e.deathMessage((tl("killedByElderGuardian", e.getEntity().getName())));
                    case SHULKER -> e.deathMessage((tl("killedByShulker", e.getEntity().getName())));
                    case HUSK -> e.deathMessage((tl("killedByHusk", e.getEntity().getName())));
                    case STRAY -> e.deathMessage((tl("killedByStray", e.getEntity().getName())));
                    case PHANTOM -> e.deathMessage((tl("killedByPhantom", e.getEntity().getName())));
                    case BLAZE -> e.deathMessage((tl("killedByBlaze", e.getEntity().getName())));
                    case GHAST -> e.deathMessage((tl("killedByGhast", e.getEntity().getName())));
                    case MAGMA_CUBE -> e.deathMessage((tl("killedByMagmaCube", e.getEntity().getName())));
                    case SILVERFISH ->
                            e.deathMessage((tl("killedBySilverFish", e.getEntity().getName())));
                    case SLIME -> e.deathMessage((tl("killedBySlime", e.getEntity().getName())));
                    case ZOMBIE_VILLAGER ->
                            e.deathMessage((tl("killedByZombieVillager", e.getEntity().getName())));
                    case DROWNED -> e.deathMessage((tl("killedByDrowned", e.getEntity().getName())));
                    case WITHER_SKELETON ->
                            e.deathMessage((tl("killedByWitherSkeleton", e.getEntity().getName())));
                    case WITCH -> e.deathMessage((tl("killedByWitch", e.getEntity().getName())));
                    case HOGLIN -> e.deathMessage((tl("killedByHoglin", e.getEntity().getName())));
                    case ZOGLIN -> e.deathMessage((tl("killedByZoglin", e.getEntity().getName())));
                    case PIGLIN_BRUTE ->
                            e.deathMessage((tl("killedByPiglinBrute", e.getEntity().getName())));
                    case BEE -> e.deathMessage((tl("killedByBee", e.getEntity().getName())));
                    case AREA_EFFECT_CLOUD ->
                            e.deathMessage((tl("killedByAreaEffectCloud", e.getEntity().getName())));
                    case ENDER_PEARL -> e.deathMessage((tl("killedByFall", e.getEntity().getName())));
                    case ENDER_CRYSTAL ->
                            e.deathMessage((tl("killedByEnderCrystal", e.getEntity().getName())));
                    case EVOKER_FANGS -> e.deathMessage((tl("killedByEvoker", e.getEntity().getName())));
                    case MINECART_TNT -> e.deathMessage((tl("killedByTNT", e.getEntity().getName())));
                    case PIGLIN -> e.deathMessage((tl("killedByPiglin", e.getEntity().getName())));
                    case LLAMA -> e.deathMessage((tl("killedByLlama", e.getEntity().getName())));
                    case RAVAGER -> e.deathMessage((tl("killedByRavager", e.getEntity().getName())));
                    case WOLF -> e.deathMessage((tl("killedByWolf", e.getEntity().getName())));
                    case WITHER -> e.deathMessage((tl("killedByTheWither", e.getEntity().getName())));
                    case PLAYER -> e.deathMessage((tl("killedByPlayer", e.getEntity().getName())));
                    case CREEPER -> e.deathMessage((tl("killedByCreeper", e.getEntity().getName())));
                    default ->
                            e.deathMessage((tl("killedByUnknown", e.getEntity().getName(), damager.getName())));
                }
            }
            case CONTACT -> {
                boolean cactus = false;
                for (int x = -1; x <= 1; x++) {
                    for (int y = -1; y <= 1; y++) {
                        for (int z = -1; z <= 1; z++) {
                            if (e.getEntity().getLocation().add(x, y, z).getBlock().getType() == Material.CACTUS) {
                                cactus = true;
                                break;
                            }
                        }
                    }
                }
                if (cactus) e.deathMessage((tl("killedByCactus", e.getEntity().getName())));
                else e.deathMessage((tl("killedByDripstone", e.getEntity().getName())));
            }
            case DROWNING -> e.deathMessage((tl("killedByDrowning", e.getEntity().getName())));
            case DRAGON_BREATH -> e.deathMessage((tl("killedByEnderDragon", e.getEntity().getName())));
            case BLOCK_EXPLOSION -> e.deathMessage((tl("killedByTNT", e.getEntity().getName())));
            case CRAMMING -> e.deathMessage((tl("killedByCramming", e.getEntity().getName())));
            case ENTITY_EXPLOSION -> e.deathMessage((tl("killedByTNT", e.getEntity().getName())));
            case ENTITY_SWEEP_ATTACK -> e.deathMessage((tl("killedByPlayer", e.getEntity().getName())));
            case FALL -> e.deathMessage((tl("killedByFall", e.getEntity().getName())));
            case FALLING_BLOCK -> e.deathMessage((tl("killedByFallingBlock", e.getEntity().getName())));
            case FIRE -> e.deathMessage((tl("killedByFire", e.getEntity().getName())));
            case FIRE_TICK -> e.deathMessage((tl("killedByFire", e.getEntity().getName())));
            case FLY_INTO_WALL -> e.deathMessage((tl("killedByFlyingIntoWall", e.getEntity().getName())));
            case FREEZE -> e.deathMessage((tl("killedByFreeze", e.getEntity().getName())));
            case HOT_FLOOR -> e.deathMessage((tl("killedByMagma", e.getEntity().getName())));
            case LAVA -> e.deathMessage((tl("killedByLava", e.getEntity().getName())));
            case LIGHTNING -> e.deathMessage((tl("killedByLightning", e.getEntity().getName())));
            case MAGIC -> e.deathMessage((tl("killedByMagic", e.getEntity().getName())));
            case POISON -> e.deathMessage((tl("killedByPosion", e.getEntity().getName())));
            case STARVATION -> e.deathMessage((tl("killedByStarvation", e.getEntity().getName())));
            case SUFFOCATION -> e.deathMessage((tl("killedBySuffocation", e.getEntity().getName())));
            case SUICIDE -> e.deathMessage((tl("killedBySuicide", e.getEntity().getName())));
            case THORNS -> e.deathMessage((tl("killedByThorns", e.getEntity().getName())));
            case VOID -> e.deathMessage((tl("killedByVoid", e.getEntity().getName())));
            case WITHER -> e.deathMessage((tl("killedByWither", e.getEntity().getName())));
            case PROJECTILE -> {
                Entity damager = ((EntityDamageByEntityEvent) e.getEntity().getLastDamageCause()).getDamager();
                switch (damager.getType()) {
                    case FIREBALL -> {
                        Fireball fireBall = (Fireball) damager;
                        if (fireBall.getShooter() instanceof Ghast)
                            e.deathMessage((tl("killedByGhast", e.getEntity().getName())));
                        else if (fireBall.getShooter() instanceof Player)
                            e.deathMessage((tl("killedByPlayer", e.getEntity().getName(), ((Player) fireBall.getShooter()).getName())));
                        else if (fireBall.getShooter() instanceof Dispenser)
                            e.deathMessage((tl("killedByUnknown", e.getEntity().getName())));
                    }
                    case ARROW -> {
                        Arrow arrow = (Arrow) damager;
                        if (arrow.getShooter() instanceof Skeleton)
                            e.deathMessage((tl("killedBySkeleton", e.getEntity().getName())));
                        else if (arrow.getShooter() instanceof Player)
                            e.deathMessage((tl("killedByPlayer", e.getEntity().getName())));
                        else if (arrow.getShooter() instanceof Pillager)
                            e.deathMessage((tl("killedByPillager", e.getEntity().getName())));
                        else if (arrow.getShooter() instanceof Dispenser)
                            e.deathMessage((tl("killedByUnknown", e.getEntity().getName())));
                    }
                    case WITHER_SKULL -> {
                        if (((WitherSkull) damager).getShooter() instanceof Wither)
                            e.deathMessage((tl("killedByTheWither", e.getEntity().getName())));
                    }
                    case LLAMA_SPIT -> {
                        if (((LlamaSpit) damager).getShooter() instanceof Llama)
                            e.deathMessage((tl("killedByLlama", e.getEntity().getName())));
                    }
                    case SMALL_FIREBALL -> {
                        SmallFireball ball = (SmallFireball) damager;
                        if (ball.getShooter() instanceof Blaze)
                            e.deathMessage((tl("killedByBlaze", e.getEntity().getName())));
                        else if (ball.getShooter() instanceof Dispenser)
                            e.deathMessage((tl("killedByUnkown", e.getEntity().getName())));
                    }
                    default -> e.deathMessage((tl("killedByUnkown", e.getEntity().getName())));
                }
            }
            default -> e.deathMessage((tl("killedByUnkown", e.getEntity().getName())));
        }
//		if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) 
//			e.deathMessage((e.getDeathMessage().replaceAll("{0}", e.getEntity().getName())
//					.replaceAll("{1}", ((EntityDamageByEntityEvent) e.getEntity().getLastDamageCause()).getDamager().getName())));
//		else e.deathMessage((e.getDeathMessage().replaceAll("{0}", e.getEntity().getName())));
    }
}
