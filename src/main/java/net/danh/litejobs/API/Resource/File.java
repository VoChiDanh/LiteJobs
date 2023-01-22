package net.danh.litejobs.API.Resource;

import net.danh.litejobs.LiteJobs;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class File {

    public static List<String> getJobList() {
        java.io.File folder = new java.io.File(LiteJobs.getLiteJobs().getDataFolder(), "/Jobs");
        java.io.File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            List<java.io.File> files = Arrays.stream(listOfFiles).collect(Collectors.toList());
            return files.stream().map(java.io.File::getName).collect(Collectors.toList());
        }
        return null;
    }

    public static FileConfiguration getConfig() {
        return LiteJobs.getSimpleConfigurationManager().file("", "config.yml");
    }

    public static FileConfiguration getMessage() {
        return LiteJobs.getSimpleConfigurationManager().file("", "message.yml");
    }

    public static FileConfiguration getJobs() {
        return LiteJobs.getSimpleConfigurationManager().file("", "jobs.yml");
    }

    public static void createFiles() {
        if (!LiteJobs.getSimpleConfigurationManager().exists("", "config.yml")
                && !LiteJobs.getSimpleConfigurationManager().exists("", "message.yml")
                && !LiteJobs.getSimpleConfigurationManager().exists("", "jobs.yml")
                && !LiteJobs.getSimpleConfigurationManager().exists("Jobs", "mining.yml")) {
            LiteJobs.getSimpleConfigurationManager().build("Jobs", "mining.yml");
            LiteJobs.getSimpleConfigurationManager().build("Jobs", "farming.yml");
        }
        LiteJobs.getSimpleConfigurationManager().build("", "config.yml", "message.yml", "jobs.yml");

    }

    public static void reloadFile() {
        LiteJobs.getSimpleConfigurationManager().reload("", "config.yml");
        LiteJobs.getSimpleConfigurationManager().reload("", "message.yml");
        LiteJobs.getSimpleConfigurationManager().reload("", "jobs.yml");
    }

    public static void saveFile() {
        LiteJobs.getSimpleConfigurationManager().save("", "config.yml");
        LiteJobs.getSimpleConfigurationManager().save("", "message.yml");
        LiteJobs.getSimpleConfigurationManager().save("", "jobs.yml");
    }
}
