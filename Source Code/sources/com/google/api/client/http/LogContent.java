package com.google.api.client.http;

import com.google.api.client.util.LoggingOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.logging.Level;

final class LogContent implements HttpContent {
    private final String contentEncoding;
    private final long contentLength;
    private final int contentLoggingLimit;
    private final String contentType;
    private final HttpContent httpContent;

    LogContent(HttpContent httpContent, String contentType, String contentEncoding, long contentLength, int contentLoggingLimit) {
        this.httpContent = httpContent;
        this.contentType = contentType;
        this.contentLength = contentLength;
        this.contentEncoding = contentEncoding;
        this.contentLoggingLimit = contentLoggingLimit;
    }

    public void writeTo(OutputStream out) throws IOException {
        LoggingOutputStream loggableOutputStream = new LoggingOutputStream(out, HttpTransport.LOGGER, Level.CONFIG, this.contentLoggingLimit);
        try {
            this.httpContent.writeTo(loggableOutputStream);
            out.flush();
        } finally {
            loggableOutputStream.getLogStream().close();
        }
    }

    public String getEncoding() {
        return this.contentEncoding;
    }

    public long getLength() {
        return this.contentLength;
    }

    public String getType() {
        return this.contentType;
    }

    int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public boolean retrySupported() {
        return this.httpContent.retrySupported();
    }
}
