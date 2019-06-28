package com.google.api.client.googleapis.batch;

import com.google.api.client.http.BackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpExecuteInterceptor;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class BatchRequest {
    private GenericUrl batchUrl = new GenericUrl("https://www.googleapis.com/batch");
    private final HttpRequestFactory requestFactory;
    List<RequestInfo<?, ?>> requestInfos = new ArrayList();

    static class RequestInfo<T, E> {
        final BatchCallback<T, E> callback;
        final Class<T> dataClass;
        final Class<E> errorClass;
        final HttpRequest request;

        RequestInfo(BatchCallback<T, E> callback, Class<T> dataClass, Class<E> errorClass, HttpRequest request) {
            this.callback = callback;
            this.dataClass = dataClass;
            this.errorClass = errorClass;
            this.request = request;
        }
    }

    class BatchInterceptor implements HttpExecuteInterceptor {
        BatchInterceptor() {
        }

        public void intercept(HttpRequest batchRequest) throws IOException {
            for (RequestInfo<?, ?> requestInfo : BatchRequest.this.requestInfos) {
                HttpExecuteInterceptor interceptor = requestInfo.request.getInterceptor();
                if (interceptor != null) {
                    interceptor.intercept(requestInfo.request);
                }
            }
        }
    }

    public BatchRequest(HttpTransport transport, HttpRequestInitializer httpRequestInitializer) {
        this.requestFactory = httpRequestInitializer == null ? transport.createRequestFactory() : transport.createRequestFactory(httpRequestInitializer);
    }

    public BatchRequest setBatchUrl(GenericUrl batchUrl) {
        this.batchUrl = batchUrl;
        return this;
    }

    public GenericUrl getBatchUrl() {
        return this.batchUrl;
    }

    public <T, E> BatchRequest queue(HttpRequest httpRequest, Class<T> dataClass, Class<E> errorClass, BatchCallback<T, E> callback) throws IOException {
        Preconditions.checkNotNull(httpRequest);
        Preconditions.checkNotNull(callback);
        Preconditions.checkNotNull(dataClass);
        Preconditions.checkNotNull(errorClass);
        this.requestInfos.add(new RequestInfo(callback, dataClass, errorClass, httpRequest));
        return this;
    }

    public int size() {
        return this.requestInfos.size();
    }

    public void execute() throws IOException {
        Preconditions.checkState(!this.requestInfos.isEmpty());
        HttpRequest batchRequest = this.requestFactory.buildPostRequest(this.batchUrl, null);
        batchRequest.setInterceptor(new BatchInterceptor());
        int retriesRemaining = batchRequest.getNumberOfRetries();
        BackOffPolicy backOffPolicy = batchRequest.getBackOffPolicy();
        if (backOffPolicy != null) {
            backOffPolicy.reset();
        }
        loop3:
        while (true) {
            boolean retryAllowed = retriesRemaining > 0;
            batchRequest.setContent(new MultipartMixedContent(this.requestInfos, "__END_OF_PART__"));
            HttpResponse response = batchRequest.execute();
            try {
                String boundary = null;
                for (String part : response.getHeaders().getContentType().split(";")) {
                    if (part.contains("boundary=")) {
                        boundary = "--" + part.substring("boundary=".length() + part.indexOf("boundary="));
                        break;
                    }
                }
                BatchUnparsedResponse batchResponse = new BatchUnparsedResponse(response.getContent(), boundary, this.requestInfos, retryAllowed);
                while (batchResponse.hasNext) {
                    batchResponse.parseNextResponse();
                }
                List<RequestInfo<?, ?>> unsuccessfulRequestInfos = batchResponse.unsuccessfulRequestInfos;
                if (!unsuccessfulRequestInfos.isEmpty()) {
                    this.requestInfos = unsuccessfulRequestInfos;
                    if (batchResponse.backOffRequired && backOffPolicy != null) {
                        long backOffTime = backOffPolicy.getNextBackOffMillis();
                        if (backOffTime != -1) {
                            sleep(backOffTime);
                        }
                    }
                    retriesRemaining--;
                    if (!retryAllowed) {
                        break loop3;
                    }
                }
                break loop3;
            } finally {
                response.disconnect();
            }
        }
        this.requestInfos.clear();
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
    }
}
