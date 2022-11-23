package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.inclass.InClassListener;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.bukkit.util.StringUtil.copyPartialMatches;

@DuckCommand(
        command = "Recipe",
        description = "Find a recipe",
        aliases = {},
        usageARGS = "",
        permissions = {})
public class Recipe extends DestiCommand {

    Listener listen;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (args.length == 0) return p.sendMessage(USAGE);
        else {
            p.sendMessage(tl("recipe", args[0]));
            ItemStack item = new ItemStack(Material.matchMaterial(args[0]));
            List<org.bukkit.inventory.Recipe> recipes = Bukkit.getRecipesFor(item);
            Inventory recipeInv = Bukkit.createInventory(null, InventoryType.WORKBENCH);
            if (recipes.size() < 1) return p.sendMessage(tl("noRecipe", args[0]));
            org.bukkit.inventory.Recipe recipe = recipes.get(0);
            p.openInventory(recipeInv);
            if (recipe instanceof ShapedRecipe) {
                shapedRecipe(p.player(), (ShapedRecipe) recipe);
            } else if (recipe instanceof ShapelessRecipe) {
                shapelessRecipe(p.player(), (ShapelessRecipe) recipe);
            }
            new InClassListener(p.player(), PLUGIN) {
                @EventHandler
                public void onClose(InventoryCloseEvent e) {
                    if (e.getPlayer().getUniqueId() == p.getUniqueId()) unregister();
                }

                @EventHandler
                public void onClick(InventoryClickEvent e) {
                    if (e.getWhoClicked().getUniqueId() == p.getUniqueId()) e.setCancelled(true);
                }
            };
        }
        return false;
    }

    private void shapedRecipe(Player p, ShapedRecipe recipe) {
        final String[] recipeShape = recipe.getShape();
        final Map<Character, ItemStack> ingredientMap = recipe.getIngredientMap();
        p.getOpenInventory().getTopInventory().setItem(0, recipe.getResult());
        for (int j = 0; j < recipeShape.length; j++) {
            for (int k = 0; k < recipeShape[j].length(); k++) {
                final ItemStack item = ingredientMap.get(recipeShape[j].toCharArray()[k]);
                if (item == null) continue;
                p.getOpenInventory().getTopInventory().setItem(j * 3 + k + 1, item);
            }
        }
    }

    private void shapelessRecipe(Player p, ShapelessRecipe recipe) {
        final List<ItemStack> ingredients = recipe.getIngredientList();
        p.getOpenInventory().getTopInventory().setItem(0, recipe.getResult());
        for (int i = 0; i < ingredients.size(); i++) {
            final ItemStack item = ingredients.get(i);
            p.getOpenInventory().getTopInventory().setItem(i + 1, item);
        }
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label,
                                      String[] args) {
        if (args.length == 1) {
            ArrayList<String> materials = Lists.newArrayList();
            for (Material mat : Material.values()) materials.add(mat.toString().toLowerCase());
            ArrayList<String> newMaterials = Lists.newArrayList();
            copyPartialMatches(args[0], materials, newMaterials);
            return newMaterials;
        } else {
            return Collections.emptyList();
        }
    }
}
	