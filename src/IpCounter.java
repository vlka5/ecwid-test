import java.io.BufferedReader;
import java.io.IOException;
import java.util.BitSet;

public class IpCounter {
    private final BitSet addressesStoreBeforeMaxInt = new BitSet();
    private final BitSet addressesStoreAfterMaxInt = new BitSet();
    private final BufferedReader bufferedReader;

    public IpCounter(BufferedReader reader) {
        this.bufferedReader = reader;
    }

    public long countUniqueAddresses() {
        try {
            String line;
            BitSet storeToPut = addressesStoreBeforeMaxInt;

            while ((line = bufferedReader.readLine()) != null) {
                long addressAsLong = getAddressAsLong(line);
                int addressToPut = (int) addressAsLong;

                if (addressAsLong > Integer.MAX_VALUE) {
                    addressToPut = (int) (addressAsLong - Integer.MAX_VALUE);
                    storeToPut = addressesStoreAfterMaxInt;
                }

                if (!storeToPut.get(addressToPut)) {
                    storeToPut.set(addressToPut);
                }
            }
        } catch (IOException e) {
            System.out.printf("Exception thrown while reading a file: %s", e.getMessage());
        }

        return addressesStoreAfterMaxInt.cardinality() + addressesStoreBeforeMaxInt.cardinality();
    }

    private long getAddressAsLong(String line) {
        long result = 0;
        String[] bytes = line.split("\\.");
        for (int i = 0; i < bytes.length; i++) {
            result += Long.parseLong(bytes[i]) << (24 - (i * 8));
        }

        return result;
    }
}