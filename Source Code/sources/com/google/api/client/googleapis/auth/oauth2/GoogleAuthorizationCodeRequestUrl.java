package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;

public class GoogleAuthorizationCodeRequestUrl extends AuthorizationCodeRequestUrl {
    @Key("access_type")
    private String accessType;
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleAuthorizationCodeRequestUrl(String clientId, String redirectUri, Iterable<String> scopes) {
        super("https://accounts.google.com/o/oauth2/auth", clientId);
        setRedirectUri(redirectUri);
        setScopes((Iterable) scopes);
    }

    public GoogleAuthorizationCodeRequestUrl(GoogleClientSecrets clientSecrets, String redirectUri, Iterable<String> scopes) {
        this(clientSecrets.getDetails().getClientId(), redirectUri, (Iterable) scopes);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleAuthorizationCodeRequestUrl setApprovalPrompt(String approvalPrompt) {
        this.approvalPrompt = approvalPrompt;
        return this;
    }

    public final String getAccessType() {
        return this.accessType;
    }

    public GoogleAuthorizationCodeRequestUrl setAccessType(String accessType) {
        this.accessType = accessType;
        return this;
    }

    public GoogleAuthorizationCodeRequestUrl setResponseTypes(String... responseTypes) {
        return (GoogleAuthorizationCodeRequestUrl) super.setResponseTypes(responseTypes);
    }

    public GoogleAuthorizationCodeRequestUrl setResponseTypes(Iterable<String> responseTypes) {
        return (GoogleAuthorizationCodeRequestUrl) super.setResponseTypes((Iterable) responseTypes);
    }

    public GoogleAuthorizationCodeRequestUrl setRedirectUri(String redirectUri) {
        Preconditions.checkNotNull(redirectUri);
        return (GoogleAuthorizationCodeRequestUrl) super.setRedirectUri(redirectUri);
    }

    public GoogleAuthorizationCodeRequestUrl setScopes(String... scopes) {
        Preconditions.checkArgument(scopes.length != 0);
        return (GoogleAuthorizationCodeRequestUrl) super.setScopes(scopes);
    }

    public GoogleAuthorizationCodeRequestUrl setScopes(Iterable<String> scopes) {
        Preconditions.checkArgument(scopes.iterator().hasNext());
        return (GoogleAuthorizationCodeRequestUrl) super.setScopes((Iterable) scopes);
    }

    public GoogleAuthorizationCodeRequestUrl setClientId(String clientId) {
        return (GoogleAuthorizationCodeRequestUrl) super.setClientId(clientId);
    }

    public GoogleAuthorizationCodeRequestUrl setState(String state) {
        return (GoogleAuthorizationCodeRequestUrl) super.setState(state);
    }
}
