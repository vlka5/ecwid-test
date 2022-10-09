import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        String pathToFile;

        if (args.length == 0) {
            System.out.println("File path is not specified");
            return;
        } else {
            pathToFile = args[0];
        }

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathToFile)))  {
            IpCounter counter = new IpCounter(bufferedReader);
            long startTime = System.currentTimeMillis();
            System.out.printf("Count of unique addresses: %d\n", counter.countUniqueAddresses());
            System.out.printf("Time spent: %d", (System.currentTimeMillis() - startTime) / 1000L);
        } catch (FileNotFoundException e) {
            System.out.printf("File with path %s was not found", pathToFile);
        } catch (IOException e) {
            System.out.printf("Something went wrong while processing the file: %s", e.getMessage());
        }
    }
}