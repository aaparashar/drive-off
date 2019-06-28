package com.google.api.client.http.apache;

import com.google.api.client.http.HttpContent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.apache.http.entity.AbstractHttpEntity;

final class ContentEntity extends AbstractHttpEntity {
    private final HttpContent content;
    private final long contentLength;

    ContentEntity(long contentLength, HttpContent content) {
        this.contentLength = contentLength;
        this.content = content;
    }

    public InputStream getContent() {
        throw new UnsupportedOperationException();
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public boolean isRepeatable() {
        return false;
    }

    public boolean isStreaming() {
        return true;
    }

    public void writeTo(OutputStream out) throws IOException {
        if (this.contentLength != 0) {
            this.content.writeTo(out);
        }
    }
}
