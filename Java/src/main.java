import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class main extends Thread {
    private Lock lock = new ReentrantLock();
    private static String last_prime_number = "0";
    private static List<String> prime_numbers;
    private static long step = 50000;
    private static List<main> threads;
    private long thread_param_i = 0;
    private long thread_param_compute_to_num = 0;

    public main(long thread_param_i, long thread_param_compute_to_num) {
        this.thread_param_i = thread_param_i;
        this.thread_param_compute_to_num = thread_param_compute_to_num;
    }

    public static void main(String[] args) throws IOException {
        try {
            readFile();

            int num_of_threads = Runtime.getRuntime().availableProcessors();
            System.out.println("Number of availabe threads: " + num_of_threads);
            threads = new ArrayList<>();
            for (int i = 0; i < num_of_threads; i++) {
                threads.add(new main(0, 0));
            }

            while (true) {
                long start = System.currentTimeMillis();
                long compute_to_num = computePrimeNumbers();
                long end = System.currentTimeMillis();
                writeFile_Log(compute_to_num, start, end);
                writeFile_PrimeNumbers();
            }
        } catch (Exception ex) {
            FileWriter fw = new FileWriter("error.txt", true);
            fw.write("Error: " + ex.toString() + "\n");
            fw.close();
        }
    }

    public void run() {
        long i = this.thread_param_i;
        long compute_to_num = this.thread_param_compute_to_num;
        List<String> prime_numbers_temp = new ArrayList<String>();
        //System.out.println("New Thread was ran.");
        if (i == 0) i = 2;
        for (; i < compute_to_num; i = i + 1) {
            int number_of_dividers = 0;
            if (i % 2 == 0 && i != 2) continue; // If the number is even, we know that it can be divided with 1
            // and 2, thus it is not a prime number except if it is a number 2.
            long sqrt = (long) Math.sqrt(i);
            for (long j = 1; j <= sqrt; j += 2) {
                // We can increment divider by 2, since none of prime numbers can be divided with even number,
                // except prime number 2.
                if (i % j == 0) {
                    number_of_dividers++;
                }
                if (number_of_dividers > 1) {
                    break;
                }
            }
            if (number_of_dividers == 1) {
                prime_numbers_temp.add(Long.toString(i));
            }
        }
        lock.lock();
        try{
            prime_numbers.addAll(prime_numbers_temp);
        }
        catch (Exception ex)
        {
            int a = 0;
        }
        finally {
            lock.unlock();
        }

    }

    private static void readFile() {
        try {
            File f = new File("mnt/prime_numbers.txt");
            if (f.exists() && !f.isDirectory()) {

                /*FileReader fr = new FileReader("mnt/prime_numbers.txt");
                BufferedReader br = new BufferedReader(fr);
                String last_prime_number_in_file;
                while ((last_prime_number_in_file = br.readLine()) != null) {
                    last_prime_number = last_prime_number_in_file;
                }
                br.close();
                fr.close();*/
                RandomAccessFile raf = new RandomAccessFile("mnt/prime_numbers.txt", "r");
                raf.seek(raf.length()-100);

                String last_prime_number_in_file;
                while ((last_prime_number_in_file = raf.readLine()) != null) {
                    last_prime_number = last_prime_number_in_file;
                }
                raf.close();
            }
        } catch (Exception ex) {
            // Do nothing
        }
    }

    private static long computePrimeNumbers() {
        prime_numbers = new ArrayList<String>();

        long i = Long.parseLong(last_prime_number); // Last prime number found in file
        if (i % step != 0) {
            i = ((i / step) * step);
            i += step;
        } else {
            i = ((i / step) * step);
        }
        long compute_to_num = 0;

        // Replace old "used" threads with new "unused" ones.
        for (int id = 0; id < threads.size(); id++) {
            compute_to_num = i + (step / threads.size());
            threads.remove(0);
            threads.add(new main(i, compute_to_num)); // Creates a new thread.
            i = compute_to_num;
        }

        for (main thread : threads) {
            thread.start();
        }

        for (main thread : threads) {
            while (thread.isAlive()) {
                // Wait for all threads to be fully executed before execution of main thread can be continued.
            }
        }
        //Collections.sort(prime_numbers, new StringByLengthAndAlphabetComparator()); // Tukaj je problem z NullPointerException
        return compute_to_num;
    }

    private static void writeFile_Log(long compute_to_num, long start, long end) {
        try {
            FileWriter fw = new FileWriter("mnt/log.txt", true); // True is for appending
            fw.write("Search for prime numbers from " + (compute_to_num - step) + " to " + compute_to_num + " completed in " + (end - start) + " milliseconds.");
            fw.write(" Found " + prime_numbers.size() + " prime numbers.\n");
            fw.close();
        } catch (Exception ex) {
            // Do nothing
        }
    }

    private static void writeFile_PrimeNumbers() {
        try {
            FileWriter fw;
            while (true) {
                try {
                    fw = new FileWriter("mnt/prime_numbers.txt", true); // True je za appendat.
                    break;
                } catch (Exception ex) {
                    // Do nothing
                }
            }

            for (String prime_number : prime_numbers) {
                fw.write(prime_number + "\n");
                last_prime_number = prime_number;
            }
            fw.close();
            prime_numbers.clear();
        } catch (Exception ex) {
            // Do nothing
        }
    }
}

