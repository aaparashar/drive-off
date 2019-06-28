package com.google.api.client.auth.oauth2.draft10;

import com.google.api.client.auth.oauth2.draft10.AccessTokenRequest.RefreshTokenGrant;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Data;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.EnumSet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Deprecated
public class AccessProtectedResource implements HttpExecuteInterceptor, HttpRequestInitializer, HttpUnsuccessfulResponseHandler {
    private static final EnumSet<HttpMethod> ALLOWED_METHODS = EnumSet.of(HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE);
    static final String HEADER_PREFIX = "OAuth ";
    static final Logger LOGGER = Logger.getLogger(AccessProtectedResource.class.getName());
    private String accessToken;
    private final String authorizationServerUrl;
    private final String clientId;
    private final String clientSecret;
    private final JsonFactory jsonFactory;
    private final Method method;
    private final String refreshToken;
    private final Lock tokenLock = new ReentrantLock();
    private final HttpTransport transport;

    public enum Method {
        AUTHORIZATION_HEADER,
        QUERY_PARAMETER,
        FORM_ENCODED_BODY
    }

    public AccessProtectedResource(String accessToken, Method method) {
        this.accessToken = accessToken;
        this.method = (Method) Preconditions.checkNotNull(method);
        this.transport = null;
        this.jsonFactory = null;
        this.authorizationServerUrl = null;
        this.clientId = null;
        this.clientSecret = null;
        this.refreshToken = null;
    }

    public AccessProtectedResource(String accessToken, Method method, HttpTransport transport, JsonFactory jsonFactory, String authorizationServerUrl, String clientId, String clientSecret, String refreshToken) {
        this.accessToken = accessToken;
        this.method = (Method) Preconditions.checkNotNull(method);
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.authorizationServerUrl = (String) Preconditions.checkNotNull(authorizationServerUrl);
        this.clientId = (String) Preconditions.checkNotNull(clientId);
        this.clientSecret = (String) Preconditions.checkNotNull(clientSecret);
        this.refreshToken = refreshToken;
    }

    public final String getAccessToken() {
        this.tokenLock.lock();
        try {
            String str = this.accessToken;
            return str;
        } finally {
            this.tokenLock.unlock();
        }
    }

    public final void setAccessToken(String accessToken) {
        this.tokenLock.lock();
        try {
            this.accessToken = accessToken;
            onAccessToken(accessToken);
        } finally {
            this.tokenLock.unlock();
        }
    }

    public final Method getMethod() {
        return this.method;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    public String getAuthorizationServerUrl() {
        return this.authorizationServerUrl;
    }

    public String getClientId() {
        return this.clientId;
    }

    public String getClientSecret() {
        return this.clientSecret;
    }

    public String getRefreshToken() {
        return this.refreshToken;
    }

    public final boolean refreshToken() throws IOException {
        this.tokenLock.lock();
        try {
            boolean executeRefreshToken = executeRefreshToken();
            return executeRefreshToken;
        } finally {
            this.tokenLock.unlock();
        }
    }

    public final void initialize(HttpRequest request) throws IOException {
        request.setInterceptor(this);
        request.setUnsuccessfulResponseHandler(this);
    }

    public void intercept(HttpRequest request) throws IOException {
        String accessToken = getAccessToken();
        if (accessToken != null) {
            switch (this.method) {
                case AUTHORIZATION_HEADER:
                    request.getHeaders().setAuthorization(HEADER_PREFIX + accessToken);
                    return;
                case QUERY_PARAMETER:
                    request.getUrl().set("oauth_token", accessToken);
                    return;
                case FORM_ENCODED_BODY:
                    Preconditions.checkArgument(ALLOWED_METHODS.contains(request.getMethod()), "expected one of these HTTP methods: %s", ALLOWED_METHODS);
                    Data.mapOf(UrlEncodedContent.getContent(request).getData()).put("oauth_token", accessToken);
                    return;
                default:
                    return;
            }
        }
    }

    private String getAccessTokenFromRequest(HttpRequest request) {
        switch (this.method) {
            case AUTHORIZATION_HEADER:
                String header = request.getHeaders().getAuthorization();
                if (header == null || !header.startsWith(HEADER_PREFIX)) {
                    return null;
                }
                return header.substring(HEADER_PREFIX.length());
            case QUERY_PARAMETER:
                Object param = request.getUrl().get("oauth_token");
                if (param != null) {
                    return param.toString();
                }
                return null;
            default:
                Object bodyParam = Data.mapOf(UrlEncodedContent.getContent(request).getData()).get("oauth_token");
                if (bodyParam == null) {
                    return null;
                }
                return bodyParam.toString();
        }
    }

    public boolean handleResponse(HttpRequest request, HttpResponse response, boolean retrySupported) {
        if (response.getStatusCode() == 401) {
            try {
                this.tokenLock.lock();
                boolean z = !Objects.equal(this.accessToken, getAccessTokenFromRequest(request)) || refreshToken();
                this.tokenLock.unlock();
                return z;
            } catch (HttpResponseException e) {
                try {
                    LOGGER.severe(e.getMessage());
                } catch (IOException exception) {
                    LOGGER.severe(exception.toString());
                }
            } catch (Throwable th) {
                this.tokenLock.unlock();
            }
        }
        return false;
    }

    protected boolean executeRefreshToken() throws IOException {
        if (this.refreshToken != null) {
            return executeAccessTokenRequest(new RefreshTokenGrant(this.transport, this.jsonFactory, this.authorizationServerUrl, this.clientId, this.clientSecret, this.refreshToken));
        }
        return false;
    }

    protected final boolean executeAccessTokenRequest(AccessTokenRequest request) throws IOException {
        String newAccessToken;
        try {
            newAccessToken = request.execute().accessToken;
        } catch (HttpResponseException e) {
            newAccessToken = null;
        }
        setAccessToken(newAccessToken);
        if (newAccessToken != null) {
            return true;
        }
        return false;
    }

    protected void onAccessToken(String accessToken) {
    }
}
