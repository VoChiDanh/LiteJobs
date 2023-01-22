package net.danh.litejobs.Command;

import net.danh.litejobs.API.CMD.CMDBase;
import net.danh.litejobs.API.Data.Jobs;
import net.danh.litejobs.API.Data.PreJobs;
import net.danh.litejobs.API.Resource.File;
import net.danh.litejobs.LiteJobs;
import net.xconfig.bukkit.TextUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainCMD extends CMDBase {
    public MainCMD() {
        super("LiteJobs");
    }

    @Override
    public void execute(CommandSender c, String @NotNull [] args) {
        if (c.hasPermission("litejobs.admin")) {
            if (args.length == 3) {
                Player p = Bukkit.getPlayer(args[2]);
                if (p != null) {
                    if (args[0].equalsIgnoreCase("select")) {
                        List<String> file = File.getJobList();
                        if (file != null) {
                            file.forEach(job -> {
                                job = job.replace(".yml", "");
                                if (job.equalsIgnoreCase(args[1])) {
                                    PreJobs.job.put(p, job);
                                    p.sendMessage(TextUtils.colorize(Objects.requireNonNull(File.getMessage().getString("command.select")).replace("<name>", new Jobs(job).getDisplayName())));
                                }
                            });
                        }
                    }
                }
            }
            if (args.length == 2) {
                if (c instanceof Player) {
                    Player p = (Player) c;
                    if (args[0].equalsIgnoreCase("select")) {
                        List<String> file = File.getJobList();
                        if (file != null) {
                            file.forEach(job -> {
                                job = job.replace(".yml", "");
                                if (job.equalsIgnoreCase(args[1])) {
                                    PreJobs.job.put(p, job);
                                    p.sendMessage(TextUtils.colorize(LiteJobs.getPrefix() + Objects.requireNonNull(File.getMessage().getString("command.select")).replace("<name>", new Jobs(job).getDisplayName())));
                                }
                            });
                        }
                    }
                }
                if (args[0].equalsIgnoreCase("jobs")) {
                    if (args[1].equalsIgnoreCase("reload")) {
                        List<String> jobs = File.getJobList();
                        if (jobs != null) {
                            if (!jobs.isEmpty()) {
                                jobs.forEach(job -> new Jobs(job).reload());
                                c.sendMessage(TextUtils.colorize(LiteJobs.getPrefix() + File.getMessage().getString("command.reload")));
                            }
                        }
                    }
                }
            }
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    File.reloadFile();
                    c.sendMessage(TextUtils.colorize(LiteJobs.getPrefix() + File.getMessage().getString("command.reload")));
                }
            }
        }
    }

    @Override
    public List<String> TabComplete(@NotNull CommandSender sender, String[] args) {
        List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        if (sender.hasPermission("litejobs.admin")) {
            if (args.length == 1) {
                commands.add("reload");
                commands.add("jobs");
                commands.add("select");
                StringUtil.copyPartialMatches(args[0], commands, completions);
            }
            if (args.length == 2) {
                if (args[0].equalsIgnoreCase("jobs")) {
                    commands.add("reload");
                    StringUtil.copyPartialMatches(args[1], commands, completions);
                }
                if (args[0].equalsIgnoreCase("select")) {
                    List<String> jobs = File.getJobList();
                    if (jobs != null) {
                        for (String s : jobs) {
                            commands.add(s.replace(".yml", ""));
                            StringUtil.copyPartialMatches(args[1], commands, completions);
                        }
                    }
                }
            }
            if (args.length == 3) {
                if (args[0].equalsIgnoreCase("select")) {
                    StringUtil.copyPartialMatches(args[2], Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList()), completions);
                }
            }
        }
        Collections.sort(completions);
        return completions;
    }
}
