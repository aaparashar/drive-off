package com.google.api.client.googleapis.batch;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpStatusCodes;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.HttpUnsuccessfulResponseHandler;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.HttpTesting;
import com.google.api.client.util.ObjectParser;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

final class BatchUnparsedResponse {
    boolean backOffRequired;
    private final String boundary;
    private final BufferedReader bufferedReader;
    private int contentId = 0;
    boolean hasNext = true;
    private final List<RequestInfo<?, ?>> requestInfos;
    private final boolean retryAllowed;
    List<RequestInfo<?, ?>> unsuccessfulRequestInfos = new ArrayList();

    @Deprecated
    private static class FakeLowLevelHttpRequest extends LowLevelHttpRequest {
        private List<String> headerNames;
        private List<String> headerValues;
        private String partContent;
        private int statusCode;

        FakeLowLevelHttpRequest(String partContent, int statusCode, List<String> headerNames, List<String> headerValues) {
            this.partContent = partContent;
            this.statusCode = statusCode;
            this.headerNames = headerNames;
            this.headerValues = headerValues;
        }

        public void addHeader(String name, String value) {
        }

        public void setContent(HttpContent content) {
        }

        public LowLevelHttpResponse execute() {
            return new FakeLowLevelHttpResponse(new ByteArrayInputStream(StringUtils.getBytesUtf8(this.partContent)), this.statusCode, this.headerNames, this.headerValues);
        }
    }

    @Deprecated
    private static class FakeLowLevelHttpResponse extends LowLevelHttpResponse {
        private List<String> headerNames = new ArrayList();
        private List<String> headerValues = new ArrayList();
        private InputStream partContent;
        private int statusCode;

        FakeLowLevelHttpResponse(InputStream partContent, int statusCode, List<String> headerNames, List<String> headerValues) {
            this.partContent = partContent;
            this.statusCode = statusCode;
            this.headerNames = headerNames;
            this.headerValues = headerValues;
        }

        public InputStream getContent() {
            return this.partContent;
        }

        public int getStatusCode() {
            return this.statusCode;
        }

        public String getContentEncoding() {
            return null;
        }

        public long getContentLength() {
            return 0;
        }

        public String getContentType() {
            return null;
        }

        public String getStatusLine() {
            return null;
        }

        public String getReasonPhrase() {
            return null;
        }

        public int getHeaderCount() {
            return this.headerNames.size();
        }

        public String getHeaderName(int index) {
            return (String) this.headerNames.get(index);
        }

        public String getHeaderValue(int index) {
            return (String) this.headerValues.get(index);
        }
    }

    @Deprecated
    private static class FakeResponseHttpTransport extends HttpTransport {
        private List<String> headerNames;
        private List<String> headerValues;
        private String partContent;
        private int statusCode;

        FakeResponseHttpTransport(int statusCode, String partContent, List<String> headerNames, List<String> headerValues) {
            this.statusCode = statusCode;
            this.partContent = partContent;
            this.headerNames = headerNames;
            this.headerValues = headerValues;
        }

        protected LowLevelHttpRequest buildDeleteRequest(String url) {
            return null;
        }

        protected LowLevelHttpRequest buildGetRequest(String url) {
            return null;
        }

        protected LowLevelHttpRequest buildPostRequest(String url) {
            return new FakeLowLevelHttpRequest(this.partContent, this.statusCode, this.headerNames, this.headerValues);
        }

        protected LowLevelHttpRequest buildPutRequest(String url) {
            return null;
        }
    }

    BatchUnparsedResponse(InputStream inputStream, String boundary, List<RequestInfo<?, ?>> requestInfos, boolean retryAllowed) throws IOException {
        this.boundary = boundary;
        this.requestInfos = requestInfos;
        this.retryAllowed = retryAllowed;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        checkForFinalBoundary(this.bufferedReader.readLine());
    }

    void parseNextResponse() throws IOException {
        String line;
        boolean z;
        StringBuilder partContent;
        this.contentId++;
        do {
            line = this.bufferedReader.readLine();
            if (line == null) {
                break;
            }
        } while (!line.equals(""));
        String statusLine = this.bufferedReader.readLine();
        if (statusLine != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkState(z);
        int statusCode = Integer.parseInt(statusLine.split(" ")[1]);
        List<String> headerNames = new ArrayList();
        List<String> headerValues = new ArrayList();
        while (true) {
            line = this.bufferedReader.readLine();
            if (line == null || line.equals("")) {
                partContent = new StringBuilder();
            } else {
                String[] headerParts = line.split(": ", 2);
                headerNames.add(headerParts[0]);
                headerValues.add(headerParts[1]);
            }
        }
        partContent = new StringBuilder();
        while (true) {
            line = this.bufferedReader.readLine();
            if (line == null || line.startsWith(this.boundary)) {
                parseAndCallback((RequestInfo) this.requestInfos.get(this.contentId - 1), statusCode, this.contentId, getFakeResponse(statusCode, partContent.toString(), headerNames, headerValues));
                checkForFinalBoundary(line);
            } else {
                partContent.append(line);
            }
        }
        parseAndCallback((RequestInfo) this.requestInfos.get(this.contentId - 1), statusCode, this.contentId, getFakeResponse(statusCode, partContent.toString(), headerNames, headerValues));
        checkForFinalBoundary(line);
    }

    private <T, E> void parseAndCallback(RequestInfo<T, E> requestInfo, int statusCode, int contentID, HttpResponse response) throws IOException {
        boolean retrySupported = false;
        BatchCallback<T, E> callback = requestInfo.callback;
        GoogleHeaders responseHeaders = new GoogleHeaders();
        responseHeaders.putAll(response.getHeaders());
        HttpUnsuccessfulResponseHandler unsuccessfulResponseHandler = requestInfo.request.getUnsuccessfulResponseHandler();
        BackOffPolicy backOffPolicy = requestInfo.request.getBackOffPolicy();
        this.backOffRequired = false;
        if (!HttpStatusCodes.isSuccess(statusCode)) {
            HttpContent content = requestInfo.request.getContent();
            if (this.retryAllowed && (content == null || content.retrySupported())) {
                retrySupported = true;
            }
            boolean errorHandled = false;
            if (unsuccessfulResponseHandler != null) {
                errorHandled = unsuccessfulResponseHandler.handleResponse(requestInfo.request, response, retrySupported);
            }
            if (!errorHandled && retrySupported && backOffPolicy != null && backOffPolicy.isBackOffRequired(response.getStatusCode())) {
                this.backOffRequired = true;
            }
            if (retrySupported && (errorHandled || this.backOffRequired)) {
                this.unsuccessfulRequestInfos.add(requestInfo);
            } else if (callback != null) {
                callback.onFailure(getParsedDataClass(requestInfo.errorClass, response, requestInfo, responseHeaders.getContentType()), responseHeaders);
            }
        } else if (callback != null) {
            callback.onSuccess(getParsedDataClass(requestInfo.dataClass, response, requestInfo, responseHeaders.getContentType()), responseHeaders);
        }
    }

    private <A, T, E> A getParsedDataClass(Class<A> dataClass, HttpResponse response, RequestInfo<T, E> requestInfo, String contentType) throws IOException {
        HttpParser oldParser = requestInfo.request.getParser(contentType);
        ObjectParser parser = requestInfo.request.getParser();
        if (dataClass != Void.class) {
            return parser != null ? parser.parseAndClose(response.getContent(), response.getContentCharset(), (Class) dataClass) : oldParser.parse(response, dataClass);
        } else {
            return null;
        }
    }

    @Deprecated
    private HttpResponse getFakeResponse(int statusCode, String partContent, List<String> headerNames, List<String> headerValues) throws IOException {
        HttpRequest request = new FakeResponseHttpTransport(statusCode, partContent, headerNames, headerValues).createRequestFactory().buildPostRequest(new GenericUrl(HttpTesting.SIMPLE_URL), null);
        request.setLoggingEnabled(false);
        request.setThrowExceptionOnExecuteError(false);
        return request.execute();
    }

    private void checkForFinalBoundary(String boundaryLine) throws IOException {
        if (boundaryLine.equals(this.boundary + "--")) {
            this.hasNext = false;
            this.bufferedReader.close();
        }
    }
}
