package com.google.api.client.auth.oauth2;

import java.util.Collections;

public class AuthorizationCodeRequestUrl extends AuthorizationRequestUrl {
    public AuthorizationCodeRequestUrl(String authorizationServerEncodedUrl, String clientId) {
        super(authorizationServerEncodedUrl, clientId, Collections.singleton("code"));
    }

    public AuthorizationCodeRequestUrl setResponseTypes(String... responseTypes) {
        return (AuthorizationCodeRequestUrl) super.setResponseTypes(responseTypes);
    }

    public AuthorizationCodeRequestUrl setResponseTypes(Iterable<String> responseTypes) {
        return (AuthorizationCodeRequestUrl) super.setResponseTypes((Iterable) responseTypes);
    }

    public AuthorizationCodeRequestUrl setRedirectUri(String redirectUri) {
        return (AuthorizationCodeRequestUrl) super.setRedirectUri(redirectUri);
    }

    public AuthorizationCodeRequestUrl setScopes(String... scopes) {
        return (AuthorizationCodeRequestUrl) super.setScopes(scopes);
    }

    public AuthorizationCodeRequestUrl setScopes(Iterable<String> scopes) {
        return (AuthorizationCodeRequestUrl) super.setScopes((Iterable) scopes);
    }

    public AuthorizationCodeRequestUrl setClientId(String clientId) {
        return (AuthorizationCodeRequestUrl) super.setClientId(clientId);
    }

    public AuthorizationCodeRequestUrl setState(String state) {
        return (AuthorizationCodeRequestUrl) super.setState(state);
    }
}
