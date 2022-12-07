package com.github.b1kku.playtimelimit;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlaytimeLimit extends JavaPlugin {

    private static PlaytimeLimit plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        PlayTimeData.setup();

        new BukkitRunnable() {
            public void run() {
                PlayTimeData.consumeTime();
            }
        }.runTaskTimer(this, 0L, 20L * getConfig().getInt("updateTimeSeconds"));
        //DEBUG
        new BukkitRunnable() {
            public void run() {
                PlayTimeData.save();
                PlayTimeData.reload();
            }
        }.runTaskTimer(this, 0L, 20L * getConfig().getInt("backupTimeSeconds"));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static PlaytimeLimit getPlugin() {
        return plugin;
    }
}
