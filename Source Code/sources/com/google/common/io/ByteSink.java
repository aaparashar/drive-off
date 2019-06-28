package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.Charset;

public abstract class ByteSink {

    private final class AsCharSink extends CharSink {
        private final Charset charset;

        private AsCharSink(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public Writer openStream() throws IOException {
            return new OutputStreamWriter(ByteSink.this.openStream(), this.charset);
        }

        public String toString() {
            return ByteSink.this.toString() + ".asCharSink(" + this.charset + ")";
        }
    }

    public abstract OutputStream openStream() throws IOException;

    public CharSink asCharSink(Charset charset) {
        return new AsCharSink(charset);
    }

    public BufferedOutputStream openBufferedStream() throws IOException {
        OutputStream out = openStream();
        return out instanceof BufferedOutputStream ? (BufferedOutputStream) out : new BufferedOutputStream(out);
    }

    public void write(byte[] bytes) throws IOException {
        Preconditions.checkNotNull(bytes);
        Closer closer = Closer.create();
        try {
            ((OutputStream) closer.register(openStream())).write(bytes);
            closer.close();
        } catch (Throwable th) {
            closer.close();
        }
    }

    public long writeFrom(InputStream input) throws IOException {
        Preconditions.checkNotNull(input);
        Closer closer = Closer.create();
        try {
            long copy = ByteStreams.copy(input, (OutputStream) closer.register(openStream()));
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }
}
