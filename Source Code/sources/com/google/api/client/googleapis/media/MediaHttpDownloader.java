package com.google.api.client.googleapis.media;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.client.http.ExponentialBackOffPolicy;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;

public final class MediaHttpDownloader {
    public static final int MAXIMUM_CHUNK_SIZE = 33554432;
    private boolean backOffPolicyEnabled = true;
    private long bytesDownloaded;
    private int chunkSize = 33554432;
    private boolean directDownloadEnabled = false;
    private DownloadState downloadState = DownloadState.NOT_STARTED;
    private long mediaContentLength;
    private MediaHttpDownloaderProgressListener progressListener;
    private final HttpRequestFactory requestFactory;
    private final HttpTransport transport;

    public enum DownloadState {
        NOT_STARTED,
        MEDIA_IN_PROGRESS,
        MEDIA_COMPLETE
    }

    public MediaHttpDownloader(HttpTransport transport, HttpRequestInitializer httpRequestInitializer) {
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.requestFactory = httpRequestInitializer == null ? transport.createRequestFactory() : transport.createRequestFactory(httpRequestInitializer);
    }

    public void download(GenericUrl requestUrl, OutputStream outputStream) throws IOException {
        Preconditions.checkArgument(this.downloadState == DownloadState.NOT_STARTED);
        requestUrl.put("alt", (Object) "media");
        HttpResponse response;
        if (this.directDownloadEnabled) {
            updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
            response = this.requestFactory.buildGetRequest(requestUrl).execute();
            try {
                this.mediaContentLength = response.getHeaders().getContentLength().longValue();
                this.bytesDownloaded = this.mediaContentLength;
                updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
                AbstractInputStreamContent.copy(response.getContent(), outputStream);
            } finally {
                response.disconnect();
            }
        } else {
            while (true) {
                HttpRequest request = this.requestFactory.buildGetRequest(requestUrl);
                request.getHeaders().setRange("bytes=" + this.bytesDownloaded + "-" + ((this.bytesDownloaded + ((long) this.chunkSize)) - 1));
                if (this.backOffPolicyEnabled) {
                    request.setBackOffPolicy(new ExponentialBackOffPolicy());
                }
                response = request.execute();
                AbstractInputStreamContent.copy(response.getContent(), outputStream);
                String contentRange = response.getHeaders().getContentRange();
                long nextByteIndex = getNextByteIndex(contentRange);
                setMediaContentLength(contentRange);
                if (this.mediaContentLength <= nextByteIndex) {
                    this.bytesDownloaded = this.mediaContentLength;
                    updateStateAndNotifyListener(DownloadState.MEDIA_COMPLETE);
                    return;
                }
                this.bytesDownloaded = nextByteIndex;
                updateStateAndNotifyListener(DownloadState.MEDIA_IN_PROGRESS);
            }
        }
    }

    private long getNextByteIndex(String rangeHeader) {
        if (rangeHeader == null) {
            return 0;
        }
        return Long.parseLong(rangeHeader.substring(rangeHeader.indexOf(45) + 1, rangeHeader.indexOf(47))) + 1;
    }

    public MediaHttpDownloader setBytesDownloaded(long bytesDownloaded) {
        this.bytesDownloaded = bytesDownloaded;
        return this;
    }

    private void setMediaContentLength(String rangeHeader) {
        if (rangeHeader != null && this.mediaContentLength == 0) {
            this.mediaContentLength = Long.parseLong(rangeHeader.substring(rangeHeader.indexOf(47) + 1));
        }
    }

    public boolean isDirectDownloadEnabled() {
        return this.directDownloadEnabled;
    }

    public MediaHttpDownloader setDirectDownloadEnabled(boolean directDownloadEnabled) {
        this.directDownloadEnabled = directDownloadEnabled;
        return this;
    }

    public MediaHttpDownloader setProgressListener(MediaHttpDownloaderProgressListener progressListener) {
        this.progressListener = progressListener;
        return this;
    }

    public MediaHttpDownloaderProgressListener getProgressListener() {
        return this.progressListener;
    }

    public MediaHttpDownloader setBackOffPolicyEnabled(boolean backOffPolicyEnabled) {
        this.backOffPolicyEnabled = backOffPolicyEnabled;
        return this;
    }

    public boolean isBackOffPolicyEnabled() {
        return this.backOffPolicyEnabled;
    }

    public HttpTransport getTransport() {
        return this.transport;
    }

    public MediaHttpDownloader setChunkSize(int chunkSize) {
        boolean z = chunkSize > 0 && chunkSize <= 33554432;
        Preconditions.checkArgument(z);
        this.chunkSize = chunkSize;
        return this;
    }

    public int getChunkSize() {
        return this.chunkSize;
    }

    public long getNumBytesDownloaded() {
        return this.bytesDownloaded;
    }

    private void updateStateAndNotifyListener(DownloadState downloadState) throws IOException {
        this.downloadState = downloadState;
        if (this.progressListener != null) {
            this.progressListener.progressChanged(this);
        }
    }

    public DownloadState getDownloadState() {
        return this.downloadState;
    }

    public double getProgress() {
        return this.mediaContentLength == 0 ? 0.0d : ((double) this.bytesDownloaded) / ((double) this.mediaContentLength);
    }
}
