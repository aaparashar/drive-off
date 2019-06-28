package com.google.api.client.googleapis.auth.oauth;

import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.util.Key;

@Deprecated
public final class GoogleOAuthGetTemporaryToken extends OAuthGetTemporaryToken {
    @Key("xoauth_displayname")
    public String displayName;
    @Key
    public String scope;

    public GoogleOAuthGetTemporaryToken() {
        super("https://www.google.com/accounts/OAuthGetRequestToken");
    }

    public OAuthParameters createParameters() {
        OAuthParameters result = super.createParameters();
        result.callback = this.callback;
        return result;
    }
}
