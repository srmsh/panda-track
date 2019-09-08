package org.track.store.http;

import okhttp3.*;
import okio.BufferedSink;
import okio.GzipSink;
import okio.Okio;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

public class GzipInterceptor implements Interceptor {

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request primaryRequest = chain.request();
        return chain.proceed(primaryRequest.newBuilder().header("Content-Encoding", "gzip")
                .method(primaryRequest.method(), gzip(primaryRequest.body())).build());
    }

    private RequestBody gzip(RequestBody body) {
        return new RequestBody() {
            @Nullable
            @Override
            public MediaType contentType() {
                return body.contentType();
            }

            @Override
            public long contentLength() throws IOException {
                return -1;
            }

            @Override
            public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
                BufferedSink gzipSink = Okio.buffer(new GzipSink(bufferedSink));
                body.writeTo(gzipSink);
                gzipSink.close();
            }
        };
    }
}
