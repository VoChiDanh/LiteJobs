package net.danh.litejobs.API.Data;

import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.litejobs.API.Calculator.Calculator;
import net.danh.litejobs.API.Resource.File;
import net.danh.litejobs.API.Utils.Chat;
import net.danh.litejobs.LiteJobs;
import net.xconfig.bukkit.model.SimpleConfigurationManager;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.logging.Level;

public class Jobs {
    private final String name;
    private final FileConfiguration fileConfiguration;

    public Jobs(String name) {
        if (!name.contains(".yml")) {
            this.name = name;
        } else {
            this.name = name.replace(".yml", "");
        }
        this.fileConfiguration = SimpleConfigurationManager.get().get("Jobs/" + this.name + ".yml");
    }

    public String getName() {
        return name;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public void load() {
        SimpleConfigurationManager.get().build("", false, "Jobs/" + name + ".yml");
        LiteJobs.getLiteJobs().getLogger().log(Level.INFO, "Loaded " + name + " Job!");
    }

    public void save() {
        if (fileConfiguration == null) load();
        SimpleConfigurationManager.get().save("Jobs/" + name + ".yml");
    }

    public void reload() {
        if (fileConfiguration == null) load();
        SimpleConfigurationManager.get().reload("Jobs/" + name + ".yml");
    }

    public String getDisplayName() {
        return Chat.colorize(Objects.requireNonNull(fileConfiguration.getString("displayname")));
    }

    public Long getXPCalculator(Player p) {
        String xp = File.getJobs().getString("level." + Objects.requireNonNull(fileConfiguration.getString("level")));
        if (xp == null)
            return Double.doubleToLongBits(Double.parseDouble(Calculator.calculator(String.valueOf(PreJobs.level.get(p.getName() + "_" + name) * 1000), 0)));
        xp = PlaceholderAPI.setPlaceholders(p, xp);
        xp = xp.replace("<level>", String.valueOf(PreJobs.level.get(p.getName() + "_" + name)));
        return Double.doubleToLongBits(Double.parseDouble(Calculator.calculator(xp, 0)));
    }


}
