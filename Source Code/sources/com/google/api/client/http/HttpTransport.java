package com.google.api.client.http;

import java.io.IOException;
import java.util.logging.Logger;

public abstract class HttpTransport {
    static final Logger LOGGER = Logger.getLogger(HttpTransport.class.getName());

    protected abstract LowLevelHttpRequest buildDeleteRequest(String str) throws IOException;

    protected abstract LowLevelHttpRequest buildGetRequest(String str) throws IOException;

    protected abstract LowLevelHttpRequest buildPostRequest(String str) throws IOException;

    protected abstract LowLevelHttpRequest buildPutRequest(String str) throws IOException;

    public final HttpRequestFactory createRequestFactory() {
        return createRequestFactory(null);
    }

    public final HttpRequestFactory createRequestFactory(HttpRequestInitializer initializer) {
        return new HttpRequestFactory(this, initializer);
    }

    HttpRequest buildRequest() {
        return new HttpRequest(this, null);
    }

    public boolean supportsHead() {
        return false;
    }

    public boolean supportsPatch() {
        return false;
    }

    protected LowLevelHttpRequest buildHeadRequest(String url) throws IOException {
        throw new UnsupportedOperationException();
    }

    protected LowLevelHttpRequest buildPatchRequest(String url) throws IOException {
        throw new UnsupportedOperationException();
    }

    public void shutdown() throws IOException {
    }
}
