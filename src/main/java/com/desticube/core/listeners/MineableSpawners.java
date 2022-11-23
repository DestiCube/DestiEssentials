package com.desticube.core.listeners;

import com.desticube.core.listeners.handlers.DestiListener;
import com.gamerduck.commons.general.Strings;
import com.gamerduck.commons.items.DuckItem;
import com.gamerduck.commons.listeners.DuckListener;
import com.gamerduck.commons.persistent.DataTypes;
import com.google.common.collect.Lists;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;

import static com.gamerduck.commons.general.Components.translate;

@DuckListener
public class MineableSpawners extends DestiListener {

    final Component format;
    final List<Component> loreFormat = Lists.newArrayList();

    public MineableSpawners() {
        format = translate(PLUGIN.getConfig().getString("MineableSpawners.SpawnerFormat"));
        loreFormat.addAll(translate(PLUGIN.getConfig().getStringList("MineableSpawners.SpawnerLoreFormat")));
    }

    @EventHandler
    public void onSpawnerMine(BlockBreakEvent e) {
        if (e.getBlock().getType() == Material.SPAWNER) {
            final Player p = e.getPlayer();
            final Block block = e.getBlock();
            if ((p.hasPermission("desticore.silkspawner") && p.getInventory().getItemInMainHand().containsEnchantment(Enchantment.SILK_TOUCH))
                    || p.hasPermission("desticore.silkspawner.bypass")) {
                CreatureSpawner s = (CreatureSpawner) block.getState();
                DuckItem spawner = new DuckItem()
                        .withMaterial(Material.SPAWNER)
                        .withDisplayName(format.replaceText(b ->b.match("%spawner%").replacement(translateName(s.getSpawnedType().toString()))))
                        .withLore(loreFormat);
                spawner.editMeta(meta ->
                        meta.getPersistentDataContainer().set(SPAWNER_KEY, DataTypes.ENTITY_TYPE, s.getSpawnedType()));
                block.getWorld().dropItemNaturally(block.getLocation(), spawner);
                p.sendMessage(tl("SilktouchBroken", translateName(s.getSpawnedType().toString())));
            } else
                p.sendMessage(tl("SpawnerBroken", translateName(((CreatureSpawner) block.getState()).getSpawnedType().toString())));
        }
    }

    private String translateName(String name) {
        return Strings.capitalizeEach(name.toLowerCase().replaceAll("_", " "));
    }

    @EventHandler
    public void onSpawnerPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().getType() == Material.SPAWNER) {
            Block block = e.getBlock();
            PersistentDataContainer container = e.getItemInHand().getItemMeta().getPersistentDataContainer();
            if (container.has(SPAWNER_KEY, DataTypes.ENTITY_TYPE)) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(container.get(SPAWNER_KEY, DataTypes.ENTITY_TYPE));
            } else if (container.has(SPAWNER_KEY, PersistentDataType.STRING)) {
                CreatureSpawner spawner = (CreatureSpawner) block.getState();
                spawner.setSpawnedType(EntityType.valueOf(container.get(SPAWNER_KEY, PersistentDataType.STRING)));
            }
        }
    }
}
