package com.github.b1kku.playtimelimit.commands;

import com.github.b1kku.playtimelimit.PlayTimeData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class resetTime implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        PlayTimeData.reset();
        return true;
    }
}
