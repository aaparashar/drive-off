package com.google.api.client.http;

import com.google.api.client.util.StringUtils;
import java.io.IOException;

public class HttpResponseException extends IOException {
    private static final long serialVersionUID = -1875819453475890043L;
    private final transient HttpHeaders headers;
    private final int statusCode;
    private final String statusMessage;

    public HttpResponseException(HttpResponse response) {
        this(response, computeMessageWithContent(response));
    }

    public HttpResponseException(HttpResponse response, String message) {
        super(message);
        this.statusCode = response.getStatusCode();
        this.statusMessage = response.getStatusMessage();
        this.headers = response.getHeaders();
    }

    public boolean isSuccessStatusCode() {
        return HttpStatusCodes.isSuccess(this.statusCode);
    }

    public int getStatusCode() {
        return this.statusCode;
    }

    public final String getStatusMessage() {
        return this.statusMessage;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    private static String computeMessageWithContent(HttpResponse response) {
        StringBuilder builder = computeMessageBuffer(response);
        String content = "";
        try {
            content = response.parseAsString();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        if (content.length() != 0) {
            builder.append(StringUtils.LINE_SEPARATOR).append(content);
        }
        return builder.toString();
    }

    public static StringBuilder computeMessageBuffer(HttpResponse response) {
        StringBuilder builder = new StringBuilder();
        int statusCode = response.getStatusCode();
        if (statusCode != 0) {
            builder.append(statusCode);
        }
        String statusMessage = response.getStatusMessage();
        if (statusMessage != null) {
            if (statusCode != 0) {
                builder.append(' ');
            }
            builder.append(statusMessage);
        }
        return builder;
    }
}
