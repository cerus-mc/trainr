package xyz.trainr.trainr.util;

public class StringFormatUtil {

    private StringFormatUtil() {
    }

    public static String formatMillis(long timeInMillis) {
        if(timeInMillis == -1) {
            return "NaN";
        }

        long minutes = (timeInMillis / 1000) / 60;
        long seconds = (timeInMillis / 1000) % 60;
        long millis = timeInMillis % 1000;
        return String.format("%02dm %02ds %03dms", minutes, seconds, millis);
    }

}
