package com.desticube.core.listeners.handlers;

import com.desticube.core.CoreMain;
import com.desticube.core.api.objects.DestiPlayer;
import com.desticube.core.api.objects.DestiServer;
import com.gamerduck.commons.listeners.AbstractDuckListener;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public abstract class DestiListener extends AbstractDuckListener {

    protected final DestiServer SERVER = CoreMain.getInstance().getDestiServer();
    protected final CoreMain MAIN = CoreMain.getInstance();
    protected final Plugin PLUGIN = CoreMain.getInstance();
    protected final NamespacedKey SPAWNER_KEY = SERVER.getSpawnerKey();

    protected Component tl(String s, Object... objects) {
        return SERVER.tl(s, objects);
    }

    protected DestiPlayer player(Player p) {
        return CoreMain.getInstance().getDestiServer().getPlayer(p.getUniqueId());
    }

}
