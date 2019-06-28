package com.google.api.client.http.javanet;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

final class NetHttpRequest extends LowLevelHttpRequest {
    private final HttpURLConnection connection;
    private HttpContent content;

    NetHttpRequest(String requestMethod, String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        this.connection = connection;
        connection.setRequestMethod(requestMethod);
        connection.setUseCaches(false);
        connection.setInstanceFollowRedirects(false);
    }

    public void addHeader(String name, String value) {
        this.connection.addRequestProperty(name, value);
    }

    public void setTimeout(int connectTimeout, int readTimeout) {
        this.connection.setReadTimeout(readTimeout);
        this.connection.setConnectTimeout(connectTimeout);
    }

    public LowLevelHttpResponse execute() throws IOException {
        HttpURLConnection connection = this.connection;
        if (this.content != null) {
            String contentType = this.content.getType();
            if (contentType != null) {
                addHeader(HttpHeaders.CONTENT_TYPE, contentType);
            }
            String contentEncoding = this.content.getEncoding();
            if (contentEncoding != null) {
                addHeader(HttpHeaders.CONTENT_ENCODING, contentEncoding);
            }
            long contentLength = this.content.getLength();
            if (contentLength >= 0) {
                addHeader(HttpHeaders.CONTENT_LENGTH, Long.toString(contentLength));
            }
            if (contentLength != 0) {
                connection.setDoOutput(true);
                if (contentLength < 0 || contentLength > 2147483647L) {
                    connection.setChunkedStreamingMode(0);
                } else {
                    connection.setFixedLengthStreamingMode((int) contentLength);
                }
                OutputStream out = connection.getOutputStream();
                try {
                    this.content.writeTo(out);
                } finally {
                    out.close();
                }
            }
        }
        boolean successfulConnection = false;
        try {
            connection.connect();
            NetHttpResponse response = new NetHttpResponse(connection);
            successfulConnection = true;
            return response;
        } finally {
            if (!successfulConnection) {
                connection.disconnect();
            }
        }
    }

    public void setContent(HttpContent content) {
        this.content = content;
    }
}
