package net.danh.litejobs.Listeners;

import io.lumine.mythic.lib.api.item.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.API.Manager.CooldownManager;
import net.danh.litejobs.API.WorldGuard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BlockBreak implements Listener {

    public static final HashMap<Location, Material> blocks = new HashMap<>();
    public static final List<Location> locations = new ArrayList<>();

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        String job = PreJobs.job.get(p);
        Long level = PreJobs.level.get(p.getName() + "_" + job);
        Jobs jobs = new Jobs(job);
        FileConfiguration file = jobs.getFileConfiguration();
        Location location = e.getBlock().getLocation();
        if (Objects.requireNonNull(file.getConfigurationSection("listeners")).getKeys(false).contains("block-break")) {
            if (!file.getStringList("listeners.block-break." + e.getBlock().getType() + ".command").isEmpty()) {
                int regen = file.getInt("listeners.block-break." + e.getBlock().getType() + ".regen");
                String regen_block = file.getString("listeners.block-break." + e.getBlock().getType() + ".regen_block");
                List<String> CMDs = file.getStringList("listeners.block-break." + e.getBlock().getType() + ".command");
                if (regen_block != null) {
                    Material material = Material.getMaterial(regen_block);
                    if (material != null) {
                        if (WorldGuard.handleForLocation(p, e.getBlock().getLocation(), e, "litejobs-block-break")) {
                            NBTItem nbtItem = NBTItem.get(p.getInventory().getItemInMainHand());
                            if (nbtItem != null && nbtItem.getType() != null) {
                                if (nbtItem.getType().equalsIgnoreCase(new Jobs(job).getFileConfiguration().getString("item.mmoitems.type")) && nbtItem.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(new Jobs(job).getFileConfiguration().getString("item.mmoitems.id"))) {
                                    if (!(e.getBlock().getBlockData() instanceof Ageable)) {
                                        locations.add(location);
                                        blocks.put(location, e.getBlock().getType());
                                        CooldownManager.setCooldown(location, regen);
                                        e.getBlock().setType(material);
                                        e.setDropItems(false);
                                        CMDs.forEach(cmd -> {
                                            cmd = PlaceholderAPI.setPlaceholders(p, cmd);
                                            cmd = cmd.replace("<level>", String.valueOf(level));
                                            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                        });
                                    } else {
                                        Ageable ageable = (Ageable) e.getBlock().getBlockData();
                                        if (ageable.getAge() == ageable.getMaximumAge()) {
                                            e.setDropItems(false);
                                            locations.add(location);
                                            blocks.put(location, e.getBlock().getType());
                                            CooldownManager.setCooldown(location, regen);
                                            e.getBlock().setType(material);
                                            CMDs.forEach(cmd -> {
                                                cmd = PlaceholderAPI.setPlaceholders(p, cmd);
                                                cmd = cmd.replace("<level>", String.valueOf(level));
                                                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), cmd);
                                            });
                                        } else {
                                            e.setCancelled(true);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
