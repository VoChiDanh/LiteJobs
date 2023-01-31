package net.danh.litejobs.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.API.Manager.Utils;
import net.danh.litejobs.LiteJobs;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PAPI extends PlaceholderExpansion {
    @Override
    public @NotNull String getIdentifier() {
        return "lj";
    }

    @Override
    public @NotNull String getAuthor() {
        return LiteJobs.getLiteJobs().getDescription().getAuthors().toString();
    }

    @Override
    public @NotNull String getVersion() {
        return LiteJobs.getLiteJobs().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public @Nullable String onPlaceholderRequest(Player p, @NotNull String args) {
        if (p != null) {
            if (args.equalsIgnoreCase("job")) {
                return new Jobs(PreJobs.job.get(p)).getDisplayName();
            }
            if (args.equalsIgnoreCase("level")) {
                return String.valueOf(PreJobs.level.get(p.getName() + "_" + PreJobs.job.get(p)));
            }
            if (args.equalsIgnoreCase("xp")) {
                return String.valueOf(PreJobs.xp.get(p.getName() + "_" + PreJobs.job.get(p)));
            }
            if (args.equalsIgnoreCase("xp_max")) {
                return String.valueOf(new Jobs(PreJobs.job.get(p)).getXPCalculator(p));
            }
            if (args.equalsIgnoreCase("level_format")) {
                return Utils.format(PreJobs.level.get(p.getName() + "_" + PreJobs.job.get(p)));
            }
            if (args.equalsIgnoreCase("xp_format")) {
                return Utils.format(PreJobs.xp.get(p.getName() + "_" + PreJobs.job.get(p)));
            }
            if (args.equalsIgnoreCase("xp_max_format")) {
                return Utils.format(new Jobs(PreJobs.job.get(p)).getXPCalculator(p));
            }
        }
        return null;
    }
}
