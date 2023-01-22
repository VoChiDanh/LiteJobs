package net.danh.litejobs.Listeners;

import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.LiteJobs;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuit implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(LiteJobs.getLiteJobs(), () -> PreJobs.loadPlayerData(e.getPlayer()));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        Bukkit.getScheduler().runTaskAsynchronously(LiteJobs.getLiteJobs(), () -> PreJobs.savePlayerData(e.getPlayer()));
    }
}
