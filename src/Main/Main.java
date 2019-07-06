import java.util.Arrays;
import java.util.Random;

public class Main{
    public static void main(String[] args) throws InterruptedException {
        Random r = new Random();

        int [] array = new int[200];
        for (int i = 0; i < array.length; i++) 
            array[i] = r.nextInt(200) + 1;
        Sort s = new Sort();
        int[] sorted = s.mergeSort(array);
        System.out.println(Arrays.toString(sorted));

    }
}
