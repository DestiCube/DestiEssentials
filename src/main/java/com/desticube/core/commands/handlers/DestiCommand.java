package com.desticube.core.commands.handlers;

import com.desticube.core.CoreMain;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.DestiServer;
import com.gamerduck.commons.commands.AbstractDuckCommand;
import com.gamerduck.commons.commands.DuckCommand;
import net.kyori.adventure.text.Component;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class DestiCommand extends AbstractDuckCommand {
    protected final DestiServer SERVER = CoreMain.getInstance().getDestiServer();
    protected Plugin PLUGIN = CoreMain.getInstance();
    protected CoreMain MAIN = CoreMain.getInstance();
    protected final Component USAGE = tl("correctCommandUsage", getClass().getAnnotation(DuckCommand.class).command(), getClass().getAnnotation(DuckCommand.class).usageARGS());
    protected final Component NO_PERMISSIONS = tl("noPermissions");

    protected DestiPlayer player(Player p) {
        return CoreMain.getInstance().getDestiServer().getPlayer(p.getUniqueId());
    }

    protected String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);//Strings.colorHex(str);
    }

    protected Component tl(String s, Object... objects) {
        return SERVER.tl(s, objects);
    }
}