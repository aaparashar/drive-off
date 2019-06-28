package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpMediaType;
import com.google.api.client.http.json.JsonHttpContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonGenerator;
import java.io.IOException;
import java.io.OutputStream;

public final class JsonCContent extends JsonHttpContent {
    public JsonCContent(JsonFactory jsonFactory, Object data) {
        super(jsonFactory, data);
    }

    public void writeTo(OutputStream out) throws IOException {
        JsonGenerator generator = getJsonFactory().createJsonGenerator(out, getCharset());
        generator.writeStartObject();
        generator.writeFieldName("data");
        generator.serialize(getData());
        generator.writeEndObject();
        generator.flush();
    }

    public JsonCContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }
}
