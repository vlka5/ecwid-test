package storage;

import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.IntStream;

/**
 * Wrapper around AtomicIntegerArray to store int values as bits
 */
public class IpAddressesStorage {

    private final AtomicIntegerArray storage;

    public IpAddressesStorage() {
        this.storage = new AtomicIntegerArray(1 << 27);
    }

    public void set(int value) {
        var bucketIndex = value >>> 5;
        var bitIndex = value & 31;
        var bitToSet = 1 << bitIndex;
        storage.updateAndGet(bucketIndex, v -> v | bitToSet);
    }

    public long result() {
        return IntStream
                .range(0, storage.length())
                .parallel()
                .mapToLong(i -> Integer.bitCount(storage.get(i)))
                .sum();
    }
}
