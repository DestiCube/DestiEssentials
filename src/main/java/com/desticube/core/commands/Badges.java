package com.desticube.core.commands;//package com.desticube.core.com.desticube.core.commands;
//
//import com.desticube.core.api.objects.DestiPlayer;
//import com.desticube.core.api.objects.records.Badge;
//import com.desticube.core.com.desticube.core.commands.handlers.DestiCommand;
//import com.gamerduck.commons.com.desticube.core.commands.DuckCommand;
//import com.gamerduck.commons.inventory.DuckInventory;
//import com.gamerduck.commons.items.DuckItem;
//import org.bukkit.Material;
//import org.bukkit.command.Command;
//import org.bukkit.command.CommandSender;
//import org.bukkit.enchantments.Enchantment;
//import org.bukkit.entity.Player;
//import org.bukkit.inventory.ItemFlag;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import static com.gamerduck.commons.general.Components.translate;
//import static net.kyori.adventure.text.Component.space;
//
//@DuckCommand(
//        command = "badges",
//        description = "Change your badges",
//        aliases = {},
//        usageARGS = "",
//        permissions = {})
//public class Badges extends DestiCommand {
//    @Override
//    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
//        DestiPlayer p = player(((Player) sender));
//        openGUI(p);
//        return false;
//    }
//
//    private void openGUI(DestiPlayer p) {
//        List<Badge> badges = SERVER.getBadges().stream().filter(b -> p.hasPermission(b.permission())).collect(Collectors.toList());
//        DuckInventory inv = new DuckInventory(PLUGIN, 54, color("&e&lBadges"))
//                .fillBorder(new DuckItem().withMaterial(Material.GRAY_STAINED_GLASS_PANE).withDisplayName(" "));
//        for (Badge badge : badges) {
//            inv.addButton(new DuckItem()
//                            .withMaterial(Material.NAME_TAG)
//                            .withDisplayName(translate("<white>" + badge.name()))
//                            .addToLore(translate("<gray>This badge will be displayed"), translate("<gray>next to your rank in game"),
//                                    translate(" <gray>» " + badge.description()), space(), translate("<gray>Click to set as your current badge!"))
//                            .withFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS)
//                            .withEnchant(Enchantment.SILK_TOUCH, 1),
//                    (event) -> {
//                        p.setCurrentBadge(SERVER.getBadge(badge.name()));
//                        p.player().closeInventory();
//                        p.sendMessage(tl("badgeSet", badge.name()));
//                    });
//        }
//        inv.open(p.player());
//    }
//}
