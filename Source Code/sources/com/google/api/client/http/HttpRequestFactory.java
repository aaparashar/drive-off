package com.google.api.client.http;

import java.io.IOException;

public final class HttpRequestFactory {
    private final HttpRequestInitializer initializer;
    private final HttpTransport transport;

    HttpRequestFactory(HttpTransport transport, HttpRequestInitializer initializer) {
        this.transport = transport;
        this.initializer = initializer;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public HttpRequestInitializer getInitializer() {
        return this.initializer;
    }

    public HttpRequest buildRequest(HttpMethod method, GenericUrl url, HttpContent content) throws IOException {
        HttpRequest request = this.transport.buildRequest();
        if (this.initializer != null) {
            this.initializer.initialize(request);
        }
        request.setMethod(method);
        if (url != null) {
            request.setUrl(url);
        }
        if (content != null) {
            request.setContent(content);
        }
        return request;
    }

    public HttpRequest buildDeleteRequest(GenericUrl url) throws IOException {
        return buildRequest(HttpMethod.DELETE, url, null);
    }

    public HttpRequest buildGetRequest(GenericUrl url) throws IOException {
        return buildRequest(HttpMethod.GET, url, null);
    }

    public HttpRequest buildPostRequest(GenericUrl url, HttpContent content) throws IOException {
        return buildRequest(HttpMethod.POST, url, content);
    }

    public HttpRequest buildPutRequest(GenericUrl url, HttpContent content) throws IOException {
        return buildRequest(HttpMethod.PUT, url, content);
    }

    public HttpRequest buildPatchRequest(GenericUrl url, HttpContent content) throws IOException {
        return buildRequest(HttpMethod.PATCH, url, content);
    }

    public HttpRequest buildHeadRequest(GenericUrl url) throws IOException {
        return buildRequest(HttpMethod.HEAD, url, null);
    }
}
