package net.danh.litejobs.API.Database;


import net.danh.litejobs.LiteJobs;

import java.util.logging.Level;

public class Error {

    public static void execute(Exception ex) {
        LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "Couldn't execute MySQL statement: ", ex);
    }

    public static void close(Exception ex) {
        LiteJobs.getLiteJobs().getLogger().log(Level.SEVERE, "Failed to close MySQL connection: ", ex);
    }
}
