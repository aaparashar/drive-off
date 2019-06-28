package com.google.api.client.http.json;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.UriTemplate;
import com.google.api.client.util.GenericData;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class JsonHttpRequest extends GenericData {
    private final JsonHttpClient client;
    private final Object content;
    private boolean enableGZipContent = true;
    private HttpHeaders lastResponseHeaders;
    private final HttpMethod method;
    private HttpHeaders requestHeaders = new HttpHeaders();
    private final String uriTemplate;

    public JsonHttpRequest(JsonHttpClient client, HttpMethod method, String uriTemplate, Object content) {
        this.client = (JsonHttpClient) Preconditions.checkNotNull(client);
        this.method = (HttpMethod) Preconditions.checkNotNull(method);
        this.uriTemplate = (String) Preconditions.checkNotNull(uriTemplate);
        this.content = content;
    }

    public JsonHttpRequest setEnableGZipContent(boolean enableGZipContent) {
        this.enableGZipContent = enableGZipContent;
        return this;
    }

    public final boolean getEnableGZipContent() {
        return this.enableGZipContent;
    }

    public final HttpMethod getMethod() {
        return this.method;
    }

    public final String getUriTemplate() {
        return this.uriTemplate;
    }

    public final Object getJsonContent() {
        return this.content;
    }

    public final JsonHttpClient getClient() {
        return this.client;
    }

    public JsonHttpRequest setRequestHeaders(HttpHeaders headers) {
        this.requestHeaders = headers;
        return this;
    }

    public final HttpHeaders getRequestHeaders() {
        return this.requestHeaders;
    }

    public final HttpHeaders getLastResponseHeaders() {
        return this.lastResponseHeaders;
    }

    public final GenericUrl buildHttpRequestUrl() {
        String baseUrl;
        if (getClient().isBaseUrlUsed()) {
            baseUrl = getClient().getBaseUrl();
        } else {
            baseUrl = getClient().getRootUrl() + getClient().getServicePath();
        }
        return new GenericUrl(UriTemplate.expand(baseUrl, this.uriTemplate, this, true));
    }

    public HttpRequest buildHttpRequest() throws IOException {
        HttpRequest request = this.client.buildHttpRequest(this.method, buildHttpRequestUrl(), this.content);
        request.getHeaders().putAll(getRequestHeaders());
        return request;
    }

    public HttpResponse executeUnparsed() throws IOException {
        HttpRequest request = buildHttpRequest();
        request.setEnableGZipContent(this.enableGZipContent);
        HttpResponse response = this.client.executeUnparsed(request);
        this.lastResponseHeaders = response.getHeaders();
        return response;
    }

    public InputStream executeAsInputStream() throws IOException {
        return executeUnparsed().getContent();
    }

    public void download(OutputStream outputStream) throws IOException {
        executeUnparsed().download(outputStream);
    }
}
