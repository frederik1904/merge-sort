import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import sun.nio.ch.ThreadPool;

public class Sort {
    private ThreadPoolExecutor tp;
    private int THREADS = 8;

    /**
     * Returns the given array but sorted, sorts in ascending order.
     * @param array The array to sort
     * @return the sorted array ASC
     */    
    public int[] mergeSort(int[] array) throws InterruptedException {
        tp = (ThreadPoolExecutor) Executors.newFixedThreadPool(THREADS);
        workerThread[] wt = new workerThread[THREADS];
        
        for (int i = 0; i < THREADS; i++) {
            wt[i] = new workerThread(Arrays.copyOfRange(array, array.length / THREADS * i, array.length / THREADS * (i + 1)));
            tp.submit(wt[i]);
        }

        tp.shutdown();
        tp.awaitTermination(60, TimeUnit.MINUTES);

        int[] tmp = wt[0].array;

        for (int i = 1; i < THREADS; i++) {
            tmp = combine(tmp, wt[i].getArray());
        }
        
        return tmp;
    }

    private boolean compare(int a, int b) {
        return a < b;
    }

    private int[] combine(int[] left, int[] right) {
        int[] array = new int[left.length + right.length];

        int i = 0, j = 0;
            while (i < left.length && j < right.length && i + j < array.length) {
                if (compare(left[i], right[j])) {
                    array[i+j] = left[i];
                    i++;
                } else {
                    array[i+j] = right[j];
                    j++;
                }
        }
        for (; i < left.length; i++) {
            array[i+j] = left[i];
        }
        for (; j < right.length; j++) {
            array[i+j] = right[j];
        }
        return array;
    }

    private class workerThread implements Runnable {
        private int[] array;

        workerThread(int[] array) {
            this.array = array;
        }

        /**
         * @return the array
         */
        public int[] getArray() {
            return array;
        }

        int[] divide(int[] array) {
            if (array.length > 1) {
                int[] left = divide(Arrays.copyOfRange(array, 0, array.length / 2)),
                      right = divide(Arrays.copyOfRange(array, array.length / 2, array.length));
                array = combine(left, right);
            }
            return array;
        }
        public void run() {
            array = divide(array);
        }
    }
}

