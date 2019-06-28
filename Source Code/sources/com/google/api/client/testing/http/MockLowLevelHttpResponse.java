package com.google.api.client.testing.http;

import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MockLowLevelHttpResponse extends LowLevelHttpResponse {
    private InputStream content;
    private String contentEncoding;
    private long contentLength = -1;
    private String contentType;
    private List<String> headerNames = new ArrayList();
    private List<String> headerValues = new ArrayList();
    private String reasonPhrase;
    private int statusCode = 200;

    public void addHeader(String name, String value) {
        this.headerNames.add(Preconditions.checkNotNull(name));
        this.headerValues.add(Preconditions.checkNotNull(value));
    }

    public MockLowLevelHttpResponse setContent(String stringContent) {
        if (stringContent == null) {
            this.content = null;
            setContentLength(0);
        } else {
            byte[] bytes = StringUtils.getBytesUtf8(stringContent);
            this.content = new ByteArrayInputStream(bytes);
            setContentLength((long) bytes.length);
        }
        return this;
    }

    public InputStream getContent() throws IOException {
        return this.content;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public long getContentLength() {
        return this.contentLength;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public int getHeaderCount() {
        return this.headerNames.size();
    }

    public String getHeaderName(int index) {
        return (String) this.headerNames.get(index);
    }

    public String getHeaderValue(int index) {
        return (String) this.headerValues.get(index);
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusLine() {
        StringBuilder buf = new StringBuilder(this.statusCode);
        if (this.reasonPhrase != null) {
            buf.append(this.reasonPhrase);
        }
        return buf.toString();
    }

    public final List<String> getHeaderNames() {
        return this.headerNames;
    }

    public MockLowLevelHttpResponse setHeaderNames(List<String> headerNames) {
        this.headerNames = (List) Preconditions.checkNotNull(headerNames);
        return this;
    }

    public final List<String> getHeaderValues() {
        return this.headerValues;
    }

    public MockLowLevelHttpResponse setHeaderValues(List<String> headerValues) {
        this.headerValues = (List) Preconditions.checkNotNull(headerValues);
        return this;
    }

    public MockLowLevelHttpResponse setContent(InputStream content) {
        this.content = content;
        return this;
    }

    public MockLowLevelHttpResponse setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public MockLowLevelHttpResponse setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
        return this;
    }

    public MockLowLevelHttpResponse setContentLength(long contentLength) {
        this.contentLength = contentLength;
        Preconditions.checkArgument(contentLength >= -1);
        return this;
    }

    public MockLowLevelHttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        Preconditions.checkArgument(statusCode >= 0);
        return this;
    }

    public MockLowLevelHttpResponse setReasonPhrase(String reasonPhrase) {
        this.reasonPhrase = reasonPhrase;
        return this;
    }
}
