package net.danh.litejobs.API.Data;

import net.danh.litejobs.API.Manager.Utils;
import net.danh.litejobs.API.Resource.File;
import net.danh.litejobs.LiteJobs;
import net.danh.litejobs.PlayerData.PlayerData;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class PreJobs {


    public static final HashMap<Player, String> job = new HashMap<>();
    public static final HashMap<String, Long> level = new HashMap<>();
    public static final HashMap<String, Long> xp = new HashMap<>();

    public static void loadPlayerData(org.bukkit.entity.Player p) {
        try {
            PlayerData playerData = getPlayerDatabase(p);
            job.put(p, playerData.getJob());
            List<String> jobs = File.getJobList();
            if (jobs != null) {
                for (String job : jobs) {
                    job = job.replace(".yml", "");
                    AtomicLong atomicLevel = new AtomicLong();
                    AtomicLong atomicXP = new AtomicLong();
                    String[] levels = playerData.getLevel().split(";");
                    String[] XPs = playerData.getXP().split(";");
                    for (int i = 0; i < jobs.size(); i++) {
                        if (levels[i] != null) {
                            String[] level = levels[i].split("=");
                            if (level[0].equalsIgnoreCase(job)) {
                                atomicLevel.set(Math.max(Utils.getLong(level[1]), 1));
                            }
                        }
                        if (XPs[i] != null) {
                            String[] xp = XPs[i].split("=");
                            if (xp[0].equalsIgnoreCase(job)) {
                                atomicXP.set(Utils.getLong(xp[1]));
                            }
                        }
                    }
                    level.put(p.getName() + "_" + job, atomicLevel.get());
                    xp.put(p.getName() + "_" + job, atomicXP.get());
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void savePlayerData(org.bukkit.entity.Player p) {
        LiteJobs.getDatabase().updateTable(new PlayerData(p.getName(), job.get(p), getXPString(p), getLevelString(p)));
    }

    public static String getLevelString(Player p) {
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            HashMap<Player, List<String>> pstring = new HashMap<>();
            List<String> list_string = new ArrayList<>();
            for (String s : jobs) {
                s = s.replace(".yml", "");
                if (!PreJobs.level.containsKey(p.getName() + "_" + s)) {
                    PreJobs.level.put(p.getName() + "_" + s, (long) 1);
                }
                list_string.add(s + "=" + PreJobs.level.get(p.getName() + "_" + s));
            }
            pstring.put(p, list_string);
            AtomicReference<String> atomic = new AtomicReference<>();
            for (String s : pstring.get(p)) {
                if (pstring.get(p).get(0).equalsIgnoreCase(s)) {
                    atomic.set(s);
                } else if (pstring.get(p).get(pstring.size() - 1).equalsIgnoreCase(s)) {
                    atomic.set(atomic.get() + ";" + s);
                    break;
                } else {
                    atomic.set(atomic.get() + ";" + s);
                }
            }
            return atomic.get();
        }
        return null;
    }

    public static String getXPString(Player p) {
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            HashMap<Player, List<String>> pstring = new HashMap<>();
            List<String> list_string = new ArrayList<>();
            for (String s : jobs) {
                s = s.replace(".yml", "");
                if (!PreJobs.xp.containsKey(p.getName() + "_" + s)) {
                    PreJobs.xp.put(p.getName() + "_" + s, (long) 0);
                }
                list_string.add(s + "=" + PreJobs.xp.get(p.getName() + "_" + s));
            }
            pstring.put(p, list_string);
            AtomicReference<String> atomic = new AtomicReference<>();
            for (String s : pstring.get(p)) {
                if (pstring.get(p).get(0).equalsIgnoreCase(s)) {
                    atomic.set(s);
                } else if (pstring.get(p).get(pstring.size() - 1).equalsIgnoreCase(s)) {
                    atomic.set(atomic.get() + ";" + s);
                    break;
                } else {
                    atomic.set(atomic.get() + ";" + s);
                }
            }
            return atomic.get();
        }
        return null;
    }

    public static String getNewLevelString(Player p) {
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            HashMap<Player, List<String>> pstring = new HashMap<>();
            List<String> list_string = new ArrayList<>();
            for (String s : jobs) {
                s = s.replace(".yml", "");
                list_string.add(s + "=" + 1);
            }
            pstring.put(p, list_string);
            AtomicReference<String> atomic = new AtomicReference<>();
            for (String s : pstring.get(p)) {
                if (pstring.get(p).get(0).equalsIgnoreCase(s)) {
                    atomic.set(s);
                } else if (pstring.get(p).get(pstring.size() - 1).equalsIgnoreCase(s)) {
                    atomic.set(atomic.get() + ";" + s);
                    break;
                } else {
                    atomic.set(atomic.get() + ";" + s);
                }
            }
            return atomic.get();
        }
        return null;
    }

    public static String getNewXPString(Player p) {
        List<String> jobs = File.getJobList();
        if (jobs != null) {
            HashMap<Player, List<String>> pstring = new HashMap<>();
            List<String> list_string = new ArrayList<>();
            for (String s : jobs) {
                s = s.replace(".yml", "");
                list_string.add(s + "=" + 0);
            }
            pstring.put(p, list_string);
            AtomicReference<String> atomic = new AtomicReference<>();
            for (String s : pstring.get(p)) {
                if (pstring.get(p).get(0).equalsIgnoreCase(s)) {
                    atomic.set(s);
                } else if (pstring.get(p).get(pstring.size() - 1).equalsIgnoreCase(s)) {
                    atomic.set(atomic.get() + ";" + s);
                    break;
                } else {
                    atomic.set(atomic.get() + ";" + s);
                }
            }
            return atomic.get();
        }
        return null;
    }


    public static PlayerData getPlayerDatabase(org.bukkit.entity.Player player) throws SQLException {

        PlayerData playerStats = LiteJobs.getDatabase().getPlayerData(player.getName());

        if (playerStats == null) {
            playerStats = new PlayerData(player.getName(), "mining", getNewXPString(player), getNewLevelString(player));
            LiteJobs.getDatabase().createTable(playerStats);
        }

        return playerStats;
    }

    public static long getXP(Player p) {
        return xp.get(getStringData(p));
    }

    public static String getStringData(Player p) {
        return p.getName() + "_" + job.get(p);
    }

    public static void addXP(Player p, long amount) {
        Jobs jobs = new Jobs(job.get(p));
        long max = jobs.getXPCalculator(p);
        long replace = getXP(p) + amount;
        if (replace < max) {
            xp.replace(getStringData(p), replace);
        } else {
            xp.replace(getStringData(p), 0L);
            addLevel(p, 1);
        }
    }

    public static void removeXP(Player p, long amount) {
        Jobs jobs = new Jobs(job.get(p));
        long max = jobs.getXPCalculator(p);
        long replace = getXP(p) - amount;
        if (replace <= max && replace > 0) {
            xp.replace(getStringData(p), replace);
        }
    }

    public static long getLevel(Player p) {
        return level.get(getStringData(p));
    }


    public static void addLevel(Player p, long amount) {
        Jobs jobs = new Jobs(job.get(p));
        long max = jobs.getXPCalculator(p);
        long replace = getLevel(p) + amount;
        if (replace < max) {
            level.replace(getStringData(p), replace);
        } else {
            level.replace(getStringData(p), 0L);
        }
    }

    public static void removeLevel(Player p, long amount) {
        Jobs jobs = new Jobs(job.get(p));
        long max = jobs.getXPCalculator(p);
        long replace = getLevel(p) - amount;
        if (replace <= max && replace > 0) {
            level.replace(getStringData(p), replace);
        }
    }
}
