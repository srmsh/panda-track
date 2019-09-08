package org.track.store;

import org.track.store.model.LinkedSpan;
import org.track.store.model.SpanData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;

public class FileStore extends AsyncStore {

    private static AsynchronousFileChannel fileChannel;

    private static long projectedSize;

    private static String filePathDefault = "./" + UUID.randomUUID().toString() + "-track.log";


    static {
        String filePath = System.getProperty("filePath", filePathDefault);

        try {
            fileChannel = AsynchronousFileChannel.open(Paths.get(filePath), StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.READ, StandardOpenOption.WRITE);
            projectedSize = fileChannel.size();
        } catch (IOException ignored) {
        }
    }

    @Override
    public void report(LinkedSpan span) {
        StringBuilder spanJson = new StringBuilder();

        String traceId = UUID.randomUUID().toString();

        while (span.hasSpan()) {
            SpanData data = span.read();
            data.setTraceId(traceId);
            spanJson.append(data.toString()).append("\n");
        }

        ByteBuffer buffer = ByteBuffer.wrap(spanJson.toString().getBytes());

        fileChannel.write(buffer, projectedSize);

        projectedSize += buffer.remaining();
    }
}
