package task;

import java.nio.ByteBuffer;
import storage.IpAddressesStorage;

/**
 * Task that scans ByteBuffer for ip addresses and puts them into storage
 */
public class AddressesCounterTask implements Runnable {

    private final IpAddressesStorage storage;
    private final ByteBuffer byteBuffer;

    public AddressesCounterTask(IpAddressesStorage storage, ByteBuffer byteBuffer) {
        this.storage = storage;
        this.byteBuffer = byteBuffer;
    }

    @Override
    public void run() {
        long result = 0;
        int octet = 0;

        while (byteBuffer.hasRemaining()) {
            char c = (char) byteBuffer.get();

            if (Character.isDigit(c)) {
                octet = octet * 10 + (c - '0');
            } else if (c == '.') {
                result = (result << 8) | octet;
                octet = 0;
            } else if (c == '\n') {
                storage.set((int) (result << 8) | octet);
                result = 0;
                octet = 0;
            }
        }

        if (result != 0) {
            storage.set((int) (result << 8) | octet);
        }
    }
}
