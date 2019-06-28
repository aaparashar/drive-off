package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.ClientParametersAuthentication;
import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.auth.oauth2.CredentialStore;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.common.base.Preconditions;
import java.util.Collections;

public class GoogleAuthorizationCodeFlow extends AuthorizationCodeFlow {
    private final String accessType;
    private final String approvalPrompt;

    public static class Builder extends com.google.api.client.auth.oauth2.AuthorizationCodeFlow.Builder {
        private String accessType;
        private String approvalPrompt;

        public Builder(HttpTransport transport, JsonFactory jsonFactory, String clientId, String clientSecret, Iterable<String> scopes) {
            super(BearerToken.authorizationHeaderAccessMethod(), transport, jsonFactory, new GenericUrl("https://accounts.google.com/o/oauth2/token"), new ClientParametersAuthentication(clientId, clientSecret), clientId, "https://accounts.google.com/o/oauth2/auth");
            setScopes((Iterable) Preconditions.checkNotNull(scopes));
        }

        public Builder(HttpTransport transport, JsonFactory jsonFactory, GoogleClientSecrets clientSecrets, Iterable<String> scopes) {
            super(BearerToken.authorizationHeaderAccessMethod(), transport, jsonFactory, new GenericUrl("https://accounts.google.com/o/oauth2/token"), new ClientParametersAuthentication(clientSecrets.getDetails().getClientId(), clientSecrets.getDetails().getClientSecret()), clientSecrets.getDetails().getClientId(), "https://accounts.google.com/o/oauth2/auth");
            setScopes((Iterable) Preconditions.checkNotNull(scopes));
        }

        public GoogleAuthorizationCodeFlow build() {
            return new GoogleAuthorizationCodeFlow(getMethod(), getTransport(), getJsonFactory(), getTokenServerUrl(), getClientAuthentication(), getClientId(), getAuthorizationServerEncodedUrl(), getCredentialStore(), getRequestInitializer(), getScopes(), this.accessType, this.approvalPrompt);
        }

        public Builder setCredentialStore(CredentialStore credentialStore) {
            return (Builder) super.setCredentialStore(credentialStore);
        }

        public Builder setRequestInitializer(HttpRequestInitializer requestInitializer) {
            return (Builder) super.setRequestInitializer(requestInitializer);
        }

        public Builder setScopes(Iterable<String> scopes) {
            return (Builder) super.setScopes((Iterable) scopes);
        }

        public Builder setScopes(String... scopes) {
            return (Builder) super.setScopes(scopes);
        }

        public Builder setApprovalPrompt(String approvalPrompt) {
            this.approvalPrompt = approvalPrompt;
            return this;
        }

        public final String getApprovalPrompt() {
            return this.approvalPrompt;
        }

        public Builder setAccessType(String accessType) {
            this.accessType = accessType;
            return this;
        }

        public final String getAccessType() {
            return this.accessType;
        }
    }

    protected GoogleAuthorizationCodeFlow(AccessMethod method, HttpTransport transport, JsonFactory jsonFactory, GenericUrl tokenServerUrl, HttpExecuteInterceptor clientAuthentication, String clientId, String authorizationServerEncodedUrl, CredentialStore credentialStore, HttpRequestInitializer requestInitializer, String scopes, String accessType, String approvalPrompt) {
        super(method, transport, jsonFactory, tokenServerUrl, clientAuthentication, clientId, authorizationServerEncodedUrl, credentialStore, requestInitializer, scopes);
        this.accessType = accessType;
        this.approvalPrompt = approvalPrompt;
    }

    public GoogleAuthorizationCodeTokenRequest newTokenRequest(String authorizationCode) {
        return new GoogleAuthorizationCodeTokenRequest(getTransport(), getJsonFactory(), "", "", authorizationCode, "").setClientAuthentication(getClientAuthentication()).setRequestInitializer(getRequestInitializer()).setScopes(new String[]{getScopes()});
    }

    public GoogleAuthorizationCodeRequestUrl newAuthorizationUrl() {
        return new GoogleAuthorizationCodeRequestUrl(getClientId(), "", Collections.singleton(getScopes())).setAccessType(this.accessType).setApprovalPrompt(this.approvalPrompt);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public final String getAccessType() {
        return this.accessType;
    }
}
