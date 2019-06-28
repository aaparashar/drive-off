package com.google.api.client.auth.oauth2.draft10;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.http.json.JsonHttpParser;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.Key;
import com.google.common.base.Preconditions;
import java.io.IOException;

@Deprecated
public class AccessTokenRequest extends GenericData {
    public String authorizationServerUrl;
    @Key("client_id")
    public String clientId;
    public String clientSecret;
    @Key("grant_type")
    public GrantType grantType;
    public JsonFactory jsonFactory;
    @Key
    public String scope;
    public HttpTransport transport;
    public boolean useBasicAuthorization;

    public enum GrantType {
        AUTHORIZATION_CODE,
        PASSWORD,
        ASSERTION,
        REFRESH_TOKEN,
        NONE
    }

    @Deprecated
    public static class AssertionGrant extends AccessTokenRequest {
        @Key
        public String assertion;
        @Key("assertion_type")
        public String assertionType;

        public AssertionGrant() {
            this.grantType = GrantType.ASSERTION;
        }

        public AssertionGrant(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String assertionType, String assertion) {
            super(transport, jsonFactory, authorizationServerUrl);
            this.grantType = GrantType.ASSERTION;
            this.assertionType = (String) Preconditions.checkNotNull(assertionType);
            this.assertion = (String) Preconditions.checkNotNull(assertion);
        }
    }

    @Deprecated
    public static class AuthorizationCodeGrant extends AccessTokenRequest {
        @Key
        public String code;
        @Key("redirect_uri")
        public String redirectUri;

        public AuthorizationCodeGrant() {
            this.grantType = GrantType.AUTHORIZATION_CODE;
        }

        public AuthorizationCodeGrant(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientId, String clientSecret, String code, String redirectUri) {
            super(transport, jsonFactory, authorizationServerUrl, clientId, clientSecret);
            this.grantType = GrantType.AUTHORIZATION_CODE;
            this.code = (String) Preconditions.checkNotNull(code);
            this.redirectUri = (String) Preconditions.checkNotNull(redirectUri);
        }
    }

    @Deprecated
    public static class RefreshTokenGrant extends AccessTokenRequest {
        @Key("refresh_token")
        public String refreshToken;

        public RefreshTokenGrant() {
            this.grantType = GrantType.REFRESH_TOKEN;
        }

        public RefreshTokenGrant(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientId, String clientSecret, String refreshToken) {
            super(transport, jsonFactory, authorizationServerUrl, clientId, clientSecret);
            this.grantType = GrantType.REFRESH_TOKEN;
            this.refreshToken = (String) Preconditions.checkNotNull(refreshToken);
        }
    }

    @Deprecated
    public static class ResourceOwnerPasswordCredentialsGrant extends AccessTokenRequest {
        public String password;
        @Key
        public String username;

        public ResourceOwnerPasswordCredentialsGrant() {
            this.grantType = GrantType.PASSWORD;
        }

        public ResourceOwnerPasswordCredentialsGrant(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientId, String clientSecret, String username, String password) {
            super(transport, jsonFactory, authorizationServerUrl, clientId, clientSecret);
            this.grantType = GrantType.PASSWORD;
            this.username = (String) Preconditions.checkNotNull(username);
            this.password = (String) Preconditions.checkNotNull(password);
        }
    }

    public AccessTokenRequest() {
        this.grantType = GrantType.NONE;
    }

    protected AccessTokenRequest(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl) {
        this();
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.authorizationServerUrl = (String) Preconditions.checkNotNull(authorizationServerUrl);
    }

    protected AccessTokenRequest(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientSecret) {
        this(transport, jsonFactory, authorizationServerUrl);
        this.clientSecret = (String) Preconditions.checkNotNull(clientSecret);
    }

    public AccessTokenRequest(HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientId, String clientSecret) {
        this(transport, jsonFactory, authorizationServerUrl, clientSecret);
        this.clientId = (String) Preconditions.checkNotNull(clientId);
    }

    public final HttpResponse executeUnparsed() throws IOException {
        HttpRequest request = this.transport.createRequestFactory().buildPostRequest(new GenericUrl(this.authorizationServerUrl), new UrlEncodedContent(this));
        request.addParser(new JsonHttpParser(this.jsonFactory));
        if (this.useBasicAuthorization) {
            request.getHeaders().setBasicAuthentication(this.clientId, this.clientSecret);
        } else {
            put("client_secret", (Object) this.clientSecret);
        }
        return request.execute();
    }

    public final AccessTokenResponse execute() throws IOException {
        return (AccessTokenResponse) executeUnparsed().parseAs(AccessTokenResponse.class);
    }
}
