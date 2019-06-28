package com.google.api.client.googleapis;

import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import java.util.EnumSet;

public final class MethodOverride implements HttpExecuteInterceptor, HttpRequestInitializer {
    private final EnumSet<HttpMethod> override;

    public MethodOverride() {
        this.override = EnumSet.noneOf(HttpMethod.class);
    }

    public MethodOverride(EnumSet<HttpMethod> override) {
        this.override = override.clone();
    }

    public void initialize(HttpRequest request) {
        request.setInterceptor(this);
    }

    public void intercept(HttpRequest request) {
        if (overrideThisMethod(request)) {
            HttpMethod method = request.getMethod();
            request.setMethod(HttpMethod.POST);
            request.getHeaders().set("X-HTTP-Method-Override", method.name());
            request.setAllowEmptyContent(false);
        }
    }

    private boolean overrideThisMethod(HttpRequest request) {
        HttpMethod method = request.getMethod();
        if (method != HttpMethod.GET && method != HttpMethod.POST && this.override.contains(method)) {
            return true;
        }
        switch (method) {
            case PATCH:
                if (request.getTransport().supportsPatch()) {
                    return false;
                }
                return true;
            case HEAD:
                if (request.getTransport().supportsHead()) {
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
}
