package com.project.socialnetwork.utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public final class TimeUtils {
    
    private static final int SECONDS_IN_A_MINUTE = 60;
    private static final int SECONDS_IN_AN_HOUR = 60 * 60;
    private static final int SECONDS_IN_A_DAY = 24 * 60 * 60;
    
    private TimeUtils() {
        // Utility class - prevent instantiation
    }
    
    public static String calculateTimeAgo(Date date) {
        if (date == null) {
            return "Just now";
        }
        
        long durationMillis = new Date().getTime() - date.getTime();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
        
        if (seconds < SECONDS_IN_A_MINUTE) {
            return "Just now";
        } else if (seconds < SECONDS_IN_AN_HOUR) {
            long minutes = seconds / SECONDS_IN_A_MINUTE;
            return minutes + " minutes ago";
        } else if (seconds < SECONDS_IN_A_DAY) {
            long hours = seconds / SECONDS_IN_AN_HOUR;
            return hours + " hours ago";
        } else {
            long days = seconds / SECONDS_IN_A_DAY;
            return days + " days ago";
        }
    }
}

