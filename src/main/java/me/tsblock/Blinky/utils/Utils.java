package me.tsblock.Blinky.utils;

public class Utils {
    public static boolean isNaN(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }
}
