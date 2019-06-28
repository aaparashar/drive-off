package com.google.api.client.auth.oauth;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.UrlEncodedParser;
import java.io.IOException;

public abstract class AbstractOAuthGetToken extends GenericUrl {
    public String consumerKey;
    public OAuthSigner signer;
    public HttpTransport transport;
    protected boolean usePost;

    protected AbstractOAuthGetToken(String authorizationServerUrl) {
        super(authorizationServerUrl);
    }

    public final OAuthCredentialsResponse execute() throws IOException {
        HttpRequest request = this.transport.createRequestFactory().buildRequest(this.usePost ? HttpMethod.POST : HttpMethod.GET, this, null);
        createParameters().intercept(request);
        HttpResponse response = request.execute();
        response.setContentLoggingLimit(0);
        Object oauthResponse = new OAuthCredentialsResponse();
        UrlEncodedParser.parse(response.parseAsString(), oauthResponse);
        return oauthResponse;
    }

    public OAuthParameters createParameters() {
        OAuthParameters result = new OAuthParameters();
        result.consumerKey = this.consumerKey;
        result.signer = this.signer;
        return result;
    }
}
