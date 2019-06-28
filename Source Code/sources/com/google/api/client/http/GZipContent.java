package com.google.api.client.http;

import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.GZIPOutputStream;

final class GZipContent extends AbstractHttpContent {
    private final HttpContent httpContent;

    GZipContent(HttpContent httpContent, String contentType) {
        super(contentType);
        this.httpContent = httpContent;
    }

    public void writeTo(OutputStream out) throws IOException {
        GZIPOutputStream zipper = new GZIPOutputStream(out);
        this.httpContent.writeTo(zipper);
        zipper.close();
    }

    public String getEncoding() {
        return "gzip";
    }

    public boolean retrySupported() {
        return this.httpContent.retrySupported();
    }

    public GZipContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }
}
