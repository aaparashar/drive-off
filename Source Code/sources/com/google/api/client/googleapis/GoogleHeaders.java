package com.google.api.client.googleapis;

import com.google.api.client.http.HttpHeaders;
import com.google.api.client.util.Key;
import com.google.api.client.util.escape.PercentEscaper;

public class GoogleHeaders extends HttpHeaders {
    public static final PercentEscaper SLUG_ESCAPER = new PercentEscaper(" !\"#$&'()*+,-./:;<=>?@[\\]^_`{|}~", false);
    @Key("X-GData-Client")
    private String gdataClient;
    @Key("X-GData-Key")
    private String gdataKey;
    @Key("GData-Version")
    @Deprecated
    public String gdataVersion;
    @Key("X-HTTP-Method-Override")
    private String methodOverride;
    @Key("Slug")
    private String slug;
    @Key("X-Upload-Content-Length")
    private long uploadContentLength;
    @Key("X-Upload-Content-Type")
    private String uploadContentType;

    public void setSlugFromFileName(String fileName) {
        this.slug = SLUG_ESCAPER.escape(fileName);
    }

    public void setApplicationName(String applicationName) {
        setUserAgent(applicationName);
    }

    public void setDeveloperId(String developerId) {
        this.gdataKey = "key=" + developerId;
    }

    public void setGoogleLogin(String authToken) {
        setAuthorization(getGoogleLoginValue(authToken));
    }

    public final long getUploadContentLength() {
        return this.uploadContentLength;
    }

    public final void setUploadContentLength(long uploadContentLength) {
        this.uploadContentLength = uploadContentLength;
    }

    public final String getUploadContentType() {
        return this.uploadContentType;
    }

    public final void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    public static String getGoogleLoginValue(String authToken) {
        return "GoogleLogin auth=" + authToken;
    }

    public final String getGDataVersion() {
        return this.gdataVersion;
    }

    public final void setGDataVersion(String gdataVersion) {
        this.gdataVersion = gdataVersion;
    }

    public final String getSlug() {
        return this.slug;
    }

    public final void setSlug(String slug) {
        this.slug = slug;
    }

    public final String getGDataClient() {
        return this.gdataClient;
    }

    public final void setGDataClient(String gdataClient) {
        this.gdataClient = gdataClient;
    }

    public final String getGDataKey() {
        return this.gdataKey;
    }

    public final void setGDataKey(String gdataKey) {
        this.gdataKey = gdataKey;
    }

    public final String getMethodOverride() {
        return this.methodOverride;
    }

    public final void setMethodOverride(String methodOverride) {
        this.methodOverride = methodOverride;
    }
}
