package com.github.b1kku.playtimelimit.listeners;

import com.github.b1kku.playtimelimit.PlayTimeData;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinListener implements Listener{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (PlayTimeData.getPlayerPlayTimes().get(event.getPlayer().getName()) == null) return;
        else if (Integer.parseInt(String.valueOf(PlayTimeData.getPlayerPlayTimes().get(event.getPlayer().getName()))) <= 0) {
            event.getPlayer().kick(Component.text("You've exceeded the playtime"));
        }

    }
}
