package com.google.api.client.googleapis.batch;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.List;

class MultipartMixedContent extends AbstractHttpContent {
    private static final String CR_LF = "\r\n";
    private static final String TWO_DASHES = "--";
    private List<RequestInfo<?, ?>> requestInfos;

    MultipartMixedContent(List<RequestInfo<?, ?>> requestInfos, String boundary) {
        super(new HttpMediaType("multipart/mixed").setParameter("boundary", (String) Preconditions.checkNotNull(boundary)));
        Preconditions.checkNotNull(requestInfos);
        Preconditions.checkArgument(!requestInfos.isEmpty());
        this.requestInfos = Collections.unmodifiableList(requestInfos);
    }

    private String getBoundary() {
        return getMediaType().getParameter("boundary");
    }

    public void writeTo(OutputStream out) throws IOException {
        int contentId = 1;
        Writer writer = new OutputStreamWriter(out);
        String boundary = getBoundary();
        for (RequestInfo<?, ?> requestInfo : this.requestInfos) {
            HttpRequest request = requestInfo.request;
            writer.write(TWO_DASHES);
            writer.write(boundary);
            writer.write(CR_LF);
            writer.write("Content-Type: application/http");
            writer.write(CR_LF);
            writer.write("Content-Transfer-Encoding: binary");
            writer.write(CR_LF);
            writer.write("Content-ID: ");
            int contentId2 = contentId + 1;
            writer.write(String.valueOf(contentId));
            writer.write(CR_LF);
            writer.write(CR_LF);
            writer.write(request.getMethod().toString());
            writer.write(" ");
            writer.write(request.getUrl().build());
            writer.write(CR_LF);
            HttpHeaders.serializeHeadersForMultipartRequests(request.getHeaders(), null, null, writer);
            HttpContent data = request.getContent();
            if (data != null) {
                writeHeader(writer, com.google.common.net.HttpHeaders.CONTENT_TYPE, data.getType());
                writeHeader(writer, com.google.common.net.HttpHeaders.CONTENT_LENGTH, String.valueOf(data.getLength()));
                writer.write(CR_LF);
                writer.flush();
                data.writeTo(out);
            }
            writer.write(CR_LF);
            contentId = contentId2;
        }
        writer.write(TWO_DASHES);
        writer.write(boundary);
        writer.write(TWO_DASHES);
        writer.write(CR_LF);
        writer.flush();
    }

    private void writeHeader(Writer writer, String name, String value) throws IOException {
        writer.write(name);
        writer.write(": ");
        writer.write(value);
        writer.write(CR_LF);
    }

    public MultipartMixedContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }
}
