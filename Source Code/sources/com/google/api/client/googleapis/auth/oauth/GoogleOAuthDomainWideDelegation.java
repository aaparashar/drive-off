package com.google.api.client.googleapis.auth.oauth;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.googleapis.GoogleUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.util.Key;
import java.io.IOException;

@Deprecated
public final class GoogleOAuthDomainWideDelegation implements HttpExecuteInterceptor, HttpRequestInitializer {
    public OAuthParameters parameters;
    public String requestorId;

    public static final class Url extends GoogleUrl {
        @Key("xoauth_requestor_id")
        public String requestorId;

        public Url(String encodedUrl) {
            super(encodedUrl);
        }
    }

    public void initialize(HttpRequest request) {
        request.setInterceptor(this);
    }

    public void intercept(HttpRequest request) throws IOException {
        request.getUrl().set("xoauth_requestor_id", this.requestorId);
        if (this.parameters != null) {
            this.parameters.intercept(request);
        }
    }
}
