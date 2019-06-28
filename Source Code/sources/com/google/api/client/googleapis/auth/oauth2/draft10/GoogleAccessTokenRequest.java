package com.google.api.client.googleapis.auth.oauth2.draft10;

import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest;
import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.AssertionGrant;
import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.AuthorizationCodeGrant;
import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.RefreshTokenGrant;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;

@Deprecated
public class GoogleAccessTokenRequest {
    public static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/token";

    @Deprecated
    public static class GoogleAssertionGrant extends AssertionGrant {
        public GoogleAssertionGrant() {
            GoogleAccessTokenRequest.init(this);
        }

        public GoogleAssertionGrant(HttpTransport transport, JsonFactory jsonFactory, String assertionType, String assertion) {
            super(transport, jsonFactory, "https://accounts.google.com/o/oauth2/token", assertionType, assertion);
            GoogleAccessTokenRequest.init(this);
        }
    }

    @Deprecated
    public static class GoogleAuthorizationCodeGrant extends AuthorizationCodeGrant {
        public GoogleAuthorizationCodeGrant() {
            GoogleAccessTokenRequest.init(this);
        }

        public GoogleAuthorizationCodeGrant(HttpTransport transport, JsonFactory jsonFactory, String clientId, String clientSecret, String code, String redirectUri) {
            super(transport, jsonFactory, "https://accounts.google.com/o/oauth2/token", clientId, clientSecret, code, redirectUri);
            GoogleAccessTokenRequest.init(this);
        }
    }

    @Deprecated
    public static class GoogleRefreshTokenGrant extends RefreshTokenGrant {
        public GoogleRefreshTokenGrant() {
            GoogleAccessTokenRequest.init(this);
        }

        public GoogleRefreshTokenGrant(HttpTransport transport, JsonFactory jsonFactory, String clientId, String clientSecret, String refreshToken) {
            super(transport, jsonFactory, "https://accounts.google.com/o/oauth2/token", clientId, clientSecret, refreshToken);
            GoogleAccessTokenRequest.init(this);
        }
    }

    static void init(AccessTokenRequest request) {
        request.authorizationServerUrl = "https://accounts.google.com/o/oauth2/token";
    }

    private GoogleAccessTokenRequest() {
    }
}
