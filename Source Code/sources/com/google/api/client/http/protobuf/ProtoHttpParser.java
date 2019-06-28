package com.google.api.client.http.protobuf;

import com.google.api.client.http.HttpParser;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.protobuf.ProtocolBuffers;
import com.google.common.base.Preconditions;
import java.io.IOException;

@Deprecated
public class ProtoHttpParser implements HttpParser {
    private final String contentType;

    public static class Builder {
        private String contentType = ProtocolBuffers.CONTENT_TYPE;

        protected Builder() {
        }

        public ProtoHttpParser build() {
            return new ProtoHttpParser(this.contentType);
        }

        public final String getContentType() {
            return this.contentType;
        }

        public Builder setContentType(String contentType) {
            this.contentType = (String) Preconditions.checkNotNull(contentType);
            return this;
        }
    }

    public ProtoHttpParser() {
        this.contentType = ProtocolBuffers.CONTENT_TYPE;
    }

    protected ProtoHttpParser(String contentType) {
        this.contentType = contentType;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public <T> T parse(HttpResponse response, Class<T> dataClass) throws IOException {
        return ProtocolBuffers.parseAndClose(response.getContent(), dataClass);
    }

    public static Builder builder() {
        return new Builder();
    }
}
