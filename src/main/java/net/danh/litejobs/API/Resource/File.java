package net.danh.litejobs.API.Resource;

import net.danh.litejobs.LiteJobs;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class File {

    public static List<String> getJobList() {
        java.io.File folder = new java.io.File(LiteJobs.getLiteJobs().getDataFolder(), "/Jobs");
        java.io.File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            List<java.io.File> files = Arrays.stream(listOfFiles).toList();
            return files.stream().map(java.io.File::getName).collect(Collectors.toList());
        }
        return null;
    }

    public static FileConfiguration getConfig() {
        return SimpleConfigurationManager.get().get("config.yml");
    }

    public static FileConfiguration getMessage() {
        return SimpleConfigurationManager.get().get("message.yml");
    }

    public static FileConfiguration getJobs() {
        return SimpleConfigurationManager.get().get("jobs.yml");
    }

    public static void createFiles() {
        SimpleConfigurationManager.get().build("", false, "config.yml", "message.yml", "jobs.yml", "Jobs/mining.yml", "Jobs/farming.yml");

    }

    public static void reloadFile() {
        SimpleConfigurationManager.get().reload("config.yml", "message.yml", "jobs.yml", "Jobs/mining.yml", "Jobs/farming.yml");
    }

    public static void saveFile() {
        SimpleConfigurationManager.get().save("config.yml", "message.yml", "jobs.yml", "Jobs/mining.yml", "Jobs/farming.yml");
    }
}
