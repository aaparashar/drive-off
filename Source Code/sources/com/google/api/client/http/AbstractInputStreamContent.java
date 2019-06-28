package com.google.api.client.http;

import com.google.common.io.ByteStreams;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbstractInputStreamContent implements HttpContent {
    private boolean closeInputStream = true;
    private String encoding;
    private String type;

    public abstract InputStream getInputStream() throws IOException;

    public AbstractInputStreamContent(String type) {
        setType(type);
    }

    public void writeTo(OutputStream out) throws IOException {
        copy(getInputStream(), out, this.closeInputStream);
        out.flush();
    }

    public String getEncoding() {
        return this.encoding;
    }

    public String getType() {
        return this.type;
    }

    public final boolean getCloseInputStream() {
        return this.closeInputStream;
    }

    public AbstractInputStreamContent setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public AbstractInputStreamContent setType(String type) {
        this.type = type;
        return this;
    }

    public AbstractInputStreamContent setCloseInputStream(boolean closeInputStream) {
        this.closeInputStream = closeInputStream;
        return this;
    }

    public static void copy(InputStream inputStream, OutputStream outputStream) throws IOException {
        copy(inputStream, outputStream, true);
    }

    public static void copy(InputStream inputStream, OutputStream outputStream, boolean closeInputStream) throws IOException {
        try {
            ByteStreams.copy(inputStream, outputStream);
        } finally {
            if (closeInputStream) {
                inputStream.close();
            }
        }
    }
}
