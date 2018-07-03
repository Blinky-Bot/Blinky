package me.tsblock.Blinky.utils;

import net.nullschool.util.DigitalRandom;

public class Utils {
    public static boolean isNaN(String str) {
        try {
            double d = Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
    }

    public static int randomNumber(int min, int max) {
        DigitalRandom random = new DigitalRandom();
        return (int) Math.abs(random.nextInt(min) - random.nextInt(max));
    }
}
