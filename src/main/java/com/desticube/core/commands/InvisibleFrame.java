package com.desticube.core.commands;

import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.commands.handlers.DestiCommand;
import com.gamerduck.commons.commands.DuckCommand;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import static net.kyori.adventure.text.Component.text;

@DuckCommand(
        command = "invisibleframe",
        description = "Make the itemframe you are looking at invisible",
        aliases = {"if", "invisframe"},
        usageARGS = "",
        permissions = {"desticore.invisibleframe"})
public class InvisibleFrame extends DestiCommand {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        DestiPlayer p = player(((Player) sender));
        if (!p.hasPermission("desticore.invisibleframe")) return p.sendMessage(NO_PERMISSIONS);
        boolean foundFrame = false;
        for (Entity ent : p.player().getNearbyEntities(10, 10, 10)) {
            if (ent instanceof ItemFrame) {
                ItemFrame frame = (ItemFrame) ent;
                if (getLookingAt(p.player(), ent)) {
                    if (frame.isVisible()) {
                        p.sendMessage(text("Itemframe is now invisible"));
                        frame.setVisible(false);
                    } else {
                        p.sendMessage(text("Itemframe is now visible"));
                        frame.setVisible(true);
                    }
                    foundFrame = true;
                    break;
                }
            }
        }
        if (!foundFrame) return p.sendMessage(text("You are not looking at an item frame"));
        return false;
    }

    private boolean getLookingAt(Player player, Entity livingEntity) {
        Location eye = player.getEyeLocation();
        Vector toEntity = livingEntity.getLocation().toVector().subtract(eye.toVector());
        double dot = toEntity.normalize().dot(eye.getDirection());
        return dot > 0.99D;
    }

}