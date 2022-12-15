package com.github.b1kku.playtimelimit.listeners;

import com.github.b1kku.playtimelimit.PlayTimeData;
import java.util.Map;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinListener implements Listener{
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        String playerName = event.getPlayer().getName();
        Map<String, Object> playtimes = PlayTimeData.getPlayerPlayTimes();
        Map<String, BossBar> bossbars = PlayTimeData.getPlayerBossBars();

        if (playtimes.get(playerName) == null) return;
        else if (((int)playtimes.get(playerName)) <= 0) {
            event.getPlayer().kick(Component.text("You've exceeded the playtime"));
        }
        //Make sure we send the player their bossbar when they join.
        if (bossbars.get(playerName) != null){
            event.getPlayer().showBossBar(bossbars.get(playerName));
        }
    }
}
