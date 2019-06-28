package com.google.api.client.protobuf;

import com.google.api.client.util.ObjectParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.Charset;

public class ProtoObjectParser implements ObjectParser {
    public <T> T parseAndClose(InputStream in, Charset charset, Class<T> dataClass) throws IOException {
        return ProtocolBuffers.parseAndClose(in, dataClass);
    }

    public Object parseAndClose(InputStream in, Charset charset, Type dataType) throws IOException {
        if (dataType instanceof Class) {
            return parseAndClose(in, charset, (Class) dataType);
        }
        throw new UnsupportedOperationException("dataType must be of Class<? extends MessageList>");
    }

    public <T> T parseAndClose(Reader reader, Class<T> cls) throws IOException {
        throw new UnsupportedOperationException("protocol buffers must be read from a binary stream");
    }

    public Object parseAndClose(Reader reader, Type dataType) throws IOException {
        throw new UnsupportedOperationException("protocol buffers must be read from a binary stream");
    }
}
