import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.function.BiFunction;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.io.BufferedWriter;

public class Timer {

    static Random r                = new Random(System.nanoTime());
    final static int TRIALS        = 5000;
    final static int[] GROUPS      = {10000, 20000, 30000, 40000, 50000};
    final static int BUFFER_SIZE   = TRIALS * GROUPS.length;

    static BiFunction<int[], Integer, Integer> LinearSearch = 
        (a, b) -> linearSearch(a, b);
    static BiFunction<int[], Integer, Integer> BinarySearch = 
        (a, b) -> binarySearch(a, b);
    static BiFunction<int[], Integer, Integer> BubbleSort = 
        (a, b) -> bubbleSort(a);
    static BiFunction<int[], Integer, Integer> ExchangeSort = 
        (a, b) -> exchangeSort(a);
    static BiFunction<int[], Integer, Integer> Fib = 
        (a, b) -> fib(b);

    public static class Point {
        int x, y;

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
        public String toString() {
            return "[ " + x + ", " + y + " ],";
        }
    }

    public static void main(String[] args) throws IOException {
        System.out.println("Running linear search tests.");
        List<Point> linearResults    = generateLinearSearchData();
        writeFile("linear.json",   linearResults);
        
        
        System.out.println("Running binary search tests.");
        List<Point> binaryResults    = generateBinarySearchData();
        writeFile("binary.json",   binaryResults);
        
        System.out.println("Running bubble sort tests.");
        List<Point> bubbleResults    = generateBubbleSortData();
        writeFile("bubble.json",   bubbleResults);

        System.out.println("Running exchange sort tests.");
        List<Point> exchangeResults  = generateExchangeSortData();
        writeFile("exchange.json", exchangeResults);
        
        System.out.println("Running fibonacci tests.");
        List<Point> fibResults       = generateFibData();
        writeFile("fib.json",      fibResults);
    }

    private static void writeFile(String filename, List<Point> data) throws IOException {
        BufferedWriter w = null;
        File f = new File(filename);
        w = new BufferedWriter(new FileWriter(f));
        for(Point p : data) {
            w.write(p.toString());
            w.newLine();
        }
        w.flush();
    }

    private static List<Point> generateFibData() {
        List<Point> results = new LinkedList<>();

        int[] sizes = new int[]{ 10, 20, 30, 40, 50};
        for (int n : sizes) {
            System.out.printf("Collecting data for fib(%d)\n", n);
            for(int i = 0; i < TRIALS; i++) {
                if (i % 50 == 0) {
                    System.out.printf("Fib(%d): trial %d\n", n, i);
                }
                int duration = time(Fib, sizes, n);
                results.add(new Point(n, duration));
            }
        }

        return results;
    }
    private static List<Point> generateLinearSearchData() {
        List<Point> results = new LinkedList<>();

        for (int groupSize : GROUPS) {
            System.out.printf("Starting group %d\n", groupSize);
            for(int i = 0; i < TRIALS; i++) {
                if(i % 50 == 0) {
                    System.out.printf("Linear Search: int[%d], trial %d\n", groupSize, i);
                }
                int[] data = generateRandomArray(groupSize);
                int valueToFind = r.nextInt(groupSize);
                int duration = time(LinearSearch, data, valueToFind);
                results.add(new Point(groupSize, duration));
            }
        }

        return results;
    }

    private static List<Point> generateBubbleSortData() {
        List<Point> results = new LinkedList<>();

        for (int groupSize : GROUPS) {
            System.out.printf("Starting group %d\n", groupSize);
            for(int i = 0; i < TRIALS; i++) {
                if(i % 50 == 0) {
                    System.out.printf("Bubblesort: int[%d], trial %d\n", groupSize, i);
                }

                int[] data = generateRandomArray(groupSize / 10);
                int duration = time(BubbleSort, data, 0);
                results.add(new Point(groupSize, duration));
            }
        }

        return results;
    }

    private static List<Point> generateExchangeSortData() {
        List<Point> results = new LinkedList<>();

        for (int groupSize : GROUPS) {
            System.out.printf("Starting group %d\n", groupSize);
            for(int i = 0; i < TRIALS; i++) {
                if(i % 50 == 0) {
                    System.out.printf("Exchange Sort: int[%d], trial %d\n", groupSize, i);
                }
                int[] data = generateRandomArray(groupSize / 10);
                int duration = time(ExchangeSort, data, 0);
                results.add(new Point(groupSize, duration));
            }
        }

        return results;
    }


    private static List<Point> generateBinarySearchData() {
        List<Point> results = new LinkedList<>();

        for (int groupSize : GROUPS) {
            System.out.printf("Starting group %d\n", groupSize);
            for(int i = 0; i < TRIALS; i++) {
                if(i % 50 == 0) {
                    System.out.printf("Binary Search: int[%d], trial %d\n", groupSize, i);
                    System.out.printf("Starting trial %d\n", i);
                }
                int[] data = generateSortedArray(groupSize);
                int valueToFind = r.nextInt(groupSize);
                int duration = time(BinarySearch, data, valueToFind);
                results.add(new Point(groupSize, duration));
            }
        }

        return results;
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

    private static int time(BiFunction<int[], Integer, Integer> func, int[] data, int value) {
        long startTime = System.nanoTime();
        func.apply(data, value);
        long endTime = System.nanoTime();
        int duration = (int)(endTime - startTime);
        return duration;
    }

    private static int linearSearch(int[] array, int value) {
        for(int i = 0; i < array.length; i++) {
            if (array[i] == value) {
                return i;
            }
        }
        return -1;
    }

    private static int binarySearch(int[] array, int value) {
        return Arrays.binarySearch(array, value);
    }

    private static int bubbleSort(int[] array) {
        for(int i = 0; i < array.length; i++) {
            for(int j = 1; j < array.length; j++) {
                if (array[j-1] < array[j]) {
                    swap(array, i, j);
                }
            }
        }
        return 0;
    }

    private static int exchangeSort(int[] array) {
        for(int i = 0; i < array.length; i++) {
            for(int j = 0; j < array.length; j++) {
                if (array[i] < array[j]) {
                    swap(array, i, j);
                }
            }
        }
        return 0;
    }


    private static void swap(int[] array, int x, int y) {
        int temp = array[x];
        array[x] = array[y];
        array[y] = temp;
    }

    private static int fib(int n) {
        if (n < 2) return n;
        return fib(n-1) + fib(n-2);
    }
}
