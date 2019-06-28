package com.google.api.client.auth.oauth;

public class OAuthGetAccessToken extends AbstractOAuthGetToken {
    public String temporaryToken;
    public String verifier;

    public OAuthGetAccessToken(String authorizationServerUrl) {
        super(authorizationServerUrl);
    }

    public OAuthParameters createParameters() {
        OAuthParameters result = super.createParameters();
        result.token = this.temporaryToken;
        result.verifier = this.verifier;
        return result;
    }
}
