package com.google.api.client.googleapis.json;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.rpc2.JsonRpcRequest;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.List;

public final class GoogleJsonRpcHttpTransport {
    private static final String JSON_RPC_CONTENT_TYPE = "application/json-rpc";
    @Deprecated
    public String accept;
    @Deprecated
    public String contentType;
    @Deprecated
    public JsonFactory jsonFactory;
    @Deprecated
    public GenericUrl rpcServerUrl;
    @Deprecated
    public HttpTransport transport;

    public static class Builder {
        static final GenericUrl DEFAULT_SERVER_URL = new GenericUrl("https://www.googleapis.com");
        private String accept = this.mimeType;
        private final HttpTransport httpTransport;
        private final JsonFactory jsonFactory;
        private String mimeType = GoogleJsonRpcHttpTransport.JSON_RPC_CONTENT_TYPE;
        private GenericUrl rpcServerUrl = DEFAULT_SERVER_URL;

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory) {
            this.httpTransport = (HttpTransport) Preconditions.checkNotNull(httpTransport);
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        }

        protected Builder setRpcServerUrl(GenericUrl rpcServerUrl) {
            this.rpcServerUrl = (GenericUrl) Preconditions.checkNotNull(rpcServerUrl);
            return this;
        }

        @Deprecated
        protected Builder setContentType(String contentType) {
            this.mimeType = (String) Preconditions.checkNotNull(contentType);
            return this;
        }

        protected Builder setMimeType(String mimeType) {
            this.mimeType = (String) Preconditions.checkNotNull(mimeType);
            return this;
        }

        protected Builder setAccept(String accept) {
            this.accept = (String) Preconditions.checkNotNull(accept);
            return this;
        }

        protected GoogleJsonRpcHttpTransport build() {
            return new GoogleJsonRpcHttpTransport(this.httpTransport, this.jsonFactory, this.rpcServerUrl.build(), this.mimeType, this.accept);
        }

        public final HttpTransport getHttpTransport() {
            return this.httpTransport;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final GenericUrl getRpcServerUrl() {
            return this.rpcServerUrl;
        }

        @Deprecated
        public final String getContentType() {
            return this.mimeType;
        }

        public final String getMimeType() {
            return this.mimeType;
        }

        public final String getAccept() {
            return this.accept;
        }
    }

    public GoogleJsonRpcHttpTransport(HttpTransport httpTransport, JsonFactory jsonFactory) {
        this((HttpTransport) Preconditions.checkNotNull(httpTransport), (JsonFactory) Preconditions.checkNotNull(jsonFactory), Builder.DEFAULT_SERVER_URL.build(), JSON_RPC_CONTENT_TYPE, JSON_RPC_CONTENT_TYPE);
    }

    protected GoogleJsonRpcHttpTransport(HttpTransport httpTransport, JsonFactory jsonFactory, String rpcServerUrl, String mimeType, String accept) {
        this.contentType = JSON_RPC_CONTENT_TYPE;
        this.accept = this.contentType;
        this.transport = httpTransport;
        this.jsonFactory = jsonFactory;
        this.rpcServerUrl = new GenericUrl(rpcServerUrl);
        this.contentType = mimeType;
        this.accept = accept;
    }

    public final HttpTransport getHttpTransport() {
        return this.transport;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final String getRpcServerUrl() {
        return this.rpcServerUrl.build();
    }

    @Deprecated
    public final String getContentType() {
        return this.contentType;
    }

    public final String getMimeType() {
        return this.contentType;
    }

    public final String getAccept() {
        return this.accept;
    }

    public HttpRequest buildPostRequest(JsonRpcRequest request) {
        return internalExecute(request);
    }

    public HttpRequest buildPostRequest(List<JsonRpcRequest> requests) {
        return internalExecute(requests);
    }

    private HttpRequest internalExecute(Object data) {
        JsonHttpContent content = new JsonHttpContent(this.jsonFactory, data);
        content.setMediaType(new HttpMediaType(this.contentType));
        try {
            HttpRequest httpRequest = this.transport.createRequestFactory().buildPostRequest(this.rpcServerUrl, content);
            httpRequest.getHeaders().setAccept(this.accept);
            return httpRequest;
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
}
