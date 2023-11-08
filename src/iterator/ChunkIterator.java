package iterator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Iterator;

/**
 * Iterator over bytes of a file
 */
public class ChunkIterator implements Iterator<ByteBuffer> {

    private final FileChannel fileChannel;
    private final long size;
    private long lastEnd;
    private final long chunkSize;

    public ChunkIterator(FileChannel fileChannel) {
        try {
            this.size = fileChannel.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.fileChannel = fileChannel;
        this.lastEnd = 0;
        this.chunkSize = 1 << 22;
    }

    @Override
    public boolean hasNext() {
        return lastEnd < size;
    }

    @Override
    public ByteBuffer next() {
        long nextEnd = lastEnd + chunkSize;
        if (nextEnd >= size) {
            nextEnd = size;
        } else {
            nextEnd = calculateNextEnd(nextEnd);
        }

        try {
            long readSize = nextEnd - lastEnd;
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, lastEnd, readSize);
            lastEnd = nextEnd;
            return byteBuffer;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private long calculateNextEnd(long nextEnd) {
        try {
            long position = nextEnd - 16;
            ByteBuffer byteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, 16);
            for (int i = 0; i < byteBuffer.capacity(); i++) {
                char c = (char) byteBuffer.get(i);
                if (c == '\n') {
                    return position + i + 1;
                }
            }
            throw new IllegalArgumentException();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
