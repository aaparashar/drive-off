package com.google.api.client.json;

import com.google.common.base.Charsets;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class JsonFactory {
    @Deprecated
    public abstract JsonGenerator createJsonGenerator(OutputStream outputStream, JsonEncoding jsonEncoding) throws IOException;

    public abstract JsonGenerator createJsonGenerator(OutputStream outputStream, Charset charset) throws IOException;

    public abstract JsonGenerator createJsonGenerator(Writer writer) throws IOException;

    public abstract JsonParser createJsonParser(InputStream inputStream) throws IOException;

    public abstract JsonParser createJsonParser(InputStream inputStream, Charset charset) throws IOException;

    public abstract JsonParser createJsonParser(Reader reader) throws IOException;

    public abstract JsonParser createJsonParser(String str) throws IOException;

    public final JsonObjectParser createJsonObjectParser() {
        return new JsonObjectParser(this);
    }

    public final String toString(Object item) {
        return toString(item, false);
    }

    public final String toPrettyString(Object item) {
        return toString(item, true);
    }

    public final byte[] toByteArray(Object item) {
        return toByteStream(item, false).toByteArray();
    }

    private String toString(Object item, boolean pretty) {
        try {
            return toByteStream(item, pretty).toString("UTF-8");
        } catch (UnsupportedEncodingException exception) {
            throw new RuntimeException(exception);
        }
    }

    private ByteArrayOutputStream toByteStream(Object item, boolean pretty) {
        OutputStream byteStream = new ByteArrayOutputStream();
        try {
            JsonGenerator generator = createJsonGenerator(byteStream, Charsets.UTF_8);
            if (pretty) {
                generator.enablePrettyPrint();
            }
            generator.serialize(item);
            generator.flush();
            return byteStream;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final <T> T fromString(String value, Class<T> destinationClass) throws IOException {
        return createJsonParser(value).parse((Class) destinationClass, null);
    }

    public final <T> T fromInputStream(InputStream inputStream, Class<T> destinationClass) throws IOException {
        return createJsonParser(inputStream).parseAndClose((Class) destinationClass, null);
    }

    public final <T> T fromInputStream(InputStream inputStream, Charset charset, Class<T> destinationClass) throws IOException {
        return createJsonParser(inputStream, charset).parseAndClose((Class) destinationClass, null);
    }

    public final <T> T fromReader(Reader reader, Class<T> destinationClass) throws IOException {
        return createJsonParser(reader).parseAndClose((Class) destinationClass, null);
    }
}
