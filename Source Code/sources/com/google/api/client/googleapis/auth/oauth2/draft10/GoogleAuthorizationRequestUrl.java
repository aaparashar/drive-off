package com.google.api.client.googleapis.auth.oauth2.draft10;

import com.google.api.client.auth.oauth2.draft10.AuthorizationRequestUrl;
import com.google.api.client.util.Key;

@Deprecated
public class GoogleAuthorizationRequestUrl extends AuthorizationRequestUrl {
    public static final String AUTHORIZATION_SERVER_URL = "https://accounts.google.com/o/oauth2/auth";
    @Key("access_type")
    private String accessType;
    @Key("approval_prompt")
    private String approvalPrompt;

    public GoogleAuthorizationRequestUrl() {
        super("https://accounts.google.com/o/oauth2/auth");
    }

    public GoogleAuthorizationRequestUrl(String clientId, String redirectUri, String scope) {
        super("https://accounts.google.com/o/oauth2/auth", clientId);
        this.redirectUri = redirectUri;
        this.scope = scope;
    }

    public final String getApprovalPrompt() {
        return this.approvalPrompt;
    }

    public GoogleAuthorizationRequestUrl setApprovalPrompt(String approvalPrompt) {
        this.approvalPrompt = approvalPrompt;
        return this;
    }

    public final String getAccessType() {
        return this.accessType;
    }

    public GoogleAuthorizationRequestUrl setAccessType(String accessType) {
        this.accessType = accessType;
        return this;
    }
}
