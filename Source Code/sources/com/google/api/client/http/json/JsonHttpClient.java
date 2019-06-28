package com.google.api.client.http.json;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonObjectParser;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

public class JsonHttpClient {
    static final Logger LOGGER = Logger.getLogger(JsonHttpClient.class.getName());
    private final String applicationName;
    @Deprecated
    private final String baseUrl;
    @Deprecated
    private final boolean baseUrlUsed;
    private final JsonFactory jsonFactory;
    @Deprecated
    private JsonHttpParser jsonHttpParser;
    private final JsonHttpRequestInitializer jsonHttpRequestInitializer;
    private final JsonObjectParser jsonObjectParser;
    private final HttpRequestFactory requestFactory;
    private final String rootUrl;
    private final String servicePath;

    public static class Builder {
        private String applicationName;
        @Deprecated
        private GenericUrl baseUrl;
        @Deprecated
        private boolean baseUrlUsed;
        private HttpRequestInitializer httpRequestInitializer;
        private final JsonFactory jsonFactory;
        private JsonHttpRequestInitializer jsonHttpRequestInitializer;
        private JsonObjectParser jsonObjectParser;
        private String rootUrl;
        private String servicePath;
        private final HttpTransport transport;

        @Deprecated
        protected Builder(HttpTransport transport, JsonFactory jsonFactory, GenericUrl baseUrl) {
            this.transport = transport;
            this.jsonFactory = jsonFactory;
            this.baseUrlUsed = true;
            setBaseUrl(baseUrl);
        }

        public Builder(HttpTransport transport, JsonFactory jsonFactory, String rootUrl, String servicePath, HttpRequestInitializer httpRequestInitializer) {
            this.transport = transport;
            this.jsonFactory = jsonFactory;
            setRootUrl(rootUrl);
            setServicePath(servicePath);
            this.httpRequestInitializer = httpRequestInitializer;
        }

        public JsonHttpClient build() {
            if (Strings.isNullOrEmpty(this.applicationName)) {
                JsonHttpClient.LOGGER.warning("Application name is not set. Call setApplicationName.");
            }
            if (this.baseUrlUsed) {
                return new JsonHttpClient(this.transport, this.jsonHttpRequestInitializer, this.httpRequestInitializer, this.jsonFactory, this.jsonObjectParser, this.baseUrl.build(), this.applicationName);
            }
            return new JsonHttpClient(this.transport, this.jsonHttpRequestInitializer, this.httpRequestInitializer, this.jsonFactory, this.jsonObjectParser, this.rootUrl, this.servicePath, this.applicationName);
        }

        @Deprecated
        protected final boolean isBaseUrlUsed() {
            return this.baseUrlUsed;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public final JsonObjectParser getObjectParser() {
            return this.jsonObjectParser;
        }

        public Builder setObjectParser(JsonObjectParser parser) {
            this.jsonObjectParser = parser;
            return this;
        }

        @Deprecated
        public final GenericUrl getBaseUrl() {
            Preconditions.checkArgument(this.baseUrlUsed);
            return this.baseUrl;
        }

        @Deprecated
        public Builder setBaseUrl(GenericUrl baseUrl) {
            Preconditions.checkArgument(this.baseUrlUsed);
            this.baseUrl = (GenericUrl) Preconditions.checkNotNull(baseUrl);
            Preconditions.checkArgument(baseUrl.build().endsWith("/"));
            return this;
        }

        public final String getRootUrl() {
            Preconditions.checkArgument(!this.baseUrlUsed, "root URL cannot be used if base URL is used.");
            return this.rootUrl;
        }

        public Builder setRootUrl(String rootUrl) {
            Preconditions.checkArgument(!this.baseUrlUsed, "root URL cannot be used if base URL is used.");
            Preconditions.checkNotNull(rootUrl, "root URL cannot be null.");
            Preconditions.checkArgument(rootUrl.endsWith("/"), "root URL must end with a \"/\".");
            this.rootUrl = rootUrl;
            return this;
        }

        public final String getServicePath() {
            Preconditions.checkArgument(!this.baseUrlUsed, "service path cannot be used if base URL is used.");
            return this.servicePath;
        }

        public Builder setServicePath(String servicePath) {
            boolean z = true;
            Preconditions.checkArgument(!this.baseUrlUsed, "service path cannot be used if base URL is used.");
            servicePath = (String) Preconditions.checkNotNull(servicePath, "service path cannot be null.");
            if (servicePath.length() == 1) {
                Preconditions.checkArgument("/".equals(servicePath), "service path must equal \"/\" if it is of length 1.");
                servicePath = "";
            } else if (servicePath.length() > 0) {
                if (!servicePath.endsWith("/") || servicePath.startsWith("/")) {
                    z = false;
                }
                Preconditions.checkArgument(z, "service path must end with a \"/\" and not begin with a \"/\".");
            }
            this.servicePath = servicePath;
            return this;
        }

        public Builder setJsonHttpRequestInitializer(JsonHttpRequestInitializer jsonHttpRequestInitializer) {
            this.jsonHttpRequestInitializer = jsonHttpRequestInitializer;
            return this;
        }

        public JsonHttpRequestInitializer getJsonHttpRequestInitializer() {
            return this.jsonHttpRequestInitializer;
        }

        public Builder setHttpRequestInitializer(HttpRequestInitializer httpRequestInitializer) {
            this.httpRequestInitializer = httpRequestInitializer;
            return this;
        }

        public final HttpRequestInitializer getHttpRequestInitializer() {
            return this.httpRequestInitializer;
        }

        public Builder setApplicationName(String applicationName) {
            this.applicationName = applicationName;
            return this;
        }

        public final String getApplicationName() {
            return this.applicationName;
        }
    }

    @Deprecated
    protected final boolean isBaseUrlUsed() {
        return this.baseUrlUsed;
    }

    public final String getBaseUrl() {
        if (this.baseUrlUsed) {
            return this.baseUrl;
        }
        return this.rootUrl + this.servicePath;
    }

    public final String getRootUrl() {
        Preconditions.checkArgument(!this.baseUrlUsed, "root URL cannot be used if base URL is used.");
        return this.rootUrl;
    }

    public final String getServicePath() {
        Preconditions.checkArgument(!this.baseUrlUsed, "service path cannot be used if base URL is used.");
        return this.servicePath;
    }

    public final String getApplicationName() {
        return this.applicationName;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final HttpRequestFactory getRequestFactory() {
        return this.requestFactory;
    }

    public final JsonHttpRequestInitializer getJsonHttpRequestInitializer() {
        return this.jsonHttpRequestInitializer;
    }

    @Deprecated
    public final JsonHttpParser getJsonHttpParser() {
        if (this.jsonHttpParser == null) {
            this.jsonHttpParser = createParser();
        }
        return this.jsonHttpParser;
    }

    @Deprecated
    protected JsonHttpParser createParser() {
        return new JsonHttpParser(this.jsonFactory);
    }

    public final JsonObjectParser getJsonObjectParser() {
        return this.jsonObjectParser;
    }

    protected JsonHttpContent createSerializer(Object body) {
        return new JsonHttpContent(getJsonFactory(), body);
    }

    protected void initialize(JsonHttpRequest jsonHttpRequest) throws IOException {
        if (getJsonHttpRequestInitializer() != null) {
            getJsonHttpRequestInitializer().initialize(jsonHttpRequest);
        }
    }

    @Deprecated
    public JsonHttpClient(HttpTransport transport, JsonFactory jsonFactory, String baseUrl) {
        this(transport, null, null, jsonFactory, baseUrl, null);
    }

    public JsonHttpClient(HttpTransport transport, JsonFactory jsonFactory, String rootUrl, String servicePath, HttpRequestInitializer httpRequestInitializer) {
        this(transport, null, httpRequestInitializer, jsonFactory, null, rootUrl, servicePath, null);
    }

    @Deprecated
    protected JsonHttpClient(HttpTransport transport, JsonHttpRequestInitializer jsonHttpRequestInitializer, HttpRequestInitializer httpRequestInitializer, JsonFactory jsonFactory, String baseUrl, String applicationName) {
        this(transport, jsonHttpRequestInitializer, httpRequestInitializer, jsonFactory, null, baseUrl, applicationName);
    }

    @Deprecated
    protected JsonHttpClient(HttpTransport transport, JsonHttpRequestInitializer jsonHttpRequestInitializer, HttpRequestInitializer httpRequestInitializer, JsonFactory jsonFactory, JsonObjectParser jsonObjectParser, String baseUrl, String applicationName) {
        this.jsonHttpRequestInitializer = jsonHttpRequestInitializer;
        this.baseUrl = (String) Preconditions.checkNotNull(baseUrl);
        Preconditions.checkArgument(baseUrl.endsWith("/"));
        this.applicationName = applicationName;
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.jsonObjectParser = jsonObjectParser;
        Preconditions.checkNotNull(transport);
        this.requestFactory = httpRequestInitializer == null ? transport.createRequestFactory() : transport.createRequestFactory(httpRequestInitializer);
        this.baseUrlUsed = true;
        this.rootUrl = null;
        this.servicePath = null;
    }

    protected JsonHttpClient(HttpTransport transport, JsonHttpRequestInitializer jsonHttpRequestInitializer, HttpRequestInitializer httpRequestInitializer, JsonFactory jsonFactory, JsonObjectParser jsonObjectParser, String rootUrl, String servicePath, String applicationName) {
        this.jsonHttpRequestInitializer = jsonHttpRequestInitializer;
        this.rootUrl = (String) Preconditions.checkNotNull(rootUrl, "root URL cannot be null.");
        Preconditions.checkArgument(rootUrl.endsWith("/"), "root URL must end with a \"/\".");
        Preconditions.checkNotNull(servicePath, "service path cannot be null");
        if (servicePath.length() == 1) {
            Preconditions.checkArgument("/".equals(servicePath), "service path must equal \"/\" if it is of length 1.");
            servicePath = "";
        } else if (servicePath.length() > 0) {
            boolean z = servicePath.endsWith("/") && !servicePath.startsWith("/");
            Preconditions.checkArgument(z, "service path must end with a \"/\" and not begin with a \"/\".");
        }
        this.servicePath = servicePath;
        this.applicationName = applicationName;
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        Preconditions.checkNotNull(transport);
        this.jsonObjectParser = jsonObjectParser;
        this.requestFactory = httpRequestInitializer == null ? transport.createRequestFactory() : transport.createRequestFactory(httpRequestInitializer);
        this.baseUrl = null;
        this.baseUrlUsed = false;
    }

    protected HttpRequest buildHttpRequest(HttpMethod method, GenericUrl url, Object body) throws IOException {
        HttpRequest httpRequest = this.requestFactory.buildRequest(method, url, null);
        JsonObjectParser parser = getJsonObjectParser();
        if (parser != null) {
            httpRequest.setParser(parser);
        } else {
            httpRequest.addParser(getJsonHttpParser());
        }
        if (getApplicationName() != null) {
            httpRequest.getHeaders().setUserAgent(getApplicationName());
        }
        if (body != null) {
            httpRequest.setContent(createSerializer(body));
        }
        return httpRequest;
    }

    protected HttpResponse executeUnparsed(HttpMethod method, GenericUrl url, Object body) throws IOException {
        return executeUnparsed(buildHttpRequest(method, url, body));
    }

    protected HttpResponse executeUnparsed(HttpRequest request) throws IOException {
        return request.execute();
    }

    protected InputStream executeAsInputStream(HttpMethod method, GenericUrl url, Object body) throws IOException {
        return executeUnparsed(method, url, body).getContent();
    }
}
