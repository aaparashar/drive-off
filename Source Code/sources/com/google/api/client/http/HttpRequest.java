package com.google.api.client.http;

import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class HttpRequest {
    public static final String USER_AGENT_SUFFIX = "Google-HTTP-Java-Client/1.10.3-beta (gzip)";
    public static final String VERSION = "1.10.3-beta";
    private boolean allowEmptyContent = true;
    private BackOffPolicy backOffPolicy;
    private int connectTimeout = 20000;
    private HttpContent content;
    private int contentLoggingLimit = 16384;
    @Deprecated
    private final Map<String, HttpParser> contentTypeToParserMap = new HashMap();
    private boolean enableGZipContent;
    private boolean followRedirects = true;
    private HttpHeaders headers = new HttpHeaders();
    private HttpExecuteInterceptor interceptor;
    private boolean loggingEnabled = true;
    private HttpMethod method;
    private int numRetries = 10;
    private ObjectParser objectParser;
    private int readTimeout = 20000;
    private HttpHeaders responseHeaders = new HttpHeaders();
    private boolean retryOnExecuteIOException = false;
    private boolean throwExceptionOnExecuteError = true;
    private final HttpTransport transport;
    private HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler;
    private GenericUrl url;

    HttpRequest(HttpTransport transport, HttpMethod method) {
        this.transport = transport;
        this.method = method;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public HttpMethod getMethod() {
        return this.method;
    }

    public HttpRequest setMethod(HttpMethod method) {
        this.method = (HttpMethod) Preconditions.checkNotNull(method);
        return this;
    }

    public GenericUrl getUrl() {
        return this.url;
    }

    public HttpRequest setUrl(GenericUrl url) {
        this.url = (GenericUrl) Preconditions.checkNotNull(url);
        return this;
    }

    public HttpContent getContent() {
        return this.content;
    }

    public HttpRequest setContent(HttpContent content) {
        this.content = content;
        return this;
    }

    public boolean getEnableGZipContent() {
        return this.enableGZipContent;
    }

    public HttpRequest setEnableGZipContent(boolean enableGZipContent) {
        this.enableGZipContent = enableGZipContent;
        return this;
    }

    public BackOffPolicy getBackOffPolicy() {
        return this.backOffPolicy;
    }

    public HttpRequest setBackOffPolicy(BackOffPolicy backOffPolicy) {
        this.backOffPolicy = backOffPolicy;
        return this;
    }

    public int getContentLoggingLimit() {
        return this.contentLoggingLimit;
    }

    public HttpRequest setContentLoggingLimit(int contentLoggingLimit) {
        Preconditions.checkArgument(contentLoggingLimit >= 0, "The content logging limit must be non-negative.");
        this.contentLoggingLimit = contentLoggingLimit;
        return this;
    }

    public boolean isLoggingEnabled() {
        return this.loggingEnabled;
    }

    public HttpRequest setLoggingEnabled(boolean loggingEnabled) {
        this.loggingEnabled = loggingEnabled;
        return this;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public HttpRequest setConnectTimeout(int connectTimeout) {
        Preconditions.checkArgument(connectTimeout >= 0);
        this.connectTimeout = connectTimeout;
        return this;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public HttpRequest setReadTimeout(int readTimeout) {
        Preconditions.checkArgument(readTimeout >= 0);
        this.readTimeout = readTimeout;
        return this;
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public HttpRequest setHeaders(HttpHeaders headers) {
        this.headers = (HttpHeaders) Preconditions.checkNotNull(headers);
        return this;
    }

    public HttpHeaders getResponseHeaders() {
        return this.responseHeaders;
    }

    public HttpRequest setResponseHeaders(HttpHeaders responseHeaders) {
        this.responseHeaders = (HttpHeaders) Preconditions.checkNotNull(responseHeaders);
        return this;
    }

    public HttpExecuteInterceptor getInterceptor() {
        return this.interceptor;
    }

    public HttpRequest setInterceptor(HttpExecuteInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

    public HttpUnsuccessfulResponseHandler getUnsuccessfulResponseHandler() {
        return this.unsuccessfulResponseHandler;
    }

    public HttpRequest setUnsuccessfulResponseHandler(HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler) {
        this.unsuccessfulResponseHandler = unsuccessfulResponseHandler;
        return this;
    }

    public HttpRequest setAllowEmptyContent(boolean allowEmptyContent) {
        this.allowEmptyContent = allowEmptyContent;
        return this;
    }

    public boolean isAllowEmptyContent() {
        return this.allowEmptyContent;
    }

    public int getNumberOfRetries() {
        return this.numRetries;
    }

    public HttpRequest setNumberOfRetries(int numRetries) {
        Preconditions.checkArgument(numRetries >= 0);
        this.numRetries = numRetries;
        return this;
    }

    @Deprecated
    public void addParser(HttpParser parser) {
        this.contentTypeToParserMap.put(normalizeMediaType(parser.getContentType()), parser);
    }

    public void setParser(ObjectParser parser) {
        this.objectParser = parser;
    }

    @Deprecated
    public final HttpParser getParser(String contentType) {
        return (HttpParser) this.contentTypeToParserMap.get(normalizeMediaType(contentType));
    }

    public final ObjectParser getParser() {
        return this.objectParser;
    }

    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public HttpRequest setFollowRedirects(boolean followRedirects) {
        this.followRedirects = followRedirects;
        return this;
    }

    public boolean getThrowExceptionOnExecuteError() {
        return this.throwExceptionOnExecuteError;
    }

    public HttpRequest setThrowExceptionOnExecuteError(boolean throwExceptionOnExecuteError) {
        this.throwExceptionOnExecuteError = throwExceptionOnExecuteError;
        return this;
    }

    public boolean getRetryOnExecuteIOException() {
        return this.retryOnExecuteIOException;
    }

    public HttpRequest setRetryOnExecuteIOException(boolean retryOnExecuteIOException) {
        this.retryOnExecuteIOException = retryOnExecuteIOException;
        return this;
    }

    public HttpResponse execute() throws IOException {
        boolean errorHandled;
        long backOffTime;
        int i;
        Preconditions.checkArgument(this.numRetries >= 0);
        int retriesRemaining = this.numRetries;
        if (this.backOffPolicy != null) {
            this.backOffPolicy.reset();
        }
        HttpResponse response = null;
        Preconditions.checkNotNull(this.method);
        Preconditions.checkNotNull(this.url);
        boolean retrySupported;
        do {
            LowLevelHttpRequest lowLevelHttpRequest;
            boolean loggable;
            StringBuilder logbuf;
            String originalUserAgent;
            HttpContent content;
            HttpContent content2;
            String contentEncoding;
            long contentLength;
            String contentType;
            LowLevelHttpResponse lowLevelHttpResponse;
            boolean redirectRequest;
            boolean backOffRetry;
            if (response != null) {
                response.ignore();
            }
            response = null;
            IOException executeException = null;
            if (this.interceptor != null) {
                this.interceptor.intercept(this);
            }
            String urlString = this.url.build();
            switch (this.method) {
                case DELETE:
                    lowLevelHttpRequest = this.transport.buildDeleteRequest(urlString);
                    break;
                case HEAD:
                    Preconditions.checkArgument(this.transport.supportsHead(), "HTTP transport doesn't support HEAD");
                    lowLevelHttpRequest = this.transport.buildHeadRequest(urlString);
                    break;
                case PATCH:
                    Preconditions.checkArgument(this.transport.supportsPatch(), "HTTP transport doesn't support PATCH");
                    lowLevelHttpRequest = this.transport.buildPatchRequest(urlString);
                    break;
                case POST:
                    lowLevelHttpRequest = this.transport.buildPostRequest(urlString);
                    break;
                case PUT:
                    lowLevelHttpRequest = this.transport.buildPutRequest(urlString);
                    break;
                default:
                    lowLevelHttpRequest = this.transport.buildGetRequest(urlString);
                    break;
            }
            Logger logger = HttpTransport.LOGGER;
            if (this.loggingEnabled) {
                if (logger.isLoggable(Level.CONFIG)) {
                    loggable = true;
                    logbuf = null;
                    if (loggable) {
                        logbuf = new StringBuilder();
                        logbuf.append("-------------- REQUEST  --------------").append(StringUtils.LINE_SEPARATOR);
                        logbuf.append(this.method).append(' ').append(urlString).append(StringUtils.LINE_SEPARATOR);
                    }
                    originalUserAgent = this.headers.getUserAgent();
                    if (originalUserAgent != null) {
                        this.headers.setUserAgent(USER_AGENT_SUFFIX);
                    } else {
                        this.headers.setUserAgent(originalUserAgent + " " + USER_AGENT_SUFFIX);
                    }
                    HttpHeaders.serializeHeaders(this.headers, logbuf, logger, lowLevelHttpRequest);
                    this.headers.setUserAgent(originalUserAgent);
                    content = this.content;
                    if (isAllowEmptyContent() && ((this.method == HttpMethod.PUT || this.method == HttpMethod.POST || this.method == HttpMethod.PATCH) && (content == null || content.getLength() == 0))) {
                        content2 = ByteArrayContent.fromString(null, " ");
                    } else {
                        content2 = content;
                    }
                    if (content2 == null) {
                        contentEncoding = content2.getEncoding();
                        contentLength = content2.getLength();
                        contentType = content2.getType();
                        if (loggable) {
                            content = content2;
                        } else {
                            content = new LogContent(content2, contentType, contentEncoding, contentLength, this.contentLoggingLimit);
                        }
                        if (this.enableGZipContent) {
                            content2 = new GZipContent(content, contentType);
                            contentEncoding = content2.getEncoding();
                            contentLength = content2.getLength();
                            content = content2;
                        }
                        if (loggable) {
                            if (contentType != null) {
                                logbuf.append("Content-Type: " + contentType).append(StringUtils.LINE_SEPARATOR);
                            }
                            if (contentEncoding != null) {
                                logbuf.append("Content-Encoding: " + contentEncoding).append(StringUtils.LINE_SEPARATOR);
                            }
                            if (contentLength >= 0) {
                                logbuf.append("Content-Length: " + contentLength).append(StringUtils.LINE_SEPARATOR);
                            }
                        }
                        lowLevelHttpRequest.setContent(content);
                    } else {
                        content = content2;
                    }
                    if (loggable) {
                        logger.config(logbuf.toString());
                    }
                    retrySupported = retriesRemaining <= 0 && (content == null || content.retrySupported());
                    lowLevelHttpRequest.setTimeout(this.connectTimeout, this.readTimeout);
                    lowLevelHttpResponse = lowLevelHttpRequest.execute();
                    HttpResponse httpResponse = new HttpResponse(this, lowLevelHttpResponse);
                    if (!true) {
                        try {
                            lowLevelHttpResponse.getContent().close();
                        } catch (IOException e) {
                            e = e;
                            response = httpResponse;
                            IOException e2;
                            if (this.retryOnExecuteIOException) {
                                executeException = e2;
                                logger.log(Level.WARNING, e2.getMessage(), e2);
                                if (response != null) {
                                    if (!response.isSuccessStatusCode()) {
                                        errorHandled = false;
                                        redirectRequest = false;
                                        backOffRetry = false;
                                        if (this.unsuccessfulResponseHandler != null) {
                                            errorHandled = this.unsuccessfulResponseHandler.handleResponse(this, response, retrySupported);
                                        }
                                        if (!errorHandled) {
                                            if (!getFollowRedirects()) {
                                            }
                                            if (retrySupported) {
                                                try {
                                                    backOffTime = this.backOffPolicy.getNextBackOffMillis();
                                                    if (backOffTime != -1) {
                                                        sleep(backOffTime);
                                                        backOffRetry = true;
                                                    }
                                                } catch (Throwable th) {
                                                    if (!(response == null || false)) {
                                                        response.disconnect();
                                                    }
                                                }
                                            }
                                        }
                                        if (!errorHandled) {
                                        }
                                        retrySupported &= i;
                                        if (retrySupported) {
                                            response.ignore();
                                        }
                                        retriesRemaining--;
                                        response.disconnect();
                                        continue;
                                        if (!retrySupported) {
                                            if (response == null) {
                                                if (this.throwExceptionOnExecuteError) {
                                                }
                                                return response;
                                            }
                                            throw executeException;
                                        }
                                    }
                                }
                                retrySupported &= response == null ? 1 : 0;
                                retriesRemaining--;
                                response.disconnect();
                                continue;
                                if (retrySupported) {
                                    if (response == null) {
                                        throw executeException;
                                    }
                                    if (this.throwExceptionOnExecuteError) {
                                    }
                                    return response;
                                }
                            }
                            throw e2;
                        }
                    }
                    response = httpResponse;
                    if (response != null) {
                        if (response.isSuccessStatusCode()) {
                            errorHandled = false;
                            redirectRequest = false;
                            backOffRetry = false;
                            if (this.unsuccessfulResponseHandler != null) {
                                errorHandled = this.unsuccessfulResponseHandler.handleResponse(this, response, retrySupported);
                            }
                            if (errorHandled) {
                                if (!getFollowRedirects() && isRedirected(response)) {
                                    handleRedirect(response);
                                    redirectRequest = true;
                                } else if (retrySupported) {
                                    if (this.backOffPolicy != null && this.backOffPolicy.isBackOffRequired(response.getStatusCode())) {
                                        backOffTime = this.backOffPolicy.getNextBackOffMillis();
                                        if (backOffTime != -1) {
                                            sleep(backOffTime);
                                            backOffRetry = true;
                                        }
                                    }
                                }
                            }
                            i = (errorHandled || redirectRequest || backOffRetry) ? 1 : 0;
                            retrySupported &= i;
                            if (retrySupported) {
                                response.ignore();
                            }
                            retriesRemaining--;
                            if (!(response == null || true)) {
                                response.disconnect();
                                continue;
                            }
                        }
                    }
                    if (response == null) {
                    }
                    retrySupported &= response == null ? 1 : 0;
                    retriesRemaining--;
                    response.disconnect();
                    continue;
                }
            }
            loggable = false;
            logbuf = null;
            if (loggable) {
                logbuf = new StringBuilder();
                logbuf.append("-------------- REQUEST  --------------").append(StringUtils.LINE_SEPARATOR);
                logbuf.append(this.method).append(' ').append(urlString).append(StringUtils.LINE_SEPARATOR);
            }
            originalUserAgent = this.headers.getUserAgent();
            if (originalUserAgent != null) {
                this.headers.setUserAgent(originalUserAgent + " " + USER_AGENT_SUFFIX);
            } else {
                this.headers.setUserAgent(USER_AGENT_SUFFIX);
            }
            HttpHeaders.serializeHeaders(this.headers, logbuf, logger, lowLevelHttpRequest);
            this.headers.setUserAgent(originalUserAgent);
            content = this.content;
            if (isAllowEmptyContent()) {
            }
            content2 = content;
            if (content2 == null) {
                content = content2;
            } else {
                contentEncoding = content2.getEncoding();
                contentLength = content2.getLength();
                contentType = content2.getType();
                if (loggable) {
                    content = content2;
                } else {
                    content = new LogContent(content2, contentType, contentEncoding, contentLength, this.contentLoggingLimit);
                }
                if (this.enableGZipContent) {
                    content2 = new GZipContent(content, contentType);
                    contentEncoding = content2.getEncoding();
                    contentLength = content2.getLength();
                    content = content2;
                }
                if (loggable) {
                    if (contentType != null) {
                        logbuf.append("Content-Type: " + contentType).append(StringUtils.LINE_SEPARATOR);
                    }
                    if (contentEncoding != null) {
                        logbuf.append("Content-Encoding: " + contentEncoding).append(StringUtils.LINE_SEPARATOR);
                    }
                    if (contentLength >= 0) {
                        logbuf.append("Content-Length: " + contentLength).append(StringUtils.LINE_SEPARATOR);
                    }
                }
                lowLevelHttpRequest.setContent(content);
            }
            if (loggable) {
                logger.config(logbuf.toString());
            }
            if (retriesRemaining <= 0) {
            }
            lowLevelHttpRequest.setTimeout(this.connectTimeout, this.readTimeout);
            try {
                lowLevelHttpResponse = lowLevelHttpRequest.execute();
                HttpResponse httpResponse2 = new HttpResponse(this, lowLevelHttpResponse);
                if (true) {
                    lowLevelHttpResponse.getContent().close();
                }
                response = httpResponse2;
            } catch (IOException e3) {
                e2 = e3;
            } catch (Throwable th2) {
                if (!false) {
                    lowLevelHttpResponse.getContent().close();
                }
            }
            if (response != null) {
                if (response.isSuccessStatusCode()) {
                    errorHandled = false;
                    redirectRequest = false;
                    backOffRetry = false;
                    if (this.unsuccessfulResponseHandler != null) {
                        errorHandled = this.unsuccessfulResponseHandler.handleResponse(this, response, retrySupported);
                    }
                    if (errorHandled) {
                        if (!getFollowRedirects()) {
                        }
                        if (retrySupported) {
                            backOffTime = this.backOffPolicy.getNextBackOffMillis();
                            if (backOffTime != -1) {
                                sleep(backOffTime);
                                backOffRetry = true;
                            }
                        }
                    }
                    if (errorHandled) {
                    }
                    retrySupported &= i;
                    if (retrySupported) {
                        response.ignore();
                    }
                    retriesRemaining--;
                    response.disconnect();
                    continue;
                }
            }
            if (response == null) {
            }
            retrySupported &= response == null ? 1 : 0;
            retriesRemaining--;
            response.disconnect();
            continue;
        } while (retrySupported);
        if (response == null) {
            throw executeException;
        } else if (this.throwExceptionOnExecuteError || response.isSuccessStatusCode()) {
            return response;
        } else {
            try {
                throw new HttpResponseException(response);
            } catch (Throwable th3) {
                response.disconnect();
            }
        }
    }

    private void handleRedirect(HttpResponse response) {
        setUrl(new GenericUrl(response.getHeaders().getLocation()));
        if (response.getStatusCode() == 303) {
            setMethod(HttpMethod.GET);
        }
    }

    private boolean isRedirected(HttpResponse response) {
        switch (response.getStatusCode()) {
            case 301:
            case 302:
            case 303:
            case 307:
                if (response.getHeaders().getLocation() != null) {
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }

    @Deprecated
    public static String normalizeMediaType(String mediaType) {
        if (mediaType == null) {
            return null;
        }
        int semicolon = mediaType.indexOf(59);
        return semicolon != -1 ? mediaType.substring(0, semicolon) : mediaType;
    }
}
