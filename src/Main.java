import counter.UniqueIpAddressesAsyncCounter;
import counter.UniqueIpAddressesCounter;
import counter.UniqueIpAddressesSyncCounter;
import java.time.Duration;

/**
 * Entrypoint of an app
 */
public class Main {
    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("File path is not specified");
        } else {
//            countUniqueAddressesSync(args[0]);
            countUniqueAddressesAsync(args[0]);
        }
    }

    private static void countUniqueAddressesSync(String pathToFile) {
        var start = System.currentTimeMillis();
        System.out.println("[sync] Counting started..");

        UniqueIpAddressesCounter addressesCounter = new UniqueIpAddressesSyncCounter(pathToFile);
        System.out.printf("[sync] Answer is %d\n", addressesCounter.countUniqueAddresses());

        printExecutionTime(start);
    }

    private static void countUniqueAddressesAsync(String pathToFile) {
        long start = System.currentTimeMillis();
        System.out.println("[async] Counting started..");

        UniqueIpAddressesCounter addressesCounter = new UniqueIpAddressesAsyncCounter(pathToFile);
        System.out.printf("[async] Answer is %d\n", addressesCounter.countUniqueAddresses());

        printAsyncExecutionTime(start);
    }

    private static void printExecutionTime(long start) {
        Duration executionDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        System.out.printf("[sync] Execution time is %d:%d\n", executionDuration.toMinutesPart(), executionDuration.toSecondsPart());
    }

    private static void printAsyncExecutionTime(long start) {
        Duration executionDuration = Duration.ofMillis(System.currentTimeMillis() - start);
        System.out.printf("[async] execution time is %d:%d\n", executionDuration.toMinutesPart(), executionDuration.toSecondsPart());
    }
}