package com.google.api.client.auth.oauth2.draft10;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;

@Deprecated
public class AccessTokenErrorResponse extends GenericData {
    @Key
    public String error;
    @Key("error_description")
    public String errorDescription;
    @Key("error_uri")
    public String errorUri;

    public enum KnownError {
        INVALID_REQUEST,
        INVALID_CLIENT,
        UNAUTHORIZED_CLIENT,
        INVALID_GRANT,
        UNSUPPORTED_GRANT_TYPE,
        INVALID_SCOPE
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
