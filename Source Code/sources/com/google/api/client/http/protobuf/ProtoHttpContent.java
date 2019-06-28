package com.google.api.client.http.protobuf;

import com.google.api.client.http.AbstractHttpContent;
import com.google.api.client.http.HttpMediaType;
import com.google.api.client.protobuf.ProtocolBuffers;
import com.google.common.base.Preconditions;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.OutputStream;

public class ProtoHttpContent extends AbstractHttpContent {
    private final MessageLite message;
    private String type = ProtocolBuffers.CONTENT_TYPE;

    public ProtoHttpContent(MessageLite message) {
        super(ProtocolBuffers.CONTENT_TYPE);
        this.message = (MessageLite) Preconditions.checkNotNull(message);
    }

    public long getLength() throws IOException {
        return (long) this.message.getSerializedSize();
    }

    public String getType() {
        return this.type;
    }

    public void writeTo(OutputStream out) throws IOException {
        this.message.writeTo(out);
        out.flush();
    }

    public ProtoHttpContent setType(String type) {
        this.type = type;
        return this;
    }

    public final MessageLite getMessage() {
        return this.message;
    }

    public ProtoHttpContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }
}
