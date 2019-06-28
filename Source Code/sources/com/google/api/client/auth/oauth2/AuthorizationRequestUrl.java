package com.google.api.client.auth.oauth2;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.util.Arrays;

public class AuthorizationRequestUrl extends GenericUrl {
    @Key("client_id")
    private String clientId;
    @Key("redirect_uri")
    private String redirectUri;
    @Key("response_type")
    private String responseTypes;
    @Key("scope")
    private String scopes;
    @Key
    private String state;

    public AuthorizationRequestUrl(String authorizationServerEncodedUrl, String clientId, Iterable<String> responseTypes) {
        super(authorizationServerEncodedUrl);
        Preconditions.checkArgument(getFragment() == null);
        setClientId(clientId);
        setResponseTypes((Iterable) responseTypes);
    }

    public final String getResponseTypes() {
        return this.responseTypes;
    }

    public AuthorizationRequestUrl setResponseTypes(String... responseTypes) {
        return setResponseTypes(Arrays.asList(responseTypes));
    }

    public AuthorizationRequestUrl setResponseTypes(Iterable<String> responseTypes) {
        this.responseTypes = Joiner.on(' ').join((Iterable) responseTypes);
        return this;
    }

    public final String getRedirectUri() {
        return this.redirectUri;
    }

    public AuthorizationRequestUrl setRedirectUri(String redirectUri) {
        this.redirectUri = redirectUri;
        return this;
    }

    public final String getScopes() {
        return this.scopes;
    }

    public AuthorizationRequestUrl setScopes(String... scopes) {
        return setScopes(scopes == null ? null : Arrays.asList(scopes));
    }

    public AuthorizationRequestUrl setScopes(Iterable<String> scopes) {
        this.scopes = scopes == null ? null : Joiner.on(' ').join((Iterable) scopes);
        return this;
    }

    public final String getClientId() {
        return this.clientId;
    }

    public AuthorizationRequestUrl setClientId(String clientId) {
        this.clientId = (String) Preconditions.checkNotNull(clientId);
        return this;
    }

    public final String getState() {
        return this.state;
    }

    public AuthorizationRequestUrl setState(String state) {
        this.state = state;
        return this;
    }
}
