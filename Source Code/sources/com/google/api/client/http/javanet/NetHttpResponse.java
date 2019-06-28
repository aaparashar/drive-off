package com.google.api.client.http.javanet;

import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.common.net.HttpHeaders;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

final class NetHttpResponse extends LowLevelHttpResponse {
    private final HttpURLConnection connection;
    private final ArrayList<String> headerNames = new ArrayList();
    private final ArrayList<String> headerValues = new ArrayList();
    private final int responseCode;
    private final String responseMessage;

    NetHttpResponse(HttpURLConnection connection) throws IOException {
        this.connection = connection;
        int responseCode = connection.getResponseCode();
        if (responseCode == -1) {
            responseCode = 0;
        }
        this.responseCode = responseCode;
        this.responseMessage = connection.getResponseMessage();
        List<String> headerNames = this.headerNames;
        List<String> headerValues = this.headerValues;
        for (Entry<String, List<String>> entry : connection.getHeaderFields().entrySet()) {
            String key = (String) entry.getKey();
            if (key != null) {
                for (String value : (List) entry.getValue()) {
                    if (value != null) {
                        headerNames.add(key);
                        headerValues.add(value);
                    }
                }
            }
        }
    }

    public int getStatusCode() {
        return this.responseCode;
    }

    public InputStream getContent() throws IOException {
        HttpURLConnection connection = this.connection;
        return HttpStatusCodes.isSuccess(this.responseCode) ? connection.getInputStream() : connection.getErrorStream();
    }

    public String getContentEncoding() {
        return this.connection.getContentEncoding();
    }

    public long getContentLength() {
        String string = this.connection.getHeaderField(HttpHeaders.CONTENT_LENGTH);
        return string == null ? -1 : Long.parseLong(string);
    }

    public String getContentType() {
        return this.connection.getHeaderField(HttpHeaders.CONTENT_TYPE);
    }

    public String getReasonPhrase() {
        return this.responseMessage;
    }

    public String getStatusLine() {
        String result = this.connection.getHeaderField(0);
        return (result == null || !result.startsWith("HTTP/1.")) ? null : result;
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

    public void disconnect() {
        this.connection.disconnect();
    }
}
