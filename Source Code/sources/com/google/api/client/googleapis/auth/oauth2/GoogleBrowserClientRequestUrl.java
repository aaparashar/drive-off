package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.auth.oauth2.BrowserClientRequestUrl;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;

public class GoogleBrowserClientRequestUrl extends BrowserClientRequestUrl {
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleBrowserClientRequestUrl(String clientId, String redirectUri, Iterable<String> scopes) {
        super("https://accounts.google.com/o/oauth2/auth", clientId);
        setRedirectUri(redirectUri);
        setScopes((Iterable) scopes);
    }

    public GoogleBrowserClientRequestUrl(GoogleClientSecrets clientSecrets, String redirectUri, Iterable<String> scopes) {
        this(clientSecrets.getDetails().getClientId(), redirectUri, (Iterable) scopes);
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleBrowserClientRequestUrl setApprovalPrompt(String approvalPrompt) {
        this.approvalPrompt = approvalPrompt;
        return this;
    }

    public GoogleBrowserClientRequestUrl setResponseTypes(String... responseTypes) {
        return (GoogleBrowserClientRequestUrl) super.setResponseTypes(responseTypes);
    }

    public GoogleBrowserClientRequestUrl setResponseTypes(Iterable<String> responseTypes) {
        return (GoogleBrowserClientRequestUrl) super.setResponseTypes((Iterable) responseTypes);
    }

    public GoogleBrowserClientRequestUrl setRedirectUri(String redirectUri) {
        return (GoogleBrowserClientRequestUrl) super.setRedirectUri(redirectUri);
    }

    public GoogleBrowserClientRequestUrl setScopes(String... scopes) {
        Preconditions.checkArgument(scopes.length != 0);
        return (GoogleBrowserClientRequestUrl) super.setScopes(scopes);
    }

    public GoogleBrowserClientRequestUrl setScopes(Iterable<String> scopes) {
        Preconditions.checkArgument(scopes.iterator().hasNext());
        return (GoogleBrowserClientRequestUrl) super.setScopes((Iterable) scopes);
    }

    public GoogleBrowserClientRequestUrl setClientId(String clientId) {
        return (GoogleBrowserClientRequestUrl) super.setClientId(clientId);
    }

    public GoogleBrowserClientRequestUrl setState(String state) {
        return (GoogleBrowserClientRequestUrl) super.setState(state);
    }
}
