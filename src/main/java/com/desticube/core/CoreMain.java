package com.desticube.core;

import com.desticube.core.api.objects.DestiServer;
import com.desticube.core.api.objects.Wiki;
import com.desticube.core.hooks.DestiExpansion;
import com.desticube.core.hooks.DestiExtension;
import com.desticube.core.hooks.VaultHook;
import com.desticube.placeholders.api.Placeholders;
import com.gamerduck.commons.commands.DuckCommand;
import com.gamerduck.commons.commands.DuckCommandHandler;
import com.gamerduck.commons.files.FileResClassLoader;
import com.gamerduck.commons.files.UTF8PropertiesControl;
import com.gamerduck.commons.general.Classes;
import com.gamerduck.commons.items.DuckItemListener;
import com.gamerduck.commons.listeners.DuckListener;
import com.gamerduck.commons.listeners.DuckListenerHandler;
import com.gamerduck.commons.storage.JSONConfig;
import org.bukkit.Bukkit;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;
import java.util.ResourceBundle;

import static java.io.File.separator;

public class CoreMain extends JavaPlugin {

    public static CoreMain instance;
    public static CoreMain getInstance() {return instance;}
    public FileConfiguration config;
    public FileConfiguration getConfig() {return config;}
    public JSONConfig worldsStorage;
    public ResourceBundle messagesConfig;
    public DestiServer destiServer;
    public DestiServer getDestiServer() {
        return destiServer;
    }

    @Override
    public void onEnable() {
        instance = this;
        loadConfigs();
        destiServer = DestiServer.loadServerData(config, messagesConfig, new VaultHook(this), instance);
        new DuckCommandHandler(this, getName(), Classes.getClassesWithAnnotation("com.desticube.core.com.desticube.core.commands", getClassLoader(), DuckCommand.class));
        new DuckListenerHandler(this, Classes.getClassesWithAnnotation("com.desticube.core.com.desticube.core.listeners", getClassLoader(), DuckListener.class));
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) new DestiExpansion(destiServer).register();
        if (Bukkit.getPluginManager().getPlugin("DestiPlaceholders") != null) Placeholders.register(new DestiExtension(destiServer));

        try {new Wiki(this);}
        catch (IOException e) {e.printStackTrace();}
        DuckItemListener.setup(this);
    }

    @Override
    public void onDisable() {
        destiServer.shutDown();
        if (Bukkit.getWorlds() != null) {
            worldsStorage.clear();
            JSONArray jsonArray = new JSONArray();
            Bukkit.getWorlds().forEach(w -> {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("worldName", w.getName());
                jsonArray.add(jsonObject);
            });
            worldsStorage.add("worlds", jsonArray);
            worldsStorage.save();
        }
    }

    private void loadConfigs() {
        File configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            configFile.getParentFile().mkdir();
            saveResource("config.yml", false);
        }
        config = new YamlConfiguration();
        try {config.load(configFile);}
        catch (IOException | InvalidConfigurationException e) {e.printStackTrace();}

        File messagesFile = new File(getDataFolder(), "messages.properties");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdir();
            saveResource("messages.properties", false);
        }
        messagesConfig = ResourceBundle.getBundle("messages", Locale.ENGLISH,
                new FileResClassLoader(getClass().getClassLoader(), this), new UTF8PropertiesControl());

        File worldsFile = new File(getDataFolder() + File.separator + "storage", "worlds.json");
        worldsStorage = new JSONConfig(worldsFile);
        worldsStorage.reload();
        if (!worldsStorage.getArray("worlds").isEmpty()) {
            for (int i = 0; i < worldsStorage.getArray("worlds").size(); i++) {
                JSONArray array = worldsStorage.getArray("worlds");
                Object obj = array.get(i);
                if (obj instanceof JSONObject jsonObject) {
                    String name = (jsonObject.get("worldName") instanceof String) ? (String) jsonObject.get("worldName") : "somethingIsWrong";
                    if (Files.exists(Path.of(Bukkit.getWorldContainer().getAbsolutePath() + separator + name)))
                        Bukkit.createWorld(new WorldCreator(name));

                    continue;
                }
            }
        }
    }
}
