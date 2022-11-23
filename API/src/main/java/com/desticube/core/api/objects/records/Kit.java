package com.desticube.core.api.objects.records;

import org.bukkit.inventory.ItemStack;

public record Kit(String name, Integer invSlot, ItemStack[] items, Long cooldown) {
}
