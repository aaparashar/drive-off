package com.google.api.client.googleapis;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpMethod;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.common.base.Preconditions;
import com.google.common.io.LimitInputStream;
import java.io.IOException;
import java.io.InputStream;

@Deprecated
public final class MediaHttpUploader {
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    private static final int KB = 1024;
    private static final int MB = 1048576;
    public static final int MINIMUM_CHUNK_SIZE = 262144;
    private boolean backOffPolicyEnabled = true;
    private long bytesUploaded;
    private int chunkSize = 10485760;
    private InputStream contentInputStream;
    private HttpRequest currentRequest;
    private GoogleHeaders initiationHeaders = new GoogleHeaders();
    private HttpMethod initiationMethod = HttpMethod.POST;
    private final AbstractInputStreamContent mediaContent;
    private long mediaContentLength;
    private HttpContent metadata;
    private MediaHttpUploaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;
    private UploadState uploadState = UploadState.NOT_STARTED;

    public enum UploadState {
        NOT_STARTED,
        INITIATION_STARTED,
        INITIATION_COMPLETE,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpUploader(AbstractInputStreamContent mediaContent, HttpTransport transport, HttpRequestInitializer httpRequestInitializer) {
        this.mediaContent = (AbstractInputStreamContent) Preconditions.checkNotNull(mediaContent);
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.requestFactory = httpRequestInitializer == null ? transport.createRequestFactory() : transport.createRequestFactory(httpRequestInitializer);
    }

    public HttpResponse upload(GenericUrl initiationRequestUrl) throws IOException {
        boolean z;
        HttpResponse response;
        if (this.currentRequest == null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkState(z, "upload can not be called twice for one instance.");
        GenericUrl uploadUrl = new GenericUrl(executeUploadInitiation(initiationRequestUrl).getHeaders().getLocation());
        this.contentInputStream = this.mediaContent.getInputStream();
        while (true) {
            this.currentRequest = this.requestFactory.buildPutRequest(uploadUrl, null);
            new MethodOverride().intercept(this.currentRequest);
            this.currentRequest.setAllowEmptyContent(false);
            setContentAndHeadersOnCurrentRequest(this.bytesUploaded);
            if (this.backOffPolicyEnabled) {
                this.currentRequest.setBackOffPolicy(new MediaExponentialBackOffPolicy(this));
            }
            this.currentRequest.setThrowExceptionOnExecuteError(false);
            response = this.currentRequest.execute();
            if (!response.isSuccessStatusCode()) {
                if (response.getStatusCode() != ErrorMessages.ERROR_TWITTER_REQUEST_FOLLOWERS_FAILED) {
                    break;
                }
                String updatedUploadUrl = response.getHeaders().getLocation();
                if (updatedUploadUrl != null) {
                    uploadUrl = new GenericUrl(updatedUploadUrl);
                }
                this.bytesUploaded = getNextByteIndex(response.getHeaders().getRange());
                updateStateAndNotifyListener(UploadState.MEDIA_IN_PROGRESS);
            } else {
                break;
            }
        }
        this.bytesUploaded = this.mediaContentLength;
        this.contentInputStream.close();
        updateStateAndNotifyListener(UploadState.MEDIA_COMPLETE);
        return response;
    }

    private long getMediaContentLength() throws IOException {
        if (this.mediaContentLength == 0) {
            this.mediaContentLength = this.mediaContent.getLength();
            Preconditions.checkArgument(this.mediaContentLength != -1);
        }
        return this.mediaContentLength;
    }

    private HttpResponse executeUploadInitiation(GenericUrl initiationRequestUrl) throws IOException {
        updateStateAndNotifyListener(UploadState.INITIATION_STARTED);
        initiationRequestUrl.put("uploadType", (Object) "resumable");
        HttpRequest request = this.requestFactory.buildRequest(this.initiationMethod, initiationRequestUrl, this.metadata);
        if (this.initiationMethod == HttpMethod.PUT) {
            new MethodOverride().intercept(request);
        }
        this.initiationHeaders.setUploadContentType(this.mediaContent.getType());
        this.initiationHeaders.setUploadContentLength(getMediaContentLength());
        request.setHeaders(this.initiationHeaders);
        request.setAllowEmptyContent(false);
        HttpResponse response = request.execute();
        updateStateAndNotifyListener(UploadState.INITIATION_COMPLETE);
        return response;
    }

    private void setContentAndHeadersOnCurrentRequest(long bytesWritten) throws IOException {
        int blockSize = (int) Math.min((long) this.chunkSize, getMediaContentLength() - bytesWritten);
        InputStreamContent contentChunk = new InputStreamContent(this.mediaContent.getType(), new LimitInputStream(this.contentInputStream, (long) blockSize));
        contentChunk.setCloseInputStream(false);
        contentChunk.setRetrySupported(true);
        contentChunk.setLength((long) blockSize);
        this.contentInputStream.mark(blockSize);
        this.currentRequest.setContent(contentChunk);
        this.currentRequest.getHeaders().setContentRange("bytes " + bytesWritten + "-" + ((((long) blockSize) + bytesWritten) - 1) + "/" + getMediaContentLength());
    }

    public void serverErrorCallback() throws IOException {
        Preconditions.checkNotNull(this.currentRequest, "The current request should not be null");
        HttpRequest request = this.requestFactory.buildPutRequest(this.currentRequest.getUrl(), null);
        new MethodOverride().intercept(request);
        request.setAllowEmptyContent(true);
        request.setContent(new ByteArrayContent(null, new byte[0]));
        request.getHeaders().setContentRange("bytes */" + getMediaContentLength());
        request.setThrowExceptionOnExecuteError(false);
        HttpResponse response = request.execute();
        long bytesWritten = getNextByteIndex(response.getHeaders().getRange());
        String updatedUploadUrl = response.getHeaders().getLocation();
        if (updatedUploadUrl != null) {
            this.currentRequest.setUrl(new GenericUrl(updatedUploadUrl));
        }
        this.contentInputStream.reset();
        long skipValue = this.bytesUploaded - bytesWritten;
        Preconditions.checkState(skipValue == this.contentInputStream.skip(skipValue));
        setContentAndHeadersOnCurrentRequest(bytesWritten);
    }

    private long getNextByteIndex(String rangeHeader) {
        if (rangeHeader == null) {
            return 0;
        }
        return Long.parseLong(rangeHeader.substring(rangeHeader.indexOf(45) + 1)) + 1;
    }

    public HttpContent getMetadata() {
        return this.metadata;
    }

    public MediaHttpUploader setMetadata(HttpContent metadata) {
        this.metadata = metadata;
        return this;
    }

    public HttpContent getMediaContent() {
        return this.mediaContent;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public MediaHttpUploader setBackOffPolicyEnabled(boolean backOffPolicyEnabled) {
        this.backOffPolicyEnabled = backOffPolicyEnabled;
        return this;
    }

    public boolean isBackOffPolicyEnabled() {
        return this.backOffPolicyEnabled;
    }

    public MediaHttpUploader setProgressListener(MediaHttpUploaderProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public MediaHttpUploaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public MediaHttpUploader setChunkSize(int chunkSize) {
        Preconditions.checkArgument(chunkSize >= 262144);
        this.chunkSize = chunkSize;
        return this;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public MediaHttpUploader setInitiationMethod(HttpMethod initiationMethod) {
        boolean z = initiationMethod == HttpMethod.POST || initiationMethod == HttpMethod.PUT;
        Preconditions.checkArgument(z);
        this.initiationMethod = initiationMethod;
        return this;
    }

    public HttpMethod getInitiationMethod() {
        return this.initiationMethod;
    }

    public MediaHttpUploader setInitiationHeaders(GoogleHeaders initiationHeaders) {
        this.initiationHeaders = initiationHeaders;
        return this;
    }

    public GoogleHeaders getInitiationHeaders() {
        return this.initiationHeaders;
    }

    public long getNumBytesUploaded() {
        return this.bytesUploaded;
    }

    private void updateStateAndNotifyListener(UploadState uploadState) throws IOException {
        this.uploadState = uploadState;
        if (this.progressListener != null) {
            this.progressListener.progressChanged(this);
        }
    }

    public UploadState getUploadState() {
        return this.uploadState;
    }

    public double getProgress() throws IOException {
        return ((double) this.bytesUploaded) / ((double) getMediaContentLength());
    }
}
