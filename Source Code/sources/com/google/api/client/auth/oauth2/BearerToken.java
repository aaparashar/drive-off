package com.google.api.client.auth.oauth2;

import com.google.api.client.auth.oauth2.Credential.AccessMethod;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.UrlEncodedContent;
import com.google.api.client.util.Data;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.Map;

public class BearerToken {
    static final String PARAM_NAME = "access_token";

    static final class AuthorizationHeaderAccessMethod implements AccessMethod {
        static final String HEADER_PREFIX = "Bearer ";

        AuthorizationHeaderAccessMethod() {
        }

        public void intercept(HttpRequest request, String accessToken) throws IOException {
            request.getHeaders().setAuthorization("Bearer " + accessToken);
        }

        public String getAccessTokenFromRequest(HttpRequest request) {
            String header = request.getHeaders().getAuthorization();
            if (header == null || !header.startsWith("Bearer ")) {
                return null;
            }
            return header.substring("Bearer ".length());
        }
    }

    static final class FormEncodedBodyAccessMethod implements AccessMethod {
        FormEncodedBodyAccessMethod() {
        }

        public void intercept(HttpRequest request, String accessToken) throws IOException {
            Preconditions.checkArgument(request.getMethod() != HttpMethod.GET, "HTTP GET method is not supported");
            getData(request).put(BearerToken.PARAM_NAME, accessToken);
        }

        public String getAccessTokenFromRequest(HttpRequest request) {
            Object bodyParam = getData(request).get(BearerToken.PARAM_NAME);
            return bodyParam == null ? null : bodyParam.toString();
        }

        private static Map<String, Object> getData(HttpRequest request) {
            return Data.mapOf(UrlEncodedContent.getContent(request).getData());
        }
    }

    static final class QueryParameterAccessMethod implements AccessMethod {
        QueryParameterAccessMethod() {
        }

        public void intercept(HttpRequest request, String accessToken) throws IOException {
            request.getUrl().set(BearerToken.PARAM_NAME, accessToken);
        }

        public String getAccessTokenFromRequest(HttpRequest request) {
            Object param = request.getUrl().get(BearerToken.PARAM_NAME);
            return param == null ? null : param.toString();
        }
    }

    public static AccessMethod authorizationHeaderAccessMethod() {
        return new AuthorizationHeaderAccessMethod();
    }

    public static AccessMethod formEncodedBodyAccessMethod() {
        return new FormEncodedBodyAccessMethod();
    }

    public static AccessMethod queryParameterAccessMethod() {
        return new QueryParameterAccessMethod();
    }
}
