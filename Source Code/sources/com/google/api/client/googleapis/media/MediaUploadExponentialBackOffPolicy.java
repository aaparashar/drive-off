package com.google.api.client.googleapis.media;

import com.google.api.client.http.ExponentialBackOffPolicy;
import java.io.IOException;

class MediaUploadExponentialBackOffPolicy extends ExponentialBackOffPolicy {
    private final MediaHttpUploader uploader;

    MediaUploadExponentialBackOffPolicy(MediaHttpUploader uploader) {
        this.uploader = uploader;
    }

    public long getNextBackOffMillis() throws IOException {
        this.uploader.serverErrorCallback();
        return super.getNextBackOffMillis();
    }
}
