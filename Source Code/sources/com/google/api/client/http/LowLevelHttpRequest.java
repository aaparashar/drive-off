package com.google.api.client.http;

import java.io.IOException;

public abstract class LowLevelHttpRequest {
    public abstract void addHeader(String str, String str2) throws IOException;

    public abstract LowLevelHttpResponse execute() throws IOException;

    public abstract void setContent(HttpContent httpContent) throws IOException;

    public void setTimeout(int connectTimeout, int readTimeout) throws IOException {
    }
}
