package counter;

import iterator.ChunkIterator;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import storage.IpAddressesStorage;
import task.AddressesCounterTask;

public class UniqueIpAddressesAsyncCounter implements UniqueIpAddressesCounter {

    private final String pathToFile;
    private final IpAddressesStorage storage;
    private final ExecutorService executorService;

    public UniqueIpAddressesAsyncCounter(String pathToFile) {
        this.pathToFile = pathToFile;
        this.storage = new IpAddressesStorage();
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    @Override
    public long countUniqueAddresses() {
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(pathToFile, "r");
             FileChannel fc = randomAccessFile.getChannel();
        ) {
            ChunkIterator chunkIterator = new ChunkIterator(fc);
            while (chunkIterator.hasNext()) {
                ByteBuffer next = chunkIterator.next();
                executorService.submit(new AddressesCounterTask(storage, next));
            }
        } catch (IOException e) {
            System.err.println("Exception thrown while processing file: " + e.getMessage());
        }

        shutdownExecutor();

        return storage.result();
    }

    private void shutdownExecutor() {
        executorService.shutdown();
        try {
            while (!executorService.awaitTermination(10, TimeUnit.MINUTES)) {
                System.out.println("[async] time limit exceeded, executor shutdown");
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            System.err.println("Exception thrown while executor shutdown: " + e.getMessage());
        }
    }

}
