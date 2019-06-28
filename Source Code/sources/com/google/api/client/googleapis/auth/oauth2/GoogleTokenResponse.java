package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.openidconnect.IdTokenResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;

public class GoogleTokenResponse extends IdTokenResponse {
    public GoogleTokenResponse setIdToken(String idToken) {
        return (GoogleTokenResponse) super.setIdToken(idToken);
    }

    public GoogleTokenResponse setAccessToken(String accessToken) {
        return (GoogleTokenResponse) super.setAccessToken(accessToken);
    }

    public GoogleTokenResponse setTokenType(String tokenType) {
        return (GoogleTokenResponse) super.setTokenType(tokenType);
    }

    public GoogleTokenResponse setExpiresInSeconds(Long expiresIn) {
        return (GoogleTokenResponse) super.setExpiresInSeconds(expiresIn);
    }

    public GoogleTokenResponse setRefreshToken(String refreshToken) {
        return (GoogleTokenResponse) super.setRefreshToken(refreshToken);
    }

    public GoogleTokenResponse setScope(String scope) {
        return (GoogleTokenResponse) super.setScope(scope);
    }

    public GoogleIdToken parseIdToken() throws IOException {
        return GoogleIdToken.parse(getFactory(), getIdToken());
    }

    public boolean verifyIdToken(GoogleIdTokenVerifier verifier) throws GeneralSecurityException, IOException {
        return verifier.verify(parseIdToken());
    }
}
