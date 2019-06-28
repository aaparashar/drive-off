package com.google.api.client.http.javanet;

import com.google.api.client.http.HttpTransport;
import java.io.IOException;

public final class NetHttpTransport extends HttpTransport {
    public boolean supportsHead() {
        return true;
    }

    public NetHttpRequest buildDeleteRequest(String url) throws IOException {
        return new NetHttpRequest("DELETE", url);
    }

    public NetHttpRequest buildGetRequest(String url) throws IOException {
        return new NetHttpRequest("GET", url);
    }

    public NetHttpRequest buildHeadRequest(String url) throws IOException {
        return new NetHttpRequest("HEAD", url);
    }

    public NetHttpRequest buildPostRequest(String url) throws IOException {
        return new NetHttpRequest("POST", url);
    }

    public NetHttpRequest buildPutRequest(String url) throws IOException {
        return new NetHttpRequest("PUT", url);
    }
}
