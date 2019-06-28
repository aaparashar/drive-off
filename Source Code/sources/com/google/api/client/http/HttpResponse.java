package com.google.api.client.http;

import com.google.api.client.util.LoggingInputStream;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Charsets;
import com.google.common.base.Preconditions;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

public final class HttpResponse {
    private InputStream content;
    private final String contentEncoding;
    private int contentLoggingLimit;
    private boolean contentRead;
    private final String contentType;
    private final HttpHeaders headers;
    private boolean loggingEnabled;
    private final HttpMediaType mediaType;
    private final HttpRequest request;
    LowLevelHttpResponse response;
    private final int statusCode;
    private final String statusMessage;
    private final HttpTransport transport;

    HttpResponse(HttpRequest request, LowLevelHttpResponse response) {
        StringBuilder stringBuilder;
        HttpMediaType httpMediaType = null;
        this.request = request;
        this.transport = request.getTransport();
        this.headers = request.getResponseHeaders();
        this.contentLoggingLimit = request.getContentLoggingLimit();
        this.loggingEnabled = request.isLoggingEnabled();
        this.response = response;
        this.contentEncoding = response.getContentEncoding();
        int code = response.getStatusCode();
        this.statusCode = code;
        String message = response.getReasonPhrase();
        this.statusMessage = message;
        Logger logger = HttpTransport.LOGGER;
        boolean loggable = this.loggingEnabled && logger.isLoggable(Level.CONFIG);
        StringBuilder logbuf = null;
        if (loggable) {
            logbuf = new StringBuilder();
            logbuf.append("-------------- RESPONSE --------------").append(StringUtils.LINE_SEPARATOR);
            String statusLine = response.getStatusLine();
            if (statusLine != null) {
                logbuf.append(statusLine);
            } else {
                logbuf.append(code);
                if (message != null) {
                    logbuf.append(' ').append(message);
                }
            }
            logbuf.append(StringUtils.LINE_SEPARATOR);
        }
        HttpHeaders httpHeaders = this.headers;
        if (loggable) {
            stringBuilder = logbuf;
        } else {
            stringBuilder = null;
        }
        httpHeaders.fromHttpResponse(response, stringBuilder);
        String contentType = response.getContentType();
        if (contentType == null) {
            contentType = this.headers.getContentType();
        }
        this.contentType = contentType;
        if (contentType != null) {
            httpMediaType = new HttpMediaType(contentType);
        }
        this.mediaType = httpMediaType;
        if (loggable) {
            logger.config(logbuf.toString());
        }
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public HttpResponse setContentLoggingLimit(int contentLoggingLimit) {
        Preconditions.checkArgument(contentLoggingLimit >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = contentLoggingLimit;
        return this;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public HttpResponse setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
        return this;
    }

    public String getContentEncoding() {
        return this.contentEncoding;
    }

    public String getContentType() {
        return this.contentType;
    }

    public HttpMediaType getMediaType() {
        return this.mediaType;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public boolean isSuccessStatusCode() {
        return HttpStatusCodes.isSuccess(this.statusCode);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public String getStatusMessage() {
        return this.statusMessage;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public HttpRequest getRequest() {
        return this.request;
    }

    public InputStream getContent() throws IOException {
        InputStream lowLevelResponseContent;
        Throwable th;
        if (!this.contentRead) {
            InputStream lowLevelResponseContent2 = this.response.getContent();
            if (lowLevelResponseContent2 != null) {
                try {
                    String contentEncoding = this.contentEncoding;
                    if (contentEncoding == null || !contentEncoding.contains("gzip")) {
                        lowLevelResponseContent = lowLevelResponseContent2;
                    } else {
                        lowLevelResponseContent = new GZIPInputStream(lowLevelResponseContent2);
                    }
                    try {
                        Logger logger = HttpTransport.LOGGER;
                        if (this.loggingEnabled && logger.isLoggable(Level.CONFIG)) {
                            lowLevelResponseContent2 = new LoggingInputStream(lowLevelResponseContent, logger, Level.CONFIG, this.contentLoggingLimit);
                        } else {
                            lowLevelResponseContent2 = lowLevelResponseContent;
                        }
                        this.content = lowLevelResponseContent2;
                        if (!true) {
                            lowLevelResponseContent2.close();
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        lowLevelResponseContent2 = lowLevelResponseContent;
                        if (!false) {
                            lowLevelResponseContent2.close();
                        }
                        throw th;
                    }
                } catch (Throwable th3) {
                    th = th3;
                    if (false) {
                        lowLevelResponseContent2.close();
                    }
                    throw th;
                }
            }
            this.contentRead = true;
        }
        return this.content;
    }

    public void download(OutputStream outputStream) throws IOException {
        AbstractInputStreamContent.copy(getContent(), outputStream);
    }

    public void ignore() throws IOException {
        InputStream content = getContent();
        if (content != null) {
            content.close();
        }
    }

    public void disconnect() throws IOException {
        ignore();
        this.response.disconnect();
    }

    @Deprecated
    public HttpParser getParser() {
        return this.request.getParser(this.contentType);
    }

    public <T> T parseAs(Class<T> dataClass) throws IOException {
        ObjectParser objectParser = this.request.getParser();
        if (objectParser != null) {
            return objectParser.parseAndClose(getContent(), getContentCharset(), (Class) dataClass);
        }
        HttpParser parser = getParser();
        if (parser != null) {
            return parser.parse(this, dataClass);
        }
        ignore();
        Preconditions.checkArgument(this.contentType != null, "Missing Content-Type header in response");
        throw new IllegalArgumentException("No parser defined for Content-Type: " + this.contentType);
    }

    public Object parseAs(Type dataType) throws IOException {
        ObjectParser objectParser = this.request.getParser();
        Preconditions.checkArgument(objectParser != null, "No ObjectParser defined for response");
        return objectParser.parseAndClose(getContent(), getContentCharset(), dataType);
    }

    public String parseAsString() throws IOException {
        InputStream content = getContent();
        if (content == null) {
            return "";
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        AbstractInputStreamContent.copy(content, out);
        return out.toString(getContentCharset().name());
    }

    public Charset getContentCharset() {
        return (this.mediaType == null || this.mediaType.getCharsetParameter() == null) ? Charsets.ISO_8859_1 : this.mediaType.getCharsetParameter();
    }
}
