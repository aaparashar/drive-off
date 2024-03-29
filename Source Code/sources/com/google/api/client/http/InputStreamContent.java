package com.google.api.client.http;

import com.google.common.base.Preconditions;
import java.io.InputStream;

public final class InputStreamContent extends AbstractInputStreamContent {
    private final InputStream inputStream;
    private long length = -1;
    private boolean retrySupported;

    public InputStreamContent(String type, InputStream inputStream) {
        super(type);
        this.inputStream = (InputStream) Preconditions.checkNotNull(inputStream);
    }

    public long getLength() {
        return this.length;
    }

    public boolean retrySupported() {
        return this.retrySupported;
    }

    public InputStreamContent setRetrySupported(boolean retrySupported) {
        this.retrySupported = retrySupported;
        return this;
    }

    public InputStream getInputStream() {
        return this.inputStream;
    }

    public InputStreamContent setEncoding(String encoding) {
        return (InputStreamContent) super.setEncoding(encoding);
    }

    public InputStreamContent setType(String type) {
        return (InputStreamContent) super.setType(type);
    }

    public InputStreamContent setCloseInputStream(boolean closeInputStream) {
        return (InputStreamContent) super.setCloseInputStream(closeInputStream);
    }

    public InputStreamContent setLength(long length) {
        this.length = length;
        return this;
    }
}
