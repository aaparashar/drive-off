package com.google.api.client.googleapis.json;

import com.google.api.client.http.HttpResponse;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import java.io.IOException;

@Deprecated
public final class JsonFeedParser<T, I> extends AbstractJsonFeedParser<T> {
    private final Class<I> itemClass;

    public JsonFeedParser(JsonParser parser, Class<T> feedClass, Class<I> itemClass) {
        super(parser, feedClass);
        this.itemClass = itemClass;
    }

    public I parseNextItem() throws IOException {
        return super.parseNextItem();
    }

    Object parseItemInternal() throws IOException {
        return this.parser.parse(this.itemClass, null);
    }

    public static <T, I> JsonFeedParser<T, I> use(JsonFactory jsonFactory, HttpResponse response, Class<T> feedClass, Class<I> itemClass) throws IOException {
        return new JsonFeedParser(JsonCParser.parserForResponse(jsonFactory, response), feedClass, itemClass);
    }
}
