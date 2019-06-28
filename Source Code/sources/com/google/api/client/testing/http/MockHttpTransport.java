package com.google.api.client.testing.http;

import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class MockHttpTransport extends HttpTransport {
    public static final Set<HttpMethod> DEFAULT_SUPPORTED_OPTIONAL_METHODS = Collections.unmodifiableSet(new HashSet(Arrays.asList(new HttpMethod[]{HttpMethod.HEAD, HttpMethod.PATCH})));
    private EnumSet<HttpMethod> supportedOptionalMethods = EnumSet.of(HttpMethod.HEAD, HttpMethod.PATCH);

    public static class Builder {
        private Set<HttpMethod> supportedOptionalMethods = MockHttpTransport.DEFAULT_SUPPORTED_OPTIONAL_METHODS;

        protected Builder() {
        }

        public MockHttpTransport build() {
            return new MockHttpTransport(this.supportedOptionalMethods);
        }

        public final Set<HttpMethod> getSupportedOptionalMethods() {
            return this.supportedOptionalMethods;
        }

        public Builder setSupportedOptionalMethods(Set<HttpMethod> supportedOptionalMethods) {
            this.supportedOptionalMethods = supportedOptionalMethods;
            return this;
        }
    }

    protected MockHttpTransport(Set<HttpMethod> supportedOptionalMethods) {
        this.supportedOptionalMethods = supportedOptionalMethods.isEmpty() ? EnumSet.noneOf(HttpMethod.class) : EnumSet.copyOf(supportedOptionalMethods);
    }

    public final Set<HttpMethod> getSupportedOptionalMethods() {
        return this.supportedOptionalMethods;
    }

    public void setSupportedOptionalMethods(EnumSet<HttpMethod> supportedOptionalMethods) {
        this.supportedOptionalMethods = supportedOptionalMethods;
    }

    public LowLevelHttpRequest buildDeleteRequest(String url) throws IOException {
        return new MockLowLevelHttpRequest(url);
    }

    public LowLevelHttpRequest buildGetRequest(String url) throws IOException {
        return new MockLowLevelHttpRequest(url);
    }

    public LowLevelHttpRequest buildHeadRequest(String url) throws IOException {
        if (supportsHead()) {
            return new MockLowLevelHttpRequest(url);
        }
        return super.buildHeadRequest(url);
    }

    public LowLevelHttpRequest buildPatchRequest(String url) throws IOException {
        if (supportsPatch()) {
            return new MockLowLevelHttpRequest(url);
        }
        return super.buildPatchRequest(url);
    }

    public LowLevelHttpRequest buildPostRequest(String url) throws IOException {
        return new MockLowLevelHttpRequest(url);
    }

    public LowLevelHttpRequest buildPutRequest(String url) throws IOException {
        return new MockLowLevelHttpRequest(url);
    }

    public boolean supportsHead() {
        return this.supportedOptionalMethods.contains(HttpMethod.HEAD);
    }

    public boolean supportsPatch() {
        return this.supportedOptionalMethods.contains(HttpMethod.PATCH);
    }

    public static Builder builder() {
        return new Builder();
    }
}
