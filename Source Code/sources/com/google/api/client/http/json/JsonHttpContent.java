package com.google.api.client.http.json;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.json.Json;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public class JsonHttpContent extends AbstractHttpContent {
    private final Object data;
    private final JsonFactory jsonFactory;

    public JsonHttpContent(JsonFactory jsonFactory, Object data) {
        super(Json.MEDIA_TYPE);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.data = Preconditions.checkNotNull(data);
    }

    public void writeTo(OutputStream out) throws IOException {
        JsonGenerator generator = this.jsonFactory.createJsonGenerator(out, getCharset());
        generator.serialize(this.data);
        generator.flush();
    }

    @Deprecated
    public JsonHttpContent setType(String type) {
        setMediaType(new HttpMediaType(type));
        return this;
    }

    public JsonHttpContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public final Object getData() {
        return this.data;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }
}
