import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;

public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("File path is not specified");
        } else {
            countUniqueAddresses(args[0]);
        }
    }

    private static void countUniqueAddresses(String pathToFile) {
        var path = Path.of(pathToFile);
        var start = System.currentTimeMillis();
        try (var lines = Files.lines(path)) {
            var counter = new UniqueIpAddressesCounter(lines);
            System.out.printf("Answer is %d\n", counter.countUniqueAddresses());
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        printExecutionTime(start);
    }

    private static void printExecutionTime(long start) {
        Duration executionDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        System.out.printf("Execution time is %d:%d\n", executionDuration.toMinutesPart(), executionDuration.toSecondsPart());
    }
}