package com.google.api.client.auth.oauth2.draft10;

import com.google.api.client.http.UrlEncodedParser;
import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import java.net.URI;
import java.net.URISyntaxException;

@Deprecated
public class AuthorizationResponse extends GenericJson {
    @Key("access_token")
    public String accessToken;
    @Key
    public String code;
    @Key
    public String error;
    @Key("error_description")
    public String errorDescription;
    @Key("error_uri")
    public String errorUri;
    @Key("expires_in")
    public Long expiresIn;
    @Key
    public String scope;
    @Key
    public String state;

    public enum KnownError {
        INVALID_REQUEST,
        INVALID_CLIENT,
        UNAUTHORIZED_CLIENT,
        REDIRECT_URI_MISMATCH,
        ACCESS_DENIED,
        UNSUPPORTED_RESPONSE_TYPE,
        INVALID_SCOPE
    }

    public AuthorizationResponse(String redirectUrl) {
        try {
            URI uri = new URI(redirectUrl);
            UrlEncodedParser.parse(uri.getRawQuery(), (Object) this);
            UrlEncodedParser.parse(uri.getRawFragment(), (Object) this);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public final KnownError getErrorCodeIfKnown() {
        if (this.error != null) {
            try {
                return KnownError.valueOf(this.error.toUpperCase());
            } catch (IllegalArgumentException e) {
            }
        }
        return null;
    }
}
