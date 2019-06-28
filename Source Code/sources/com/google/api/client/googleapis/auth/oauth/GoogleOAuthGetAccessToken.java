package com.google.api.client.googleapis.auth.oauth;

import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpTransport;
import java.io.IOException;

@Deprecated
public final class GoogleOAuthGetAccessToken extends OAuthGetAccessToken {
    public GoogleOAuthGetAccessToken() {
        super("https://www.google.com/accounts/OAuthGetAccessToken");
    }

    public static void revokeAccessToken(HttpTransport transport, OAuthParameters parameters) throws IOException {
        HttpRequest request = transport.createRequestFactory().buildGetRequest(new GenericUrl("https://www.google.com/accounts/AuthSubRevokeToken"));
        parameters.intercept(request);
        request.execute().ignore();
    }
}
