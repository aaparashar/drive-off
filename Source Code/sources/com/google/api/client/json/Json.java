package com.google.api.client.json;

import com.google.api.client.http.HttpMediaType;
import com.google.common.base.Charsets;

public class Json {
    @Deprecated
    public static final String CONTENT_TYPE = "application/json";
    public static final String MEDIA_TYPE = new HttpMediaType(CONTENT_TYPE).setCharsetParameter(Charsets.UTF_8).build();
}
