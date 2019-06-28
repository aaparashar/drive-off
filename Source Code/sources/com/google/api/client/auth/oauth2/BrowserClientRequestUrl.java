package com.google.api.client.auth.oauth2;

import java.util.Collections;

public class BrowserClientRequestUrl extends AuthorizationRequestUrl {
    public BrowserClientRequestUrl(String encodedAuthorizationServerUrl, String clientId) {
        super(encodedAuthorizationServerUrl, clientId, Collections.singleton("token"));
    }

    public BrowserClientRequestUrl setResponseTypes(String... responseTypes) {
        return (BrowserClientRequestUrl) super.setResponseTypes(responseTypes);
    }

    public BrowserClientRequestUrl setResponseTypes(Iterable<String> responseTypes) {
        return (BrowserClientRequestUrl) super.setResponseTypes((Iterable) responseTypes);
    }

    public BrowserClientRequestUrl setRedirectUri(String redirectUri) {
        return (BrowserClientRequestUrl) super.setRedirectUri(redirectUri);
    }

    public BrowserClientRequestUrl setScopes(String... scopes) {
        return (BrowserClientRequestUrl) super.setScopes(scopes);
    }

    public BrowserClientRequestUrl setScopes(Iterable<String> scopes) {
        return (BrowserClientRequestUrl) super.setScopes((Iterable) scopes);
    }

    public BrowserClientRequestUrl setClientId(String clientId) {
        return (BrowserClientRequestUrl) super.setClientId(clientId);
    }

    public BrowserClientRequestUrl setState(String state) {
        return (BrowserClientRequestUrl) super.setState(state);
    }
}
