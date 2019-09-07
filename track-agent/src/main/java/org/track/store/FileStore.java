package org.track.store;

import org.track.store.model.LinkedSpan;
import org.track.store.model.SpanData;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class FileStore implements PandaStore {

    private static AsynchronousFileChannel fileChannel;

    private volatile boolean started = false;

    private static long projectedSize;

    private BlockingQueue<LinkedSpan> queue = new LinkedBlockingQueue<>();

    private static String filePathDefault = "./" + UUID.randomUUID().toString() + "-track.log";


    static {
        String filePath = System.getProperty("filePath", filePathDefault);

        try {
            fileChannel = AsynchronousFileChannel.open(Paths.get(filePath), StandardOpenOption.CREATE_NEW,
                    StandardOpenOption.READ, StandardOpenOption.WRITE);
            projectedSize = fileChannel.size();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(LinkedSpan data) {
        return queue.offer(data);
    }

    public void start() {
        started = true;
        new Thread(() -> {
            while (started) {
                try {

                    StringBuilder spanJson = new StringBuilder();

                    LinkedSpan span = queue.take();

                    String traceId = UUID.randomUUID().toString();

                    while (span.hasSpan()) {
                        SpanData data = span.read();
                        data.setTraceId(traceId);
                        spanJson.append(data.toString()).append("\n");
                    }

                    ByteBuffer buffer = ByteBuffer.wrap(spanJson.toString().getBytes());

                    fileChannel.write(buffer, projectedSize);

                    projectedSize += buffer.remaining();

                } catch (Throwable ignored) {
                    //防御性容错
                }
            }
        }).start();
    }
}
