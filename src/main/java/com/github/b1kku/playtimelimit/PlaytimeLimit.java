package com.github.b1kku.playtimelimit;

import com.github.b1kku.playtimelimit.commands.playTime;
import com.github.b1kku.playtimelimit.commands.resetTime;
import com.github.b1kku.playtimelimit.listeners.joinListener;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class PlaytimeLimit extends JavaPlugin {

    private static PlaytimeLimit plugin;

    @Override
    public void onEnable() {
        plugin = this;
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        //Setup and save the playtimefile, just in case a player is on when it's loading.
        PlayTimeData.setup();
        PlayTimeData.save();

        Objects.requireNonNull(getCommand("resetTime")).setExecutor(new resetTime());
        Objects.requireNonNull(getCommand("playTime")).setExecutor(new playTime());
        getServer().getPluginManager().registerEvents(new joinListener(), this);

        //If next reset doesn't exist, create it.
        if (getConfig().get("nextReset") == null){
            plugin.reloadConfig();
            getConfig().set("nextReset", PlayTimeData.getResetDate());
            saveConfig();
        }

        //Controls time consumption for the plugin, probably also bossbar sending.
        new BukkitRunnable() {
            public void run() {
                PlayTimeData.consumeTime();
            }
        }.runTaskTimer(this, 0L, 20L * getConfig().getInt("updateTimeSeconds"));

        //Controls file operations, aka backups to files and resets in case time is past reset.
        new BukkitRunnable() {
            public void run() {
                if (LocalDateTime.parse(Objects.requireNonNull(getConfig().getString("nextReset")), DateTimeFormatter.ISO_DATE_TIME)
                        .isBefore(LocalDateTime.now())) {
                    PlayTimeData.reset();
                }
                else {
                    PlayTimeData.save();
                    PlayTimeData.reload();
                }

            }
        }.runTaskTimer(this, 0L, 20L * getConfig().getInt("backupTimeSeconds"));
    }

    @Override
    public void onDisable() {
        PlayTimeData.save();
    }

    public static PlaytimeLimit getPlugin() {
        return plugin;
    }
}
