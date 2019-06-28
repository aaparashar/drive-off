package com.google.api.client.googleapis.auth.oauth2.draft10;

import com.google.api.client.auth.oauth2.draft10.AccessProtectedResource;
import com.google.api.client.auth.oauth2.draft10.AccessProtectedResource.Method;
import com.google.api.client.googleapis.auth.oauth2.draft10.GoogleAccessTokenRequest.GoogleRefreshTokenGrant;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import java.io.IOException;

@Deprecated
public class GoogleAccessProtectedResource extends AccessProtectedResource {
    public GoogleAccessProtectedResource(String accessToken) {
        super(accessToken, Method.AUTHORIZATION_HEADER);
    }

    public GoogleAccessProtectedResource(String accessToken, HttpTransport transport, JsonFactory jsonFactory, String clientId, String clientSecret, String refreshToken) {
        super(accessToken, Method.AUTHORIZATION_HEADER, transport, jsonFactory, "https://accounts.google.com/o/oauth2/token", clientId, clientSecret, refreshToken);
    }

    protected boolean executeRefreshToken() throws IOException {
        if (getRefreshToken() != null) {
            return executeAccessTokenRequest(new GoogleRefreshTokenGrant(getTransport(), getJsonFactory(), getClientId(), getClientSecret(), getRefreshToken()));
        }
        return false;
    }
}
