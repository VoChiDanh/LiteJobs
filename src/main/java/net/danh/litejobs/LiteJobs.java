package net.danh.litejobs;

import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.API.Database.Database;
import net.danh.litejobs.API.Database.SQLite;
import net.danh.litejobs.API.Manager.CooldownManager;
import net.danh.litejobs.API.NMS.NMSAssistant;
import net.danh.litejobs.API.Resource.File;
import net.danh.litejobs.API.Utils.Chat;
import net.danh.litejobs.API.WorldGuard.WorldGuard;
import net.danh.litejobs.Command.MainCMD;
import net.danh.litejobs.Listeners.BlockBreak;
import net.danh.litejobs.Listeners.JoinQuit;
import net.danh.litejobs.PlaceholderAPI.PAPI;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;

public final class LiteJobs extends JavaPlugin {

    private static String prefix;
    private static LiteJobs liteJobs;
    private static Database database;

    public static LiteJobs getLiteJobs() {
        return liteJobs;
    }

    @Contract(pure = true)
    public static @NotNull String getPrefix() {
        return prefix + " &f";
    }

    public static Database getDatabase() {
        return database;
    }

    @Override
    public void onLoad() {
        liteJobs = this;
        SimpleConfigurationManager.register(liteJobs);
        getLogger().log(Level.INFO, "Detected Server Version " + new NMSAssistant().getNMSVersion());
        WorldGuard.register(liteJobs);
    }

    @Override
    public void onEnable() {
        new MainCMD();
        getLogger().log(Level.INFO, "Registered Main Command");
        File.createFiles();
        getLogger().log(Level.INFO, "Loaded Files");
        prefix = Chat.colorize(Objects.requireNonNull(File.getConfig().getString("prefix")));
        getLogger().log(Level.INFO, "Loaded Prefix " + Chat.colorize(prefix));
        database = new SQLite(liteJobs);
        database.load();
        getLogger().log(Level.INFO, "Loaded Database");
        registerListeners(new JoinQuit(), new BlockBreak());
        new PAPI().register();
        getLogger().log(Level.INFO, "Registered Listeners");
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            if (!jobs.isEmpty()) {
                jobs.forEach(job -> new Jobs(job).load());
            }
        }
        getLogger().log(Level.INFO, "Loaded Jobs");
        getServer().getOnlinePlayers().forEach(PreJobs::loadPlayerData);
        getLogger().log(Level.INFO, "Loaded PlayerData");
        getLogger().log(Level.INFO, "Loaded Complete");
        Bukkit.getScheduler().scheduleSyncRepeatingTask(liteJobs, () -> {
            if (!BlockBreak.locations.isEmpty()) {
                for (int i = 0; i < BlockBreak.locations.size(); i++) {
                    Location location = BlockBreak.locations.get(i);
                    int times = CooldownManager.getCooldown(location);
                    if (Math.abs(times) > 0) {
                        CooldownManager.setCooldown(location, --times);
                    } else {
                        Material block_type = BlockBreak.blocks.get(location);
                        location.getBlock().setType(block_type != null ? block_type : Material.AIR);
                        BlockBreak.blocks.remove(location, block_type);
                        BlockBreak.locations.remove(location);
                    }
                }
            }
        }, 20L, 20L);
    }

    @Override
    public void onDisable() {
        File.saveFile();
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            if (!jobs.isEmpty()) {
                jobs.forEach(job -> new Jobs(job).save());
            }
        }
        for (Location location : BlockBreak.locations) {
            location.getBlock().setType(BlockBreak.blocks.get(location));
        }
        getServer().getOnlinePlayers().forEach(PreJobs::savePlayerData);
    }

    private void registerListeners(Listener... listeners) {
        Arrays.stream(listeners).toList().forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, liteJobs));
    }

}
