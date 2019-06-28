package com.google.api.client.http.apache;

import java.net.URI;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

final class HttpPatch extends HttpEntityEnclosingRequestBase {
    public HttpPatch(String uri) {
        setURI(URI.create(uri));
    }

    public String getMethod() {
        return "PATCH";
    }
}
