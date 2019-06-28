package com.google.api.client.http;

import com.google.common.base.Preconditions;
import java.io.IOException;

public final class BasicAuthentication implements HttpRequestInitializer, HttpExecuteInterceptor {
    private final String password;
    private final String username;

    public BasicAuthentication(String username, String password) {
        this.username = (String) Preconditions.checkNotNull(username);
        this.password = (String) Preconditions.checkNotNull(password);
    }

    public void initialize(HttpRequest request) throws IOException {
        request.setInterceptor(this);
        intercept(request);
    }

    public void intercept(HttpRequest request) throws IOException {
        request.getHeaders().setBasicAuthentication(this.username, this.password);
    }

    public String getUsername() {
        return this.username;
    }

    public String getPassword() {
        return this.password;
    }
}
