package com.google.api.client.googleapis.media;

import com.google.api.client.googleapis.GoogleHeaders;
import com.google.api.client.googleapis.MethodOverride;
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
import com.google.common.base.Preconditions;
import com.google.common.io.LimitInputStream;
import java.io.IOException;
import java.io.InputStream;

public final class MediaHttpUploader {
    public static final int DEFAULT_CHUNK_SIZE = 10485760;
    private static final int KB = 1024;
    static final int MB = 1048576;
    public static final int MINIMUM_CHUNK_SIZE = 262144;
    private boolean backOffPolicyEnabled = true;
    private long bytesUploaded;
    private int chunkSize = 10485760;
    private InputStream contentInputStream;
    private HttpRequest currentRequest;
    private boolean directUploadEnabled;
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

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public com.google.api.client.http.HttpResponse upload(com.google.api.client.http.GenericUrl r15) throws java.io.IOException {
        /*
        r14 = this;
        r10 = r14.uploadState;
        r11 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.NOT_STARTED;
        if (r10 != r11) goto L_0x0056;
    L_0x0006:
        r10 = 1;
    L_0x0007:
        com.google.common.base.Preconditions.checkArgument(r10);
        r10 = r14.directUploadEnabled;
        if (r10 == 0) goto L_0x0067;
    L_0x000e:
        r10 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS;
        r14.updateStateAndNotifyListener(r10);
        r0 = r14.mediaContent;
        r10 = r14.metadata;
        if (r10 == 0) goto L_0x0058;
    L_0x0019:
        r0 = new com.google.api.client.http.MultipartRelatedContent;
        r10 = r14.metadata;
        r11 = 1;
        r11 = new com.google.api.client.http.HttpContent[r11];
        r12 = 0;
        r13 = r14.mediaContent;
        r11[r12] = r13;
        r0.<init>(r10, r11);
        r10 = "uploadType";
        r11 = "multipart";
        r15.put(r10, r11);
    L_0x002f:
        r10 = r14.requestFactory;
        r11 = r14.initiationMethod;
        r2 = r10.buildRequest(r11, r15, r0);
        r10 = 1;
        r2.setEnableGZipContent(r10);
        r14.addMethodOverride(r2);
        r3 = r2.execute();
        r5 = 0;
        r10 = r14.getMediaContentLength();	 Catch:{ all -> 0x0060 }
        r14.bytesUploaded = r10;	 Catch:{ all -> 0x0060 }
        r10 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_COMPLETE;	 Catch:{ all -> 0x0060 }
        r14.updateStateAndNotifyListener(r10);	 Catch:{ all -> 0x0060 }
        r5 = 1;
        if (r5 != 0) goto L_0x0054;
    L_0x0051:
        r3.disconnect();
    L_0x0054:
        r4 = r3;
    L_0x0055:
        return r4;
    L_0x0056:
        r10 = 0;
        goto L_0x0007;
    L_0x0058:
        r10 = "uploadType";
        r11 = "media";
        r15.put(r10, r11);
        goto L_0x002f;
    L_0x0060:
        r10 = move-exception;
        if (r5 != 0) goto L_0x0066;
    L_0x0063:
        r3.disconnect();
    L_0x0066:
        throw r10;
    L_0x0067:
        r1 = r14.executeUploadInitiation(r15);
        r8 = new com.google.api.client.http.GenericUrl;	 Catch:{ all -> 0x00f0 }
        r10 = r1.getHeaders();	 Catch:{ all -> 0x00f0 }
        r10 = r10.getLocation();	 Catch:{ all -> 0x00f0 }
        r8.<init>(r10);	 Catch:{ all -> 0x00f0 }
        r1.disconnect();
        r10 = r14.mediaContent;
        r10 = r10.getInputStream();
        r14.contentInputStream = r10;
        r10 = r14.contentInputStream;
        r10 = r10.markSupported();
        if (r10 != 0) goto L_0x0094;
    L_0x008b:
        r10 = new java.io.BufferedInputStream;
        r11 = r14.contentInputStream;
        r10.<init>(r11);
        r14.contentInputStream = r10;
    L_0x0094:
        r10 = r14.requestFactory;
        r11 = 0;
        r10 = r10.buildPutRequest(r8, r11);
        r14.currentRequest = r10;
        r10 = new com.google.api.client.googleapis.MethodOverride;
        r10.<init>();
        r11 = r14.currentRequest;
        r10.intercept(r11);
        r10 = r14.currentRequest;
        r11 = 0;
        r10.setAllowEmptyContent(r11);
        r10 = r14.bytesUploaded;
        r14.setContentAndHeadersOnCurrentRequest(r10);
        r10 = r14.backOffPolicyEnabled;
        if (r10 == 0) goto L_0x00c0;
    L_0x00b6:
        r10 = r14.currentRequest;
        r11 = new com.google.api.client.googleapis.media.MediaUploadExponentialBackOffPolicy;
        r11.<init>(r14);
        r10.setBackOffPolicy(r11);
    L_0x00c0:
        r10 = r14.currentRequest;
        r11 = 0;
        r10.setThrowExceptionOnExecuteError(r11);
        r10 = r14.currentRequest;
        r11 = 1;
        r10.setRetryOnExecuteIOException(r11);
        r10 = r14.currentRequest;
        r3 = r10.execute();
        r6 = 0;
        r10 = r3.isSuccessStatusCode();	 Catch:{ all -> 0x0130 }
        if (r10 == 0) goto L_0x00f5;
    L_0x00d9:
        r10 = r14.mediaContentLength;	 Catch:{ all -> 0x0130 }
        r14.bytesUploaded = r10;	 Catch:{ all -> 0x0130 }
        r10 = r14.contentInputStream;	 Catch:{ all -> 0x0130 }
        r10.close();	 Catch:{ all -> 0x0130 }
        r10 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_COMPLETE;	 Catch:{ all -> 0x0130 }
        r14.updateStateAndNotifyListener(r10);	 Catch:{ all -> 0x0130 }
        r6 = 1;
        if (r6 != 0) goto L_0x00ed;
    L_0x00ea:
        r3.disconnect();
    L_0x00ed:
        r4 = r3;
        goto L_0x0055;
    L_0x00f0:
        r10 = move-exception;
        r1.disconnect();
        throw r10;
    L_0x00f5:
        r10 = r3.getStatusCode();	 Catch:{ all -> 0x0130 }
        r11 = 308; // 0x134 float:4.32E-43 double:1.52E-321;
        if (r10 == r11) goto L_0x0106;
    L_0x00fd:
        r6 = 1;
        if (r6 != 0) goto L_0x0103;
    L_0x0100:
        r3.disconnect();
    L_0x0103:
        r4 = r3;
        goto L_0x0055;
    L_0x0106:
        r10 = r3.getHeaders();	 Catch:{ all -> 0x0130 }
        r7 = r10.getLocation();	 Catch:{ all -> 0x0130 }
        if (r7 == 0) goto L_0x0116;
    L_0x0110:
        r9 = new com.google.api.client.http.GenericUrl;	 Catch:{ all -> 0x0130 }
        r9.<init>(r7);	 Catch:{ all -> 0x0130 }
        r8 = r9;
    L_0x0116:
        r10 = r3.getHeaders();	 Catch:{ all -> 0x0130 }
        r10 = r10.getRange();	 Catch:{ all -> 0x0130 }
        r10 = r14.getNextByteIndex(r10);	 Catch:{ all -> 0x0130 }
        r14.bytesUploaded = r10;	 Catch:{ all -> 0x0130 }
        r10 = com.google.api.client.googleapis.media.MediaHttpUploader.UploadState.MEDIA_IN_PROGRESS;	 Catch:{ all -> 0x0130 }
        r14.updateStateAndNotifyListener(r10);	 Catch:{ all -> 0x0130 }
        if (r6 != 0) goto L_0x0094;
    L_0x012b:
        r3.disconnect();
        goto L_0x0094;
    L_0x0130:
        r10 = move-exception;
        if (r6 != 0) goto L_0x0136;
    L_0x0133:
        r3.disconnect();
    L_0x0136:
        throw r10;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.api.client.googleapis.media.MediaHttpUploader.upload(com.google.api.client.http.GenericUrl):com.google.api.client.http.HttpResponse");
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
        addMethodOverride(request);
        this.initiationHeaders.setUploadContentType(this.mediaContent.getType());
        this.initiationHeaders.setUploadContentLength(getMediaContentLength());
        request.setHeaders(this.initiationHeaders);
        request.setAllowEmptyContent(false);
        request.setRetryOnExecuteIOException(true);
        request.setEnableGZipContent(true);
        HttpResponse response = request.execute();
        boolean notificationCompleted = false;
        try {
            updateStateAndNotifyListener(UploadState.INITIATION_COMPLETE);
            notificationCompleted = true;
            return response;
        } finally {
            if (!notificationCompleted) {
                response.disconnect();
            }
        }
    }

    private void addMethodOverride(HttpRequest request) {
        new MethodOverride().intercept(request);
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
        request.setRetryOnExecuteIOException(true);
        HttpResponse response = request.execute();
        try {
            long bytesWritten = getNextByteIndex(response.getHeaders().getRange());
            String updatedUploadUrl = response.getHeaders().getLocation();
            if (updatedUploadUrl != null) {
                this.currentRequest.setUrl(new GenericUrl(updatedUploadUrl));
            }
            this.contentInputStream.reset();
            long skipValue = this.bytesUploaded - bytesWritten;
            Preconditions.checkState(skipValue == this.contentInputStream.skip(skipValue));
            setContentAndHeadersOnCurrentRequest(bytesWritten);
        } finally {
            response.disconnect();
        }
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

    public MediaHttpUploader setDirectUploadEnabled(boolean directUploadEnabled) {
        this.directUploadEnabled = directUploadEnabled;
        return this;
    }

    public boolean isDirectUploadEnabled() {
        return this.directUploadEnabled;
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
