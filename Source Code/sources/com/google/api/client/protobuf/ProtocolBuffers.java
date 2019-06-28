package com.google.api.client.protobuf;

import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;

public class ProtocolBuffers {
    public static final String ALT_CONTENT_TYPE = "application/x-protobuffer";
    public static final String CONTENT_TYPE = "application/x-protobuf";

    public static <T extends MessageLite> T parseAndClose(InputStream inputStream, Class<T> messageClass) throws IOException {
        try {
            MessageLite messageLite = (MessageLite) messageClass.cast(messageClass.getDeclaredMethod("parseFrom", new Class[]{InputStream.class}).invoke(null, new Object[]{inputStream}));
            inputStream.close();
            return messageLite;
        } catch (Exception e) {
            IOException io = new IOException("Error parsing message of type " + messageClass);
            io.initCause(e);
            throw io;
        } catch (Throwable th) {
            inputStream.close();
        }
    }

    private ProtocolBuffers() {
    }
}
