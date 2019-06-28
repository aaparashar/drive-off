package com.google.api.client.http;

import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

public final class ByteArrayContent extends AbstractInputStreamContent {
    private final byte[] byteArray;
    private final int length;
    private final int offset;

    public ByteArrayContent(String type, byte[] array) {
        this(type, array, 0, array.length);
    }

    public ByteArrayContent(String type, byte[] array, int offset, int length) {
        super(type);
        this.byteArray = (byte[]) Preconditions.checkNotNull(array);
        boolean z = offset >= 0 && length >= 0 && offset + length <= array.length;
        Preconditions.checkArgument(z);
        this.offset = offset;
        this.length = length;
    }

    public static ByteArrayContent fromString(String type, String contentString) {
        return new ByteArrayContent(type, StringUtils.getBytesUtf8(contentString));
    }

    public long getLength() {
        return (long) this.length;
    }

    public boolean retrySupported() {
        return true;
    }

    public InputStream getInputStream() {
        return new ByteArrayInputStream(this.byteArray, this.offset, this.length);
    }

    public ByteArrayContent setEncoding(String encoding) {
        return (ByteArrayContent) super.setEncoding(encoding);
    }

    public ByteArrayContent setType(String type) {
        return (ByteArrayContent) super.setType(type);
    }

    public ByteArrayContent setCloseInputStream(boolean closeInputStream) {
        return (ByteArrayContent) super.setCloseInputStream(closeInputStream);
    }
}
