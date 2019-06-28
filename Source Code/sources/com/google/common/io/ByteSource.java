package com.google.common.io;

import com.google.common.base.Preconditions;
import com.google.common.hash.Funnels;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hasher;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Arrays;

public abstract class ByteSource {
    private static final int BUF_SIZE = 4096;
    private static final byte[] countBuffer = new byte[4096];

    private final class AsCharSource extends CharSource {
        private final Charset charset;

        private AsCharSource(Charset charset) {
            this.charset = (Charset) Preconditions.checkNotNull(charset);
        }

        public Reader openStream() throws IOException {
            return new InputStreamReader(ByteSource.this.openStream(), this.charset);
        }

        public String toString() {
            return ByteSource.this.toString() + ".asCharSource(" + this.charset + ")";
        }
    }

    private final class SlicedByteSource extends ByteSource {
        private final long length;
        private final long offset;

        private SlicedByteSource(long offset, long length) {
            boolean z;
            if (offset >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "offset (%s) may not be negative", Long.valueOf(offset));
            if (length >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "length (%s) may not be negative", Long.valueOf(length));
            this.offset = offset;
            this.length = length;
        }

        public InputStream openStream() throws IOException {
            Closer closer;
            InputStream in = ByteSource.this.openStream();
            if (this.offset > 0) {
                try {
                    ByteStreams.skipFully(in, this.offset);
                } catch (Throwable th) {
                    closer.close();
                }
            }
            return ByteStreams.limit(in, this.length);
        }

        public ByteSource slice(long offset, long length) {
            boolean z;
            if (offset >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "offset (%s) may not be negative", Long.valueOf(offset));
            if (length >= 0) {
                z = true;
            } else {
                z = false;
            }
            Preconditions.checkArgument(z, "length (%s) may not be negative", Long.valueOf(length));
            return ByteSource.this.slice(this.offset + offset, Math.min(length, this.length - offset));
        }

        public String toString() {
            return ByteSource.this.toString() + ".slice(" + this.offset + ", " + this.length + ")";
        }
    }

    public abstract InputStream openStream() throws IOException;

    public CharSource asCharSource(Charset charset) {
        return new AsCharSource(charset);
    }

    public BufferedInputStream openBufferedStream() throws IOException {
        InputStream in = openStream();
        return in instanceof BufferedInputStream ? (BufferedInputStream) in : new BufferedInputStream(in);
    }

    public ByteSource slice(long offset, long length) {
        return new SlicedByteSource(offset, length);
    }

    public long size() throws IOException {
        long countBySkipping;
        Closer closer = Closer.create();
        try {
            countBySkipping = countBySkipping((InputStream) closer.register(openStream()));
            closer.close();
        } catch (IOException e) {
            closer.close();
            closer = Closer.create();
            countBySkipping = countByReading((InputStream) closer.register(openStream()));
            closer.close();
        } catch (Throwable e2) {
            try {
                RuntimeException rethrow = closer.rethrow(e2);
            } catch (Throwable th) {
                closer.close();
            }
        }
        return countBySkipping;
    }

    private long countBySkipping(InputStream in) throws IOException {
        long count = 0;
        while (true) {
            long skipped = in.skip((long) Math.min(in.available(), Integer.MAX_VALUE));
            if (skipped > 0) {
                count += skipped;
            } else if (in.read() == -1) {
                return count;
            } else {
                count++;
            }
        }
    }

    private long countByReading(InputStream in) throws IOException {
        long count = 0;
        while (true) {
            long read = (long) in.read(countBuffer);
            if (read == -1) {
                return count;
            }
            count += read;
        }
    }

    public long copyTo(OutputStream output) throws IOException {
        Preconditions.checkNotNull(output);
        Closer closer = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) closer.register(openStream()), output);
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public long copyTo(ByteSink sink) throws IOException {
        Preconditions.checkNotNull(sink);
        Closer closer = Closer.create();
        try {
            long copy = ByteStreams.copy((InputStream) closer.register(openStream()), (OutputStream) closer.register(sink.openStream()));
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public byte[] read() throws IOException {
        Closer closer = Closer.create();
        try {
            byte[] toByteArray = ByteStreams.toByteArray((InputStream) closer.register(openStream()));
            closer.close();
            return toByteArray;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public HashCode hash(HashFunction hashFunction) throws IOException {
        Hasher hasher = hashFunction.newHasher();
        copyTo(Funnels.asOutputStream(hasher));
        return hasher.hash();
    }

    public boolean contentEquals(ByteSource other) throws IOException {
        Preconditions.checkNotNull(other);
        byte[] buf1 = new byte[4096];
        byte[] buf2 = new byte[4096];
        Closer closer = Closer.create();
        try {
            InputStream in1 = (InputStream) closer.register(openStream());
            InputStream in2 = (InputStream) closer.register(other.openStream());
            int read1;
            do {
                read1 = ByteStreams.read(in1, buf1, 0, 4096);
                if (!(read1 == ByteStreams.read(in2, buf2, 0, 4096) && Arrays.equals(buf1, buf2))) {
                    closer.close();
                    return false;
                }
            } while (read1 == 4096);
            closer.close();
            return true;
        } catch (Throwable th) {
            closer.close();
        }
    }
}
