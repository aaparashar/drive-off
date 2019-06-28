package com.google.api.client.http.json;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public class JsonHttpParser implements HttpParser {
    private final String contentType;
    private final JsonFactory jsonFactory;

    public static class Builder {
        private String contentType = Json.CONTENT_TYPE;
        private final JsonFactory jsonFactory;

        protected Builder(JsonFactory jsonFactory) {
            this.jsonFactory = jsonFactory;
        }

        public JsonHttpParser build() {
            return new JsonHttpParser(this.jsonFactory, this.contentType);
        }

        public final String getContentType() {
            return this.contentType;
        }

        public Builder setContentType(String contentType) {
            this.contentType = (String) Preconditions.checkNotNull(contentType);
            return this;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }
    }

    public JsonHttpParser(JsonFactory jsonFactory) {
        this(jsonFactory, Json.CONTENT_TYPE);
    }

    protected JsonHttpParser(JsonFactory jsonFactory, String contentType) {
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.contentType = contentType;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        return parserForResponse(this.jsonFactory, response).parseAndClose((Class) dataClass, null);
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public static JsonParser parserForResponse(JsonFactory jsonFactory, HttpResponse response) throws IOException {
        InputStream content = response.getContent();
        try {
            JsonParser parser = jsonFactory.createJsonParser(content);
            parser.nextToken();
            content = null;
            return parser;
        } finally {
            if (content != null) {
                content.close();
            }
        }
    }

    public static Builder builder(JsonFactory jsonFactory) {
        return new Builder(jsonFactory);
    }
}
