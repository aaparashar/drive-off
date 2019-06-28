package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.jsontoken.JsonWebSignature.Header;
import com.google.api.client.auth.jsontoken.JsonWebToken.Payload;
import com.google.api.client.auth.jsontoken.RsaSHA256Signer;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.auth.security.PrivateKeys;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets.Details;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Clock;
import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.List;

public class GoogleCredential extends Credential {
    private String serviceAccountId;
    private PrivateKey serviceAccountPrivateKey;
    private String serviceAccountScopes;
    private String serviceAccountUser;

    public static class Builder extends com.google.api.client.auth.oauth2.Credential.Builder {
        private String serviceAccountId;
        private PrivateKey serviceAccountPrivateKey;
        private String serviceAccountScopes;
        private String serviceAccountUser;

        public Builder() {
            super(BearerToken.authorizationHeaderAccessMethod());
            setTokenServerEncodedUrl("https://accounts.google.com/o/oauth2/token");
        }

        public GoogleCredential build() {
            return new GoogleCredential(getMethod(), getTransport(), getJsonFactory(), getTokenServerUrl() == null ? null : getTokenServerUrl().build(), getClientAuthentication(), getRequestInitializer(), getRefreshListeners(), this.serviceAccountId, this.serviceAccountScopes, this.serviceAccountPrivateKey, this.serviceAccountUser, getClock());
        }

        public Builder setTransport(HttpTransport transport) {
            return (Builder) super.setTransport(transport);
        }

        public Builder setJsonFactory(JsonFactory jsonFactory) {
            return (Builder) super.setJsonFactory(jsonFactory);
        }

        public Builder setClock(Clock clock) {
            return (Builder) super.setClock(clock);
        }

        public Builder setClientSecrets(String clientId, String clientSecret) {
            setClientAuthentication(new ClientParametersAuthentication(clientId, clientSecret));
            return this;
        }

        public Builder setClientSecrets(GoogleClientSecrets clientSecrets) {
            Details details = clientSecrets.getDetails();
            setClientAuthentication(new ClientParametersAuthentication(details.getClientId(), details.getClientSecret()));
            return this;
        }

        public final String getServiceAccountId() {
            return this.serviceAccountId;
        }

        public Builder setServiceAccountId(String serviceAccountId) {
            this.serviceAccountId = serviceAccountId;
            return this;
        }

        public final String getServiceAccountScopes() {
            return this.serviceAccountScopes;
        }

        public Builder setServiceAccountScopes(String... serviceAccountScopes) {
            return setServiceAccountScopes(serviceAccountScopes == null ? null : Arrays.asList(serviceAccountScopes));
        }

        public Builder setServiceAccountScopes(Iterable<String> serviceAccountScopes) {
            this.serviceAccountScopes = serviceAccountScopes == null ? null : Joiner.on(' ').join((Iterable) serviceAccountScopes);
            return this;
        }

        public final PrivateKey getServiceAccountPrivateKey() {
            return this.serviceAccountPrivateKey;
        }

        public Builder setServiceAccountPrivateKey(PrivateKey serviceAccountPrivateKey) {
            this.serviceAccountPrivateKey = serviceAccountPrivateKey;
            return this;
        }

        public Builder setServiceAccountPrivateKeyFromP12File(File p12File) throws GeneralSecurityException, IOException {
            this.serviceAccountPrivateKey = PrivateKeys.loadFromP12File(p12File, "notasecret", "privatekey", "notasecret");
            return this;
        }

        public final String getServiceAccountUser() {
            return this.serviceAccountUser;
        }

        public Builder setServiceAccountUser(String serviceAccountUser) {
            this.serviceAccountUser = serviceAccountUser;
            return this;
        }

        public Builder setRequestInitializer(HttpRequestInitializer requestInitializer) {
            return (Builder) super.setRequestInitializer(requestInitializer);
        }

        public Builder addRefreshListener(CredentialRefreshListener refreshListener) {
            return (Builder) super.addRefreshListener(refreshListener);
        }

        public Builder setRefreshListeners(List<CredentialRefreshListener> refreshListeners) {
            return (Builder) super.setRefreshListeners(refreshListeners);
        }

        public Builder setTokenServerUrl(GenericUrl tokenServerUrl) {
            return (Builder) super.setTokenServerUrl(tokenServerUrl);
        }

        public Builder setTokenServerEncodedUrl(String tokenServerEncodedUrl) {
            return (Builder) super.setTokenServerEncodedUrl(tokenServerEncodedUrl);
        }

        public Builder setClientAuthentication(HttpExecuteInterceptor clientAuthentication) {
            return (Builder) super.setClientAuthentication(clientAuthentication);
        }
    }

    public GoogleCredential() {
        super(BearerToken.authorizationHeaderAccessMethod(), null, null, "https://accounts.google.com/o/oauth2/token", null, null, null);
    }

    protected GoogleCredential(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, String tokenServerEncodedUrl, HttpExecuteInterceptor clientAuthentication, HttpRequestInitializer requestInitializer, List<CredentialRefreshListener> refreshListeners, String serviceAccountId, String serviceAccountScopes, PrivateKey serviceAccountPrivateKey, String serviceAccountUser) {
        this(method, transport, jsonFactory, tokenServerEncodedUrl, clientAuthentication, requestInitializer, refreshListeners, serviceAccountId, serviceAccountScopes, serviceAccountPrivateKey, serviceAccountUser, Clock.SYSTEM);
    }

    protected GoogleCredential(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, String tokenServerEncodedUrl, HttpExecuteInterceptor clientAuthentication, HttpRequestInitializer requestInitializer, List<CredentialRefreshListener> refreshListeners, String serviceAccountId, String serviceAccountScopes, PrivateKey serviceAccountPrivateKey, String serviceAccountUser, Clock clock) {
        super(method, transport, jsonFactory, tokenServerEncodedUrl, clientAuthentication, requestInitializer, refreshListeners, clock);
        if (serviceAccountPrivateKey == null) {
            boolean z = serviceAccountId == null && serviceAccountScopes == null && serviceAccountUser == null;
            Preconditions.checkArgument(z);
            return;
        }
        this.serviceAccountId = (String) Preconditions.checkNotNull(serviceAccountId);
        this.serviceAccountScopes = (String) Preconditions.checkNotNull(serviceAccountScopes);
        this.serviceAccountPrivateKey = serviceAccountPrivateKey;
        this.serviceAccountUser = serviceAccountUser;
    }

    public GoogleCredential setAccessToken(String accessToken) {
        return (GoogleCredential) super.setAccessToken(accessToken);
    }

    public GoogleCredential setRefreshToken(String refreshToken) {
        if (refreshToken != null) {
            boolean z = (getJsonFactory() == null || getTransport() == null || getClientAuthentication() == null) ? false : true;
            Preconditions.checkArgument(z, "Please use the Builder and call setJsonFactory, setTransport and setClientSecrets");
        }
        return (GoogleCredential) super.setRefreshToken(refreshToken);
    }

    public GoogleCredential setExpirationTimeMilliseconds(Long expirationTimeMilliseconds) {
        return (GoogleCredential) super.setExpirationTimeMilliseconds(expirationTimeMilliseconds);
    }

    public GoogleCredential setExpiresInSeconds(Long expiresIn) {
        return (GoogleCredential) super.setExpiresInSeconds(expiresIn);
    }

    public GoogleCredential setFromTokenResponse(TokenResponse tokenResponse) {
        return (GoogleCredential) super.setFromTokenResponse(tokenResponse);
    }

    protected TokenResponse executeRefreshToken() throws IOException {
        if (this.serviceAccountPrivateKey == null) {
            return super.executeRefreshToken();
        }
        Header header = new Header();
        header.setAlgorithm("RS256");
        header.setType("JWT");
        Payload payload = new Payload(getClock());
        long currentTime = getClock().currentTimeMillis();
        payload.setIssuer(this.serviceAccountId).setAudience("https://accounts.google.com/o/oauth2/token").setIssuedAtTimeSeconds(Long.valueOf(currentTime / 1000)).setExpirationTimeSeconds(Long.valueOf((currentTime / 1000) + 3600)).setPrincipal(this.serviceAccountUser);
        payload.put("scope", (Object) this.serviceAccountScopes);
        try {
            String assertion = RsaSHA256Signer.sign(this.serviceAccountPrivateKey, getJsonFactory(), header, payload);
            TokenRequest request = new TokenRequest(getTransport(), getJsonFactory(), new GenericUrl("https://accounts.google.com/o/oauth2/token"), "assertion");
            request.put("assertion_type", (Object) "http://oauth.net/grant_type/jwt/1.0/bearer");
            request.put("assertion", (Object) assertion);
            return request.execute();
        } catch (GeneralSecurityException exception) {
            IOException e = new IOException();
            e.initCause(exception);
            throw e;
        }
    }

    public final String getServiceAccountId() {
        return this.serviceAccountId;
    }

    public final String getServiceAccountScopes() {
        return this.serviceAccountScopes;
    }

    public final PrivateKey getServiceAccountPrivateKey() {
        return this.serviceAccountPrivateKey;
    }

    public final String getServiceAccountUser() {
        return this.serviceAccountUser;
    }
}
