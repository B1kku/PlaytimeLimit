package com.github.b1kku.playtimelimit;

import java.util.logging.Level;
import org.bukkit.plugin.java.JavaPlugin;

public final class PlaytimeLimit extends JavaPlugin {

    private static PlaytimeLimit plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();

        //DEBUG
        getLogger().log(Level.WARNING, "" + getConfig().getInt("Time"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlaytimeLimit getPlugin() {
        return plugin;
    }
}
