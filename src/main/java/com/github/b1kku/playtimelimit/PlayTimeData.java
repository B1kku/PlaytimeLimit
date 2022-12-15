package com.github.b1kku.playtimelimit;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.logging.Level;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.bossbar.BossBar.Color;
import net.kyori.adventure.bossbar.BossBar.Overlay;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class PlayTimeData {

    private static File file;
    private static FileConfiguration playTimesFile;
    private static final Plugin plugin = PlaytimeLimit.getPlugin();
    private static Map<String, Object> playerPlayTimes;
    private static Map<String, BossBar> playerBossBars = new HashMap<>();

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
                                //Initialize, since time has already counted down we also take down the time.
                                plugin.getConfig().getInt("playTimeLimitSeconds") - plugin.getConfig().getInt("updateTimeSeconds"));
                    } else {
                        playerPlayTimes.put(player.getName(),
                                //Reduce the current playtime on hashmap.
                                (Integer)playerPlayTimes.get(player.getName()) - plugin.getConfig().getInt("updateTimeSeconds"));
                    }

                    //Date formatting for bossbar, since timezones can get messy I have to set a random timezone first.
                    Date date = new Date((int)playerPlayTimes.get(player.getName())*1000);
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
                    String formattedTime = dateFormat.format(date);
                    TextComponent text = Component.text("Time left:: ")
                            .style(Style.style(TextColor.fromCSSHexString("#00ffff")).decorate(TextDecoration.BOLD));
                    //If it doesn't exist, we create one, if it does, we set the new time.
                    if (playerBossBars.get(player.getName()) == null){
                        playerBossBars.put(player.getName(),
                                BossBar.bossBar(text.append(Component.text(formattedTime)),
                                        getPercentageTimeLeft(player.getName()),
                                        Color.BLUE, Overlay.NOTCHED_10));
                        player.showBossBar(playerBossBars.get(player.getName()));
                    }
                    else {
                        BossBar bar = playerBossBars.get(player.getName());
                        bar.name(text.append(Component.text(formattedTime)));
                        bar.progress(getPercentageTimeLeft(player.getName()));
                    }

                    //Kick player if they're below 0, AKA time is consumed. And hide the bar.
                    if (((int)playerPlayTimes.get(player.getName())) <= 0){
                        playerBossBars.remove(player.getName());
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
        plugin.reloadConfig();
        plugin.getConfig().set("nextReset", getResetDate());
        plugin.saveConfig();
    }

    //Returns a new reset date with the decided hour and days it takes to reset, formats as a string.
    public static @NotNull String getResetDate(){
        return LocalDateTime.now().withHour(plugin.getConfig().getInt("resetTimeHour"))
                .withMinute(0).withSecond(0).withNano(0)
                .plusDays(plugin.getConfig().getInt("resetTimeDays")).format(DateTimeFormatter.ISO_DATE_TIME);
    }

    private static float getPercentageTimeLeft(String name){
       float percentage = (int)playerPlayTimes.get(name) * 100.0f / plugin.getConfig().getInt("playTimeLimitSeconds") / 100.0f;
       return Math.min(percentage, 1.0F);
    }

    public static FileConfiguration getPlayTimesFile(){
        return playTimesFile;
    }

    public static Map<String, Object> getPlayerPlayTimes(){
        return playerPlayTimes;
    }

    public static Map<String, BossBar> getPlayerBossBars(){
        return playerBossBars;
    }

}
