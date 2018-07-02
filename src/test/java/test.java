import net.nullschool.util.DigitalRandom;

import java.util.Random;

public class test {
    public static void main(String[] args) {
        for(int i=0;i<100;i++) {
            System.out.println(randomNumber(5, 140));
        }
    }
    public static int randomNumber(int min, int max) {
        double Drandom = new DigitalRandom().nextDouble();
        return (int) Math.floor(min + (max + 1 - min) * (Math.pow(Drandom, 6.5)));
    }
}
