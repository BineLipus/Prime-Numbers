import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class main {
    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String last_prime_number = "0";
        String a = "";
        int step = 10000;

        File f = new File("prime_numbers.txt");
        if (f.exists() && !f.isDirectory()) {

            FileReader fr = new FileReader("prime_numbers.txt");
            BufferedReader br = new BufferedReader(fr);
            while ((a = br.readLine()) != null) {
                last_prime_number = a;
            }
            br.close();
            fr.close();

        }

        while (true)
        {
            List<String> prime_numbers = new ArrayList<String>();
            int i = Integer.parseInt(last_prime_number); // Last prime number found in file
            int k = ((i / step) * step);
            if (i % step != 0)
            {
                k += step;
            }
            i = k;
            int compute_to_num = i + step;
            if (i==0) i = 2;
            for (; i < compute_to_num; i++) {
                int number_of_dividers = 0;
                int sqrt = (int) Math.sqrt(i);
                for (int j = 1; j <= sqrt; j++) {
                    if (i % j == 0) {
                        number_of_dividers++;
                    }
                    if (number_of_dividers > 1) {
                        break;
                    }
                }
                if (number_of_dividers == 1) {
                    prime_numbers.add(Integer.toString(i));
                }
            }

            long end = System.currentTimeMillis();
            FileWriter fw = new FileWriter("log.txt", true); // True is for appending
            fw.write("Search for prime numbers from " + (compute_to_num - step) + " to " + compute_to_num + " completed in " + (end - start) + " milliseconds.");
            fw.write(" Found " + prime_numbers.size() + " prime numbers.\n");
            fw.close();
            start = System.currentTimeMillis();

            while (true)
            {
                try
                {
                    fw = new FileWriter("prime_numbers.txt", true); // True je za appendat.
                    break;
                }
                catch (Exception ex) {}
            }

            for (String prime_number : prime_numbers) {
                fw.write(prime_number + "\n");
                last_prime_number = prime_number;
            }
            fw.close();
            prime_numbers.clear();
        }
    }
}
