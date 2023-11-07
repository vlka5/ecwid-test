package counter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.BitSet;
import java.util.stream.Stream;

/**
 * Class for sync execution
 */
public class UniqueIpAddressesSyncCounter implements UniqueIpAddressesCounter {
    private final BitSet positive = new BitSet();
    private final BitSet negative = new BitSet();
    private final Stream<String> lines;

    public UniqueIpAddressesSyncCounter(String pathToFile) {
        Stream<String> lines1;
        try {
            lines1 = Files.lines(Path.of(pathToFile));
        } catch (IOException e) {
            System.err.println("Exception thrown while trying to open file: " + e.getMessage());
            lines1 = Stream.empty();
        }
        this.lines = lines1;
    }

    @Override
    public long countUniqueAddresses() {
        lines
                .map(this::getAddressAsInt)
                .forEach(intAddress -> {
                    if (intAddress >= 0)
                        positive.set(intAddress);
                    else
                        negative.set(~intAddress);
                });

        return positive.cardinality() + negative.cardinality();
    }

    public int getAddressAsInt(String line) {
        long result = 0;
        int octet = 0;

        for (char c : line.toCharArray()) {
            if (Character.isDigit(c)) {
                octet = octet * 10 + (c - '0');
            } else {
                result = (result << 8) | octet;
                octet = 0;
            }
        }

        return (int) (result << 8) | octet;
    }

}