package com.google.api.client.http.json;

import java.io.IOException;

public interface JsonHttpRequestInitializer {
    void initialize(JsonHttpRequest jsonHttpRequest) throws IOException;
}
