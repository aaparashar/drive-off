package com.google.api.client.http;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.nio.charset.Charset;

public abstract class AbstractHttpContent implements HttpContent {
    private long computedLength;
    private HttpMediaType mediaType;

    @Deprecated
    protected AbstractHttpContent() {
        this.computedLength = -1;
    }

    protected AbstractHttpContent(String mediaType) {
        this(mediaType == null ? null : new HttpMediaType(mediaType));
    }

    protected AbstractHttpContent(HttpMediaType mediaType) {
        this.computedLength = -1;
        this.mediaType = mediaType;
    }

    public String getEncoding() {
        return null;
    }

    public long getLength() throws IOException {
        if (this.computedLength == -1) {
            this.computedLength = computeLength();
        }
        return this.computedLength;
    }

    public final HttpMediaType getMediaType() {
        return this.mediaType;
    }

    public AbstractHttpContent setMediaType(HttpMediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    protected final Charset getCharset() {
        return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.UTF_8 : this.mediaType.getCharsetParameter();
    }

    public String getType() {
        return this.mediaType == null ? null : this.mediaType.build();
    }

    protected long computeLength() throws IOException {
        if (!retrySupported()) {
            return -1;
        }
        ByteCountingOutputStream countingStream = new ByteCountingOutputStream();
        try {
            writeTo(countingStream);
            return countingStream.count;
        } finally {
            countingStream.close();
        }
    }

    public boolean retrySupported() {
        return true;
    }
}
