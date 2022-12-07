package com.github.b1kku.playtimelimit;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class PlayTimeData {

    private static File file;
    private static FileConfiguration playTimesFile;
    private static final Plugin plugin = PlaytimeLimit.getPlugin();
    private static Map<String, Object> playerPlayTimes;

    public static void setup(){
        file = new File(plugin.getDataFolder(), "playTimes.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e){
                plugin.getLogger().log(Level.SEVERE, "Unable to create file, this is bad");
            }
            playTimesFile = YamlConfiguration.loadConfiguration(file);
            playerPlayTimes = playTimesFile.getValues(true);
        }
    }

    public static void consumeTime(){
        plugin.getServer().getOnlinePlayers().forEach(
                (player -> {
                    if (playerPlayTimes.get(player.getName()) == null){
                        playerPlayTimes.put(player.getName(),
                                plugin.getConfig().getInt("playTimeLimitHours")*60*60 - plugin.getConfig().getInt("updateTimeSeconds"));
                    } else {
                        playerPlayTimes.put(player.getName(),
                                (Integer)playerPlayTimes.get(player.getName()) - plugin.getConfig().getInt("updateTimeSeconds"));
                    }
                }));
    }

    public static void save(){
        try{
            playerPlayTimes.forEach(
                    (key, value) -> playTimesFile.set(key, value));
            playTimesFile.save(file);
        } catch (IOException e){
            plugin.getLogger().log(Level.SEVERE, "Unable to save file with playtime, this is bad.");
        }
    }

    public static void reload(){
        playTimesFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get(){
        return playTimesFile;
    }


}
