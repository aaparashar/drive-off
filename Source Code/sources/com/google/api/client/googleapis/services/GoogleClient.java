package com.google.api.client.googleapis.services;

import com.google.api.client.googleapis.MethodOverride;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpClient;
import com.google.api.client.http.json.JsonHttpRequestInitializer;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import java.io.IOException;
import java.util.Arrays;

public class GoogleClient extends JsonHttpClient {

    public static class Builder extends com.google.api.client.http.json.JsonHttpClient.Builder {
        @Deprecated
        protected Builder(HttpTransport transport, JsonFactory jsonFactory, GenericUrl baseUrl) {
            super(transport, jsonFactory, baseUrl);
        }

        public Builder(HttpTransport transport, JsonFactory jsonFactory, String rootUrl, String servicePath, HttpRequestInitializer httpRequestInitializer) {
            super(transport, jsonFactory, rootUrl, servicePath, httpRequestInitializer);
        }

        public GoogleClient build() {
            if (isBaseUrlUsed()) {
                return new GoogleClient(getTransport(), getJsonHttpRequestInitializer(), getHttpRequestInitializer(), getJsonFactory(), getObjectParser(), getBaseUrl().build(), getApplicationName());
            }
            return new GoogleClient(getTransport(), getJsonHttpRequestInitializer(), getHttpRequestInitializer(), getJsonFactory(), getObjectParser(), getRootUrl(), getServicePath(), getApplicationName());
        }
    }

    @Deprecated
    public GoogleClient(HttpTransport transport, JsonFactory jsonFactory, String baseUrl) {
        super(transport, jsonFactory, baseUrl);
    }

    public GoogleClient(HttpTransport transport, JsonFactory jsonFactory, String rootUrl, String servicePath, HttpRequestInitializer httpRequestInitializer) {
        super(transport, jsonFactory, rootUrl, servicePath, httpRequestInitializer);
    }

    @Deprecated
    protected GoogleClient(HttpTransport transport, JsonHttpRequestInitializer jsonHttpRequestInitializer, HttpRequestInitializer httpRequestInitializer, JsonFactory jsonFactory, JsonObjectParser jsonObjectParser, String baseUrl, String applicationName) {
        super(transport, jsonHttpRequestInitializer, httpRequestInitializer, jsonFactory, jsonObjectParser, baseUrl, applicationName);
    }

    protected GoogleClient(HttpTransport transport, JsonHttpRequestInitializer jsonHttpRequestInitializer, HttpRequestInitializer httpRequestInitializer, JsonFactory jsonFactory, JsonObjectParser jsonObjectParser, String rootUrl, String servicePath, String applicationName) {
        super(transport, jsonHttpRequestInitializer, httpRequestInitializer, jsonFactory, jsonObjectParser, rootUrl, servicePath, applicationName);
    }

    protected HttpRequest buildHttpRequest(HttpMethod method, GenericUrl url, Object body) throws IOException {
        HttpRequest httpRequest = super.buildHttpRequest(method, url, body);
        new MethodOverride().intercept(httpRequest);
        httpRequest.setAllowEmptyContent(false);
        return httpRequest;
    }

    public BatchRequest batch() {
        return batch(null);
    }

    public BatchRequest batch(HttpRequestInitializer httpRequestInitializer) {
        GenericUrl baseUrl;
        BatchRequest batch = new BatchRequest(getRequestFactory().getTransport(), httpRequestInitializer);
        if (isBaseUrlUsed()) {
            baseUrl = new GenericUrl(getBaseUrl());
            baseUrl.setPathParts(Arrays.asList(new String[]{"", "batch"}));
        } else {
            baseUrl = new GenericUrl(getRootUrl() + "batch");
        }
        batch.setBatchUrl(baseUrl);
        return batch;
    }

    protected HttpResponse executeUnparsed(HttpMethod method, GenericUrl url, Object body) throws IOException {
        return executeUnparsed(buildHttpRequest(method, url, body));
    }

    protected HttpResponse executeUnparsed(HttpRequest request) throws IOException {
        return GoogleJsonResponseException.execute(getJsonFactory(), request);
    }
}
