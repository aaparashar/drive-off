package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Arrays;

public class AuthorizationCodeFlow {
    private final String authorizationServerEncodedUrl;
    private final HttpExecuteInterceptor clientAuthentication;
    private final String clientId;
    private final Clock clock;
    private final CredentialStore credentialStore;
    private final JsonFactory jsonFactory;
    private final AccessMethod method;
    private final HttpRequestInitializer requestInitializer;
    private String scopes;
    private final String tokenServerEncodedUrl;
    private final HttpTransport transport;

    public static class Builder {
        private final String authorizationServerEncodedUrl;
        private final HttpExecuteInterceptor clientAuthentication;
        private final String clientId;
        private Clock clock = Clock.SYSTEM;
        private CredentialStore credentialStore;
        private final JsonFactory jsonFactory;
        private final AccessMethod method;
        private HttpRequestInitializer requestInitializer;
        private String scopes;
        private final GenericUrl tokenServerUrl;
        private final HttpTransport transport;

        public Builder(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, HttpExecuteInterceptor clientAuthentication, String clientId, String authorizationServerEncodedUrl) {
            this.method = (AccessMethod) Preconditions.checkNotNull(method);
            this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
            this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
            this.tokenServerUrl = (GenericUrl) Preconditions.checkNotNull(tokenServerUrl);
            this.clientAuthentication = clientAuthentication;
            this.clientId = (String) Preconditions.checkNotNull(clientId);
            this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(authorizationServerEncodedUrl);
        }

        public AuthorizationCodeFlow build() {
            return new AuthorizationCodeFlow(this.method, this.transport, this.jsonFactory, this.tokenServerUrl, this.clientAuthentication, this.clientId, this.authorizationServerEncodedUrl, this.credentialStore, this.requestInitializer, this.scopes, this.clock);
        }

        public final AccessMethod getMethod() {
            return this.method;
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final GenericUrl getTokenServerUrl() {
            return this.tokenServerUrl;
        }

        public final HttpExecuteInterceptor getClientAuthentication() {
            return this.clientAuthentication;
        }

        public final String getClientId() {
            return this.clientId;
        }

        public final String getAuthorizationServerEncodedUrl() {
            return this.authorizationServerEncodedUrl;
        }

        public final CredentialStore getCredentialStore() {
            return this.credentialStore;
        }

        public final Clock getClock() {
            return this.clock;
        }

        public Builder setClock(Clock clock) {
            this.clock = (Clock) Preconditions.checkNotNull(clock);
            return this;
        }

        public Builder setCredentialStore(CredentialStore credentialStore) {
            this.credentialStore = credentialStore;
            return this;
        }

        public final HttpRequestInitializer getRequestInitializer() {
            return this.requestInitializer;
        }

        public Builder setRequestInitializer(HttpRequestInitializer requestInitializer) {
            this.requestInitializer = requestInitializer;
            return this;
        }

        public Builder setScopes(Iterable<String> scopes) {
            this.scopes = scopes == null ? null : Joiner.on(' ').join((Iterable) scopes);
            return this;
        }

        public Builder setScopes(String... scopes) {
            return setScopes(scopes == null ? null : Arrays.asList(scopes));
        }

        public final String getScopes() {
            return this.scopes;
        }
    }

    protected AuthorizationCodeFlow(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, HttpExecuteInterceptor clientAuthentication, String clientId, String authorizationServerEncodedUrl, CredentialStore credentialStore, HttpRequestInitializer requestInitializer, String scopes) {
        this(method, transport, jsonFactory, tokenServerUrl, clientAuthentication, clientId, authorizationServerEncodedUrl, credentialStore, requestInitializer, scopes, Clock.SYSTEM);
    }

    protected AuthorizationCodeFlow(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, HttpExecuteInterceptor clientAuthentication, String clientId, String authorizationServerEncodedUrl, CredentialStore credentialStore, HttpRequestInitializer requestInitializer, String scopes, Clock clock) {
        this.method = (AccessMethod) Preconditions.checkNotNull(method);
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.tokenServerEncodedUrl = ((GenericUrl) Preconditions.checkNotNull(tokenServerUrl)).build();
        this.clientAuthentication = clientAuthentication;
        this.clientId = (String) Preconditions.checkNotNull(clientId);
        this.authorizationServerEncodedUrl = (String) Preconditions.checkNotNull(authorizationServerEncodedUrl);
        this.requestInitializer = requestInitializer;
        this.credentialStore = credentialStore;
        this.scopes = scopes;
        this.clock = (Clock) Preconditions.checkNotNull(clock);
    }

    public AuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new AuthorizationCodeRequestUrl(this.authorizationServerEncodedUrl, this.clientId).setScopes(this.scopes);
    }

    public AuthorizationCodeTokenRequest newTokenRequest(String authorizationCode) {
        return new AuthorizationCodeTokenRequest(this.transport, this.jsonFactory, new GenericUrl(this.tokenServerEncodedUrl), authorizationCode).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setScopes(this.scopes);
    }

    public Credential createAndStoreCredential(TokenResponse response, String userId) throws IOException {
        Credential credential = newCredential(userId).setFromTokenResponse(response);
        if (this.credentialStore != null) {
            this.credentialStore.store(userId, credential);
        }
        return credential;
    }

    public Credential loadCredential(String userId) throws IOException {
        if (this.credentialStore == null) {
            return null;
        }
        Credential credential = newCredential(userId);
        if (this.credentialStore.load(userId, credential)) {
            return credential;
        }
        return null;
    }

    private Credential newCredential(String userId) {
        com.google.api.client.auth.oauth2.Credential.Builder builder = new com.google.api.client.auth.oauth2.Credential.Builder(this.method).setTransport(this.transport).setJsonFactory(this.jsonFactory).setTokenServerEncodedUrl(this.tokenServerEncodedUrl).setClientAuthentication(this.clientAuthentication).setRequestInitializer(this.requestInitializer).setClock(this.clock);
        if (this.credentialStore != null) {
            builder.addRefreshListener(new CredentialStoreRefreshListener(userId, this.credentialStore));
        }
        return builder.build();
    }

    public final AccessMethod getMethod() {
        return this.method;
    }

    public final HttpTransport getTransport() {
        return this.transport;
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public final String getTokenServerEncodedUrl() {
        return this.tokenServerEncodedUrl;
    }

    public final HttpExecuteInterceptor getClientAuthentication() {
        return this.clientAuthentication;
    }

    public final String getClientId() {
        return this.clientId;
    }

    public final String getAuthorizationServerEncodedUrl() {
        return this.authorizationServerEncodedUrl;
    }

    public final CredentialStore getCredentialStore() {
        return this.credentialStore;
    }

    public final HttpRequestInitializer getRequestInitializer() {
        return this.requestInitializer;
    }

    public final String getScopes() {
        return this.scopes;
    }

    public final Clock getClock() {
        return this.clock;
    }
}
