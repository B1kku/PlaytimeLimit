package com.github.b1kku.playtimelimit.commands;

import com.github.b1kku.playtimelimit.PlayTimeData;
import com.github.b1kku.playtimelimit.PlaytimeLimit;
import java.util.Map;
import java.util.logging.Level;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class playTime implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (args.length != 3) return false;
        Map<String, Object> playerPlayTimes = PlayTimeData.getPlayerPlayTimes();
        if (playerPlayTimes.get(args[1]) == null){
            if (sender instanceof Player p){
                p.sendMessage("Player not found");
                return false;
            }
            PlaytimeLimit.getPlugin().getLogger().log(Level.INFO, "Player not found");
            return false;
        }
        try {
            if (args[0].compareToIgnoreCase("add") == 0){
                playerPlayTimes.put(args[1], Integer.parseInt(String.valueOf(
                        playerPlayTimes.get(args[1]))) + Integer.parseInt(args[2]));
                return true;
            }
            else if(args[0].compareToIgnoreCase("reduce") == 0){
                playerPlayTimes.put(args[1], Integer.parseInt(String.valueOf(
                        playerPlayTimes.get(args[1]))) - Integer.parseInt(args[2]));
                return true;
            } else{
                if (sender instanceof Player p){
                    p.sendMessage("Usage: /command <add/reduce> player seconds");
                    return true;
                }
                PlaytimeLimit.getPlugin().getLogger().log(Level.INFO, "Usage: /command <add/reduce> player seconds");
                return true;
            }
        }
        catch (NumberFormatException e) {
            if (sender instanceof Player p){
                p.sendMessage("Wrong number");
                return false;
            }
            PlaytimeLimit.getPlugin().getLogger().log(Level.INFO, "Wrong number");
            return false;
        }
    }
}
