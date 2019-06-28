package com.google.api.client.googleapis;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.IOException;

@Deprecated
class MediaExponentialBackOffPolicy extends ExponentialBackOffPolicy {
    private final MediaHttpUploader uploader;

    MediaExponentialBackOffPolicy(MediaHttpUploader uploader) {
        this.uploader = uploader;
    }

    public long getNextBackOffMillis() {
        try {
            this.uploader.serverErrorCallback();
            return super.getNextBackOffMillis();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
