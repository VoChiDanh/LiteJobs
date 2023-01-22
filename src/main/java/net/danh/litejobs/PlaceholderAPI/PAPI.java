package net.danh.litejobs.PlaceholderAPI;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
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
        }
        return null;
    }
}
