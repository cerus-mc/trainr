package xyz.trainr.trainr.util;

public class StringFormatUtil {

    private StringFormatUtil() {
    }

    public static String formatMillis(long timeInMillis) {
        if (timeInMillis == -1) {
            return "N/A";
        }

        long minutes = (timeInMillis / 1000) / 60;
        long seconds = (timeInMillis / 1000) % 60;
        long millis = timeInMillis % 1000;
        return String.format("%02dm %02ds %03dms", minutes, seconds, millis);
    }

    public static String formatMillisShort(long timeInMillis) {
        if (timeInMillis == -1) {
            return "N/A";
        }

        long seconds = (timeInMillis / 1000) % 60;
        long millis = timeInMillis % 1000;
        return String.format("%02d.%ss", seconds, String.format("%.1f", millis / 1000f).split("\\.")[1]);
    }

}
