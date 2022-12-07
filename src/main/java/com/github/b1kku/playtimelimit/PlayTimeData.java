package com.github.b1kku.playtimelimit;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayTimeData {

    private static File file;
    private static FileConfiguration playTimesFile;
    private static final Plugin plugin = PlaytimeLimit.getPlugin();
    private static Map<String, Object> playerPlayTimes;

    //Makes a new file if it doesn't exist, loads it as a yaml and populates the Map with pre-existing playtimes.
    public static void setup(){

        file = new File(plugin.getDataFolder(), "playTimes.yml");

        if (!file.exists()){
            try{
                file.createNewFile();
            } catch (IOException e){
                plugin.getLogger().log(Level.SEVERE, "Unable to create file, this is bad");
            }
        }
        playTimesFile = YamlConfiguration.loadConfiguration(file);
        playerPlayTimes = playTimesFile.getValues(true);
    }

    //For each player online, reduces the time in seconds since the last time consumption. Also handles creating them if they didn't exist.
    public static void consumeTime(){
        plugin.getServer().getOnlinePlayers().forEach(
                (player -> {
                    if (playerPlayTimes.get(player.getName()) == null){
                        playerPlayTimes.put(player.getName(),
                                plugin.getConfig().getInt("playTimeLimitSeconds") - plugin.getConfig().getInt("updateTimeSeconds"));
                    } else {
                        playerPlayTimes.put(player.getName(),
                                (Integer)playerPlayTimes.get(player.getName()) - plugin.getConfig().getInt("updateTimeSeconds"));
                    }
                    //Kick player if they're below 0, AKA time is consumed.
                    if (Integer.parseInt(String.valueOf(playerPlayTimes.get(player.getName()))) <= 0){
                        player.kick(Component.text("You've exceeded the playtime"));
                    }
                }));
    }

    //Saves the current Map state to the file and saves the file.
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

    //Resets the time by deleting the file, clearing the map, sets the file again and sets a new reset date.
    public static void reset(){
        playerPlayTimes.clear();
        file.delete();
        setup();
        plugin.getConfig().set("nextReset", getResetDate());
        plugin.saveConfig();
    }

    //Returns a new reset date with the decided hour and days it takes to reset, formats as a string.
    public static @NotNull String getResetDate(){
        return LocalDateTime.now().withHour(plugin.getConfig().getInt("resetTimeHour"))
                .withMinute(0).withSecond(0).withNano(0)
                .plusDays(plugin.getConfig().getInt("resetTimeDays")).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    public static FileConfiguration getPlayTimesFile(){
        return playTimesFile;
    }

    public static Map<String, Object> getPlayerPlayTimes(){
        return playerPlayTimes;
    }

}
