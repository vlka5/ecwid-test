import java.util.BitSet;
import java.util.stream.Stream;

public class UniqueIpAddressesCounter {
    private final BitSet positive = new BitSet();
    private final BitSet negative = new BitSet();
    private final Stream<String> lines;

    public UniqueIpAddressesCounter(Stream<String> lines) {
        this.lines = lines;
    }

    public long countUniqueAddresses() {
        System.out.println("Counting started..");
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