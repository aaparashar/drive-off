package com.google.api.client.testing.http;

import com.google.api.client.http.HttpContent;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public class MockHttpContent implements HttpContent {
    private byte[] content = new byte[0];
    private String encoding;
    private long length = -1;
    private String type;

    public String getEncoding() {
        return this.encoding;
    }

    public long getLength() throws IOException {
        return this.length;
    }

    public String getType() {
        return this.type;
    }

    public void writeTo(OutputStream out) throws IOException {
        out.write(this.content);
        out.flush();
    }

    public boolean retrySupported() {
        return true;
    }

    public final byte[] getContent() {
        return this.content;
    }

    public MockHttpContent setContent(byte[] content) {
        this.content = (byte[]) Preconditions.checkNotNull(content);
        return this;
    }

    public MockHttpContent setEncoding(String encoding) {
        this.encoding = encoding;
        return this;
    }

    public MockHttpContent setLength(long length) {
        Preconditions.checkArgument(length >= -1);
        this.length = length;
        return this;
    }

    public MockHttpContent setType(String type) {
        this.type = type;
        return this;
    }
}
