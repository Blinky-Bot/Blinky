import java.util.Random;

public class test {
    public static void main(String[] args) {
        for(int i=0;i<100;i++) {
            System.out.println(randomNumber(5, 140));
        }
    }
    public static int randomNumber(int min, int max) {
        Random random = new Random();
        return (int) Math.abs(random.nextInt(5) - random.nextInt(140));
    }
}
