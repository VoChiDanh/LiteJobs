package net.danh.litejobs.API.Database;

public class Errors {
    public static String sqlConnectionExecute() {
        return "Couldn't execute MySQL statement: ";
    }

    public static String sqlConnectionClose() {
        return "Failed to close MySQL connection: ";
    }

    public static String noSQLConnection() {
        return "Unable to retrieve MYSQL connection: ";
    }

    public static String noTableFound() {
        return "Database Error: No Table Found";
    }
}
