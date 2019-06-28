package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.ObjectParser;
import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public final class JsonCParser extends JsonHttpParser implements ObjectParser {
    private final JsonFactory jsonFactory;

    public final JsonFactory getFactory() {
        return this.jsonFactory;
    }

    public JsonCParser(JsonFactory jsonFactory) {
        super(jsonFactory);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
    }

    @Deprecated
    public <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        return parserForResponse(getJsonFactory(), response).parseAndClose((Class) dataClass, null);
    }

    @Deprecated
    public static JsonParser parserForResponse(JsonFactory jsonFactory, HttpResponse response) throws IOException {
        String contentType = response.getContentType();
        if (contentType == null || !contentType.startsWith(Json.CONTENT_TYPE)) {
            throw new IllegalArgumentException("Wrong content type: expected <application/json> but got <" + contentType + ">");
        }
        boolean failed = true;
        JsonParser parser = JsonHttpParser.parserForResponse(jsonFactory, response);
        try {
            parser.skipToKey(response.isSuccessStatusCode() ? "data" : "error");
            if (parser.getCurrentToken() == JsonToken.END_OBJECT) {
                throw new IllegalArgumentException("data key not found");
            }
            failed = false;
            return parser;
        } finally {
            if (failed) {
                parser.close();
            }
        }
    }

    public static JsonParser initializeParser(JsonParser parser) throws IOException {
        boolean failed = true;
        try {
            if (parser.skipToKey(Sets.newHashSet("data", "error")) == null || parser.getCurrentToken() == JsonToken.END_OBJECT) {
                throw new IllegalArgumentException("data key not found");
            }
            failed = false;
            return parser;
        } finally {
            if (failed) {
                parser.close();
            }
        }
    }

    public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException {
        return parseAndClose(in, charset, (Type) dataClass);
    }

    public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
        return initializeParser(this.jsonFactory.createJsonParser(in, charset)).parse(dataType, true, null);
    }

    public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        return parseAndClose(reader, (Type) dataClass);
    }

    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        return initializeParser(this.jsonFactory.createJsonParser(reader)).parse(dataType, true, null);
    }
}
