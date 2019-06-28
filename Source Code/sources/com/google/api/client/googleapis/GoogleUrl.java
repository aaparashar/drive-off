package com.google.api.client.googleapis;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.util.Key;

public class GoogleUrl extends GenericUrl {
    @Key
    private String alt;
    @Key
    private String fields;
    @Key
    private String key;
    @Key("prettyPrint")
    private Boolean prettyprint;
    @Key("userIp")
    private String userip;

    public GoogleUrl(String encodedUrl) {
        super(encodedUrl);
    }

    public GoogleUrl clone() {
        return (GoogleUrl) super.clone();
    }

    public Boolean getPrettyPrint() {
        return this.prettyprint;
    }

    public void setPrettyPrint(Boolean prettyPrint) {
        this.prettyprint = prettyPrint;
    }

    public final String getAlt() {
        return this.alt;
    }

    public final void setAlt(String alt) {
        this.alt = alt;
    }

    public final String getFields() {
        return this.fields;
    }

    public final void setFields(String fields) {
        this.fields = fields;
    }

    public final String getKey() {
        return this.key;
    }

    public final void setKey(String key) {
        this.key = key;
    }

    public final String getUserIp() {
        return this.userip;
    }

    public final void setUserIp(String userip) {
        this.userip = userip;
    }
}
