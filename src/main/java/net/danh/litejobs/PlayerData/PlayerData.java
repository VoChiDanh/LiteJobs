package net.danh.litejobs.PlayerData;

public class PlayerData {
    private final String p;
    private final String job;
    private final String xp;
    private final String level;

    public PlayerData(String p, String job, String xp, String level) {
        this.p = p;
        this.job = job;
        this.xp = xp;
        this.level = level;
    }

    public String getPlayer() {
        return p;
    }

    public String getJob() {
        return job;
    }

    public String getLevel() {
        return level;
    }

    public String getXP() {
        return xp;
    }
}
