package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeTokenRequest;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.common.base.Preconditions;
import java.io.IOException;

public class GoogleAuthorizationCodeTokenRequest extends AuthorizationCodeTokenRequest {
    public GoogleAuthorizationCodeTokenRequest(HttpTransport transport, JsonFactory jsonFactory, String clientId, String clientSecret, String code, String redirectUri) {
        super(transport, jsonFactory, new GenericUrl("https://accounts.google.com/o/oauth2/token"), code);
        setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));
        setRedirectUri(redirectUri);
    }

    public GoogleAuthorizationCodeTokenRequest setRequestInitializer(HttpRequestInitializer requestInitializer) {
        return (GoogleAuthorizationCodeTokenRequest) super.setRequestInitializer(requestInitializer);
    }

    public GoogleAuthorizationCodeTokenRequest setTokenServerUrl(GenericUrl tokenServerUrl) {
        return (GoogleAuthorizationCodeTokenRequest) super.setTokenServerUrl(tokenServerUrl);
    }

    public GoogleAuthorizationCodeTokenRequest setScopes(String... scopes) {
        return (GoogleAuthorizationCodeTokenRequest) super.setScopes(scopes);
    }

    public GoogleAuthorizationCodeTokenRequest setScopes(Iterable<String> scopes) {
        return (GoogleAuthorizationCodeTokenRequest) super.setScopes((Iterable) scopes);
    }

    public GoogleAuthorizationCodeTokenRequest setGrantType(String grantType) {
        return (GoogleAuthorizationCodeTokenRequest) super.setGrantType(grantType);
    }

    public GoogleAuthorizationCodeTokenRequest setClientAuthentication(HttpExecuteInterceptor clientAuthentication) {
        Preconditions.checkNotNull(clientAuthentication);
        return (GoogleAuthorizationCodeTokenRequest) super.setClientAuthentication(clientAuthentication);
    }

    public GoogleAuthorizationCodeTokenRequest setCode(String code) {
        return (GoogleAuthorizationCodeTokenRequest) super.setCode(code);
    }

    public GoogleAuthorizationCodeTokenRequest setRedirectUri(String redirectUri) {
        Preconditions.checkNotNull(redirectUri);
        return (GoogleAuthorizationCodeTokenRequest) super.setRedirectUri(redirectUri);
    }

    public GoogleTokenResponse execute() throws IOException {
        return (GoogleTokenResponse) executeUnparsed().parseAs(GoogleTokenResponse.class);
    }
}
