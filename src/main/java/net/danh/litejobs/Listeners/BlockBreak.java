package net.danh.litejobs.Listeners;

import io.lumine.mythic.lib.api.item.NBTItem;
import me.clip.placeholderapi.PlaceholderAPI;
import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.API.Manager.CooldownManager;
import net.danh.litejobs.API.Utils.Chat;
import net.danh.litejobs.API.WorldGuard.WorldGuard;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.Ageable;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BlockBreak implements Listener {

    public static final HashMap<Location, Material> blocks = new HashMap<>();
    public static final List<Location> locations = new ArrayList<>();

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = e.getPlayer();
        String job = PreJobs.job.get(p);
        Long level = PreJobs.level.get(p.getName() + "_" + job);
        Jobs jobs = new Jobs(job);
        FileConfiguration file = jobs.getFileConfiguration();
        Location location = e.getBlock().getLocation();
        Chat.debug(job);
        Chat.debug(String.valueOf(jobs));
        Chat.debug(String.valueOf(file));
        if (Objects.requireNonNull(file.getConfigurationSection("listeners")).getKeys(false).contains("block-break")) {
            Chat.debug("1");
            if (!file.getStringList("listeners.block-break." + e.getBlock().getType() + ".command").isEmpty()) {
                Chat.debug("2");
                int regen = file.getInt("listeners.block-break." + e.getBlock().getType() + ".regen");
                String regen_block = file.getString("listeners.block-break." + e.getBlock().getType() + ".regen_block");
                List<String> CMDs = file.getStringList("listeners.block-break." + e.getBlock().getType() + ".command");
                if (regen_block != null) {
                    Chat.debug("3");
                    Material material = Material.getMaterial(regen_block);
                    if (material != null) {
                        Chat.debug("4");
                        if (WorldGuard.handleForLocation(p, e.getBlock().getLocation(), e, "litejobs-block-break")) {
                            Chat.debug("5");
                            NBTItem nbtItem = NBTItem.get(p.getInventory().getItemInMainHand());
                            if (nbtItem != null && nbtItem.getType() != null) {
                                Chat.debug("6");
                                if (nbtItem.getType().equalsIgnoreCase(new Jobs(job).getFileConfiguration().getString("item.mmoitems.type")) && nbtItem.getString("MMOITEMS_ITEM_ID").equalsIgnoreCase(new Jobs(job).getFileConfiguration().getString("item.mmoitems.id"))) {
                                    Chat.debug("7");
                                    if (!(e.getBlock().getBlockData() instanceof Ageable ageable)) {
                                        Chat.debug("8");
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
                                        Chat.debug("9");
                                        if (ageable.getAge() == ageable.getMaximumAge()) {
                                            Chat.debug("10");
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
                                            Chat.debug("11");
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
