package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;

public class RefreshTokenRequest extends TokenRequest {
    @Key("refresh_token")
    private String refreshToken;

    public RefreshTokenRequest(HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, String refreshToken) {
        super(transport, jsonFactory, tokenServerUrl, "refresh_token");
        setRefreshToken(refreshToken);
    }

    public RefreshTokenRequest setRequestInitializer(HttpRequestInitializer requestInitializer) {
        return (RefreshTokenRequest) super.setRequestInitializer(requestInitializer);
    }

    public RefreshTokenRequest setTokenServerUrl(GenericUrl tokenServerUrl) {
        return (RefreshTokenRequest) super.setTokenServerUrl(tokenServerUrl);
    }

    public RefreshTokenRequest setScopes(String... scopes) {
        return (RefreshTokenRequest) super.setScopes(scopes);
    }

    public RefreshTokenRequest setScopes(Iterable<String> scopes) {
        return (RefreshTokenRequest) super.setScopes((Iterable) scopes);
    }

    public RefreshTokenRequest setGrantType(String grantType) {
        return (RefreshTokenRequest) super.setGrantType(grantType);
    }

    public RefreshTokenRequest setClientAuthentication(HttpExecuteInterceptor clientAuthentication) {
        return (RefreshTokenRequest) super.setClientAuthentication(clientAuthentication);
    }

    public final String getRefreshToken() {
        return this.refreshToken;
    }

    public RefreshTokenRequest setRefreshToken(String refreshToken) {
        this.refreshToken = (String) Preconditions.checkNotNull(refreshToken);
        return this;
    }
}
