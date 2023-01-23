package net.danh.litejobs.API.Manager;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    private static final String[] suffix = new String[]{"", "K", "M", "B", "T", "Q"};
    private static final int MAX_LENGTH = 4;

    public static String format(long number) {
        String r = new DecimalFormat("##0E0").format(number);
        r = r.replaceAll("E[0-9]", suffix[Character.getNumericValue(r.charAt(r.length() - 1)) / 3]);
        while (r.length() > MAX_LENGTH || r.matches("[0-9]+\\.[a-z]")) {
            r = r.substring(0, r.length() - 2) + r.substring(r.length() - 1);
        }
        return r.replace(".", "");
    }

    public static double getRandomDouble(double min, double max) {
        if (max > min) {
            return ThreadLocalRandom.current().nextDouble(min, max);
        } else {
            return min;
        }
    }

    public static int getRandomInteger(int min, int max) {
        if (max > min + 2) {
            return ThreadLocalRandom.current().nextInt(min, max);
        } else {
            return min;
        }
    }

    public static long getRandomLong(long min, long max) {
        if (max > min + 2) {
            return ThreadLocalRandom.current().nextLong(min, max);
        } else {
            return min;
        }
    }

    public static double getDouble(java.lang.String s) {
        try {
            if (!s.contains("-")) {
                return BigDecimal.valueOf(Double.parseDouble(s)).doubleValue();
            } else {
                return getRandomDouble(BigDecimal.valueOf(Double.parseDouble(s.split("-")[0])).doubleValue(), BigDecimal.valueOf(Double.parseDouble(s.split("-")[1])).doubleValue());
            }
        } catch (NumberFormatException | NullPointerException e) {
            return 0d;
        }
    }

    public static int getInteger(java.lang.String s) {
        try {
            if (!s.contains("-")) {
                return BigDecimal.valueOf(Long.parseLong(s)).intValue();
            } else {
                return getRandomInteger(BigDecimal.valueOf(Long.parseLong(s.split("-")[0])).intValue(), BigDecimal.valueOf(Long.parseLong(s.split("-")[1])).intValue());
            }
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }

    public static long getLong(java.lang.String s) {
        try {
            if (!s.contains("-")) {
                return BigDecimal.valueOf(Long.parseLong(s)).longValue();
            } else {
                return getRandomLong(BigDecimal.valueOf(Long.parseLong(s.split("-")[0])).longValue(), BigDecimal.valueOf(Long.parseLong(s.split("-")[1])).longValue());
            }
        } catch (NumberFormatException | NullPointerException e) {
            return 0;
        }
    }
}
