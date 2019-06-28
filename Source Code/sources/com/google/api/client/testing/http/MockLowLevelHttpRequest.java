package com.google.api.client.testing.http;

import com.google.api.client.http.HttpContent;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MockLowLevelHttpRequest extends LowLevelHttpRequest {
    private HttpContent content;
    private final Map<String, List<String>> headersMap = new HashMap();
    private MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
    private String url;

    public MockLowLevelHttpRequest(String url) {
        this.url = url;
    }

    public void addHeader(String name, String value) {
        List<String> values = (List) this.headersMap.get(name);
        if (values == null) {
            values = new ArrayList();
            this.headersMap.put(name, values);
        }
        values.add(value);
    }

    public LowLevelHttpResponse execute() throws IOException {
        return this.response;
    }

    public void setContent(HttpContent content) throws IOException {
        this.content = content;
    }

    public String getUrl() {
        return this.url;
    }

    public Map<String, List<String>> getHeaders() {
        return this.headersMap;
    }

    public MockLowLevelHttpRequest setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpContent getContent() {
        return this.content;
    }

    public MockLowLevelHttpResponse getResponse() {
        return this.response;
    }

    public MockLowLevelHttpRequest setResponse(MockLowLevelHttpResponse response) {
        this.response = response;
        return this;
    }
}
