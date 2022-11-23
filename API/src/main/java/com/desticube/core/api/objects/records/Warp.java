package com.desticube.core.api.objects.records;

import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public record Warp(String name, Location location, ItemStack displayItem) {
}
