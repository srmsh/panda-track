package org.track.store.http;

import com.google.gson.Gson;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.track.store.AsyncStore;
import org.track.store.model.LinkedSpan;
import org.track.store.model.SpanData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class HttpStore extends AsyncStore {

    private OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.SECONDS)
//            .addInterceptor(new GzipInterceptor())
            .build();

    private Gson gson = new Gson();

    private static final Semaphore semaphore = new Semaphore(0);

    private BlockingQueue<List<LinkedSpan>> reportQueue = new LinkedBlockingQueue<>();


    private String reportUrl = System.getProperty("reportUrl", "http://127.0.0.1:6060/panda/collect");

    public HttpStore() {
        new Thread(() -> {
            while (started) {
                try {
                    List<LinkedSpan> spans = reportQueue.take();

                    List<SpanData> spanDataList = new ArrayList<>();

                    spans.forEach(linkedSpan -> {
                        String traceId = UUID.randomUUID().toString();
                        while (linkedSpan.hasSpan()) {
                            SpanData data = linkedSpan.read();
                            data.setTraceId(traceId);
                            spanDataList.add(data);
                        }
                    });

                    Request.Builder builder = new Request.Builder();
                    RequestBody body = RequestBody.create(gson.toJson(spanDataList), MediaType.parse("application/json;"));
                    builder.url(reportUrl).post(body);
                    Call call = client.newCall(builder.build());
                    call.enqueue(callback);
                } catch (Throwable e) {
                    //
                }

            }
        }).start();
    }

    private Callback callback = new Callback() {
        @Override
        public void onFailure(@NotNull Call call, @NotNull IOException e) {
        }

        @Override
        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        }
    };

    private void drain(List<LinkedSpan> linkedSpans) {
        while (!queue.isEmpty() && linkedSpans.size() < 20) {
            try {
                linkedSpans.add(queue.poll(1, TimeUnit.SECONDS));
            } catch (InterruptedException e) {
                //容错
            }
        }
    }

    @Override
    public void report(LinkedSpan span) {
        List<LinkedSpan> linkedSpans = new ArrayList<>();
        linkedSpans.add(span);
        drain(linkedSpans);
        reportQueue.offer(linkedSpans);
    }
}
