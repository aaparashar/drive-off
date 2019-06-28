package com.google.api.client.auth.oauth2.draft10;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

@Deprecated
public class AuthorizationRequestUrl extends GenericUrl {
    @Key("client_id")
    public String clientId;
    @Key("redirect_uri")
    public String redirectUri;
    @Key("response_type")
    public String responseType;
    @Key
    public String scope;
    @Key
    public String state;

    public enum ResponseType {
        CODE,
        TOKEN,
        CODE_AND_TOKEN;

        public void set(AuthorizationRequestUrl url) {
            url.responseType = toString().toLowerCase();
        }
    }

    public AuthorizationRequestUrl(String encodedAuthorizationServerUrl) {
        super(encodedAuthorizationServerUrl);
        this.responseType = "code";
    }

    public AuthorizationRequestUrl(String encodedAuthorizationServerUrl, String clientId) {
        this(encodedAuthorizationServerUrl);
        this.clientId = clientId;
    }
}
