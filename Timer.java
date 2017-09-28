import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.function;

public class Grapher {

    static Random r = new Random(System.nanoTime());
    final static int TRIALS = 10000;
    final int[] GROUPS      = {10000, 20000, 30000, 40000, 50000};
    final int BUFFER_SIZE   = TRIALS * GROUPS.length;

    class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return "[ " + x + ", " + y + " ],";
        }
    }

    public static void main(String[] args) {
        Point[] linearResults    = generateLinearSearchData();
        Point[] binaryResults    = generateBinarySearchData();
        Point[] bubbleResults    = generateBubblesortData();
        Point[] exchangeResults  = generateExchangeSortData();
        Point[] selectionResults = generateSelectionSortData();

        writeFile("linear.json", linearResults);
        writeFile("binary.json", linearResults);
        writeFile("bubble.json", linearResults);
        writeFile("exchange.json", linearResults);
        writeFile("selection.json", linearResults);
    }

    private static void writeFile(String filename, Point[] data) {
        BufferedWriter w;
        try {
            File f = new File(filename);
            w = new BufferedWriter(new FileWriter(f));
            for(Point p : data) {
                w.write(p.toString());
                w.newLine();
            }
        } catch(Exception e) {
            w.flush();
        }
    }

    private static Point[] generateBinarySearchData() {
        Point[] results = new Point[BUFFER_SIZE];
        int count = 0;

        for (int groupSize : GROUPS) {
            int duration = (int) (timeBinarySearch(groupSize) / 1000);
            results[count] = new Point(groupSize, duration);
        }
    }

    private static int[] generateRandomArray(int size) {
        Integer[] array = new Integer[size];
        for(int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        List<Integer> random = Arrays.asList(array);
        Collections.shuffle(random);  
        return convert(array);
    }

    private static int[] generateSortedArray(int size) {
        Integer[] array = new Integer[size];
        for(int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        return convert(array);
    }

    private static int[] convert(Integer[] objArray) {
        int[] ints = new int[objArray.length];
        for(int i = 0; i < objArray.length; i++) {
            ints[i] = objArray[i].intValue();
        }
        return ints;
    }

    private static int time(BiFunction<int[], int, int> func, int a, int b) {
        long startTime = System.nanoTime();
        func.apply(a, b);
        long endTime = System.nanoTime();
        int duration = (int)(endTime - startTime);
        return duration;
    }

    private static Point[] generateLinearSearchData() {
        Point[] results = new Point[BUFFER_SIZE];
        int count = 0;

        for (int groupSize : GROUPS) {
            int duration = (int)(timeLinearSearch(groupSize) / 1000);
            results[count] = new Point(groupSize, duration);
            count++;
        }

        return results;
    }

    private static int linearSearch(Integer[] array, Integer value) {
        for(int i = 0; i < array.length; i++) {
            if (array[i].intValue() == value.intValue()) {
                return i;
            }
        }
        return -1;
    }

    // Run linear search once, testing to see how long it takes.
    // Returns the duration in nano seconds.
    private static long timeLinearSearch(int count) {
        Integer[] array = new Integer[count];
        for(int i = 0; i < array.length; i++) {
            array[i] = i;
        }
        List<Integer> random = Arrays.asList(array);
        Collections.shuffle(random);

        int toFind = r.nextInt(count);
        long startTime = System.nanoTime();
        linearSearch(random.toArray(array), toFind);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
