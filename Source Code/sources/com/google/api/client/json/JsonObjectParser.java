package com.google.api.client.json;

import com.google.api.client.util.ObjectParser;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class JsonObjectParser implements ObjectParser {
    private final JsonFactory jsonFactory;

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public JsonObjectParser(JsonFactory jsonFactory) {
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
    }

    public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException {
        return parseAndClose(in, charset, (Type) dataClass);
    }

    public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
        return this.jsonFactory.createJsonParser(in, charset).parse(dataType, true, null);
    }

    public <T> T parseAndClose(Reader reader, Class<T> dataClass) throws IOException {
        return parseAndClose(reader, (Type) dataClass);
    }

    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        return this.jsonFactory.createJsonParser(reader).parse(dataType, true, null);
    }
}
