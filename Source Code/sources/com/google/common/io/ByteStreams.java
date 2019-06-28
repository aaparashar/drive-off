package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.util.Arrays;
import java.util.zip.Checksum;

@Beta
public final class ByteStreams {
    private static final int BUF_SIZE = 4096;
    private static final OutputStream NULL_OUTPUT_STREAM = new C02331();

    /* renamed from: com.google.common.io.ByteStreams$1 */
    static class C02331 extends OutputStream {
        C02331() {
        }

        public void write(int b) {
        }

        public void write(byte[] b) {
            Preconditions.checkNotNull(b);
        }

        public void write(byte[] b, int off, int len) {
            Preconditions.checkNotNull(b);
        }

        public String toString() {
            return "ByteStreams.nullOutputStream()";
        }
    }

    private static final class LimitedInputStream extends FilterInputStream {
        private long left;
        private long mark = -1;

        LimitedInputStream(InputStream in, long limit) {
            super(in);
            Preconditions.checkNotNull(in);
            Preconditions.checkArgument(limit >= 0, "limit must be non-negative");
            this.left = limit;
        }

        public int available() throws IOException {
            return (int) Math.min((long) this.in.available(), this.left);
        }

        public synchronized void mark(int readLimit) {
            this.in.mark(readLimit);
            this.mark = this.left;
        }

        public int read() throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int result = this.in.read();
            if (result == -1) {
                return result;
            }
            this.left--;
            return result;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            if (this.left == 0) {
                return -1;
            }
            int result = this.in.read(b, off, (int) Math.min((long) len, this.left));
            if (result == -1) {
                return result;
            }
            this.left -= (long) result;
            return result;
        }

        public synchronized void reset() throws IOException {
            if (!this.in.markSupported()) {
                throw new IOException("Mark not supported");
            } else if (this.mark == -1) {
                throw new IOException("Mark not set");
            } else {
                this.in.reset();
                this.left = this.mark;
            }
        }

        public long skip(long n) throws IOException {
            long skipped = this.in.skip(Math.min(n, this.left));
            this.left -= skipped;
            return skipped;
        }
    }

    private static final class ByteArrayByteSource extends ByteSource {
        private final byte[] bytes;

        private ByteArrayByteSource(byte[] bytes) {
            this.bytes = (byte[]) Preconditions.checkNotNull(bytes);
        }

        public InputStream openStream() throws IOException {
            return new ByteArrayInputStream(this.bytes);
        }

        public long size() throws IOException {
            return (long) this.bytes.length;
        }

        public byte[] read() throws IOException {
            return (byte[]) this.bytes.clone();
        }

        public long copyTo(OutputStream output) throws IOException {
            output.write(this.bytes);
            return (long) this.bytes.length;
        }

        public HashCode hash(HashFunction hashFunction) throws IOException {
            return hashFunction.hashBytes(this.bytes);
        }

        public String toString() {
            return "ByteStreams.asByteSource(" + BaseEncoding.base16().encode(this.bytes) + ")";
        }
    }

    private static class ByteArrayDataInputStream implements ByteArrayDataInput {
        final DataInput input;

        ByteArrayDataInputStream(byte[] bytes) {
            this.input = new DataInputStream(new ByteArrayInputStream(bytes));
        }

        ByteArrayDataInputStream(byte[] bytes, int start) {
            this.input = new DataInputStream(new ByteArrayInputStream(bytes, start, bytes.length - start));
        }

        public void readFully(byte[] b) {
            try {
                this.input.readFully(b);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public void readFully(byte[] b, int off, int len) {
            try {
                this.input.readFully(b, off, len);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int skipBytes(int n) {
            try {
                return this.input.skipBytes(n);
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public boolean readBoolean() {
            try {
                return this.input.readBoolean();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public byte readByte() {
            try {
                return this.input.readByte();
            } catch (EOFException e) {
                throw new IllegalStateException(e);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public int readUnsignedByte() {
            try {
                return this.input.readUnsignedByte();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public short readShort() {
            try {
                return this.input.readShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int readUnsignedShort() {
            try {
                return this.input.readUnsignedShort();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public char readChar() {
            try {
                return this.input.readChar();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public int readInt() {
            try {
                return this.input.readInt();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public long readLong() {
            try {
                return this.input.readLong();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public float readFloat() {
            try {
                return this.input.readFloat();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public double readDouble() {
            try {
                return this.input.readDouble();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public String readLine() {
            try {
                return this.input.readLine();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }

        public String readUTF() {
            try {
                return this.input.readUTF();
            } catch (IOException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private static class ByteArrayDataOutputStream implements ByteArrayDataOutput {
        final ByteArrayOutputStream byteArrayOutputSteam;
        final DataOutput output;

        ByteArrayDataOutputStream() {
            this(new ByteArrayOutputStream());
        }

        ByteArrayDataOutputStream(int size) {
            this(new ByteArrayOutputStream(size));
        }

        ByteArrayDataOutputStream(ByteArrayOutputStream byteArrayOutputSteam) {
            this.byteArrayOutputSteam = byteArrayOutputSteam;
            this.output = new DataOutputStream(byteArrayOutputSteam);
        }

        public void write(int b) {
            try {
                this.output.write(b);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void write(byte[] b) {
            try {
                this.output.write(b);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void write(byte[] b, int off, int len) {
            try {
                this.output.write(b, off, len);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeBoolean(boolean v) {
            try {
                this.output.writeBoolean(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeByte(int v) {
            try {
                this.output.writeByte(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeBytes(String s) {
            try {
                this.output.writeBytes(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeChar(int v) {
            try {
                this.output.writeChar(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeChars(String s) {
            try {
                this.output.writeChars(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeDouble(double v) {
            try {
                this.output.writeDouble(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeFloat(float v) {
            try {
                this.output.writeFloat(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeInt(int v) {
            try {
                this.output.writeInt(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeLong(long v) {
            try {
                this.output.writeLong(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeShort(int v) {
            try {
                this.output.writeShort(v);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public void writeUTF(String s) {
            try {
                this.output.writeUTF(s);
            } catch (IOException impossible) {
                throw new AssertionError(impossible);
            }
        }

        public byte[] toByteArray() {
            return this.byteArrayOutputSteam.toByteArray();
        }
    }

    private ByteStreams() {
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] b) {
        return asInputSupplier(asByteSource(b));
    }

    public static InputSupplier<ByteArrayInputStream> newInputStreamSupplier(byte[] b, int off, int len) {
        return asInputSupplier(asByteSource(b).slice((long) off, (long) len));
    }

    public static ByteSource asByteSource(byte[] b) {
        return new ByteArrayByteSource(b);
    }

    public static void write(byte[] from, OutputSupplier<? extends OutputStream> to) throws IOException {
        asByteSink(to).write(from);
    }

    public static long copy(InputSupplier<? extends InputStream> from, OutputSupplier<? extends OutputStream> to) throws IOException {
        return asByteSource((InputSupplier) from).copyTo(asByteSink(to));
    }

    public static long copy(InputSupplier<? extends InputStream> from, OutputStream to) throws IOException {
        return asByteSource((InputSupplier) from).copyTo(to);
    }

    public static long copy(InputStream from, OutputSupplier<? extends OutputStream> to) throws IOException {
        return asByteSink(to).writeFrom(from);
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        byte[] buf = new byte[4096];
        long total = 0;
        while (true) {
            int r = from.read(buf);
            if (r == -1) {
                return total;
            }
            to.write(buf, 0, r);
            total += (long) r;
        }
    }

    public static long copy(ReadableByteChannel from, WritableByteChannel to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        ByteBuffer buf = ByteBuffer.allocate(4096);
        long total = 0;
        while (from.read(buf) != -1) {
            buf.flip();
            while (buf.hasRemaining()) {
                total += (long) to.write(buf);
            }
            buf.clear();
        }
        return total;
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        OutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public static byte[] toByteArray(InputSupplier<? extends InputStream> supplier) throws IOException {
        return asByteSource((InputSupplier) supplier).read();
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes) {
        return new ByteArrayDataInputStream(bytes);
    }

    public static ByteArrayDataInput newDataInput(byte[] bytes, int start) {
        Preconditions.checkPositionIndex(start, bytes.length);
        return new ByteArrayDataInputStream(bytes, start);
    }

    public static ByteArrayDataOutput newDataOutput() {
        return new ByteArrayDataOutputStream();
    }

    public static ByteArrayDataOutput newDataOutput(int size) {
        boolean z;
        if (size >= 0) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "Invalid size: %s", Integer.valueOf(size));
        return new ByteArrayDataOutputStream(size);
    }

    public static OutputStream nullOutputStream() {
        return NULL_OUTPUT_STREAM;
    }

    public static InputStream limit(InputStream in, long limit) {
        return new LimitedInputStream(in, limit);
    }

    public static long length(InputSupplier<? extends InputStream> supplier) throws IOException {
        return asByteSource((InputSupplier) supplier).size();
    }

    public static boolean equal(InputSupplier<? extends InputStream> supplier1, InputSupplier<? extends InputStream> supplier2) throws IOException {
        return asByteSource((InputSupplier) supplier1).contentEquals(asByteSource((InputSupplier) supplier2));
    }

    public static void readFully(InputStream in, byte[] b) throws IOException {
        readFully(in, b, 0, b.length);
    }

    public static void readFully(InputStream in, byte[] b, int off, int len) throws IOException {
        int read = read(in, b, off, len);
        if (read != len) {
            throw new EOFException("reached end of stream after reading " + read + " bytes; " + len + " bytes expected");
        }
    }

    public static void skipFully(InputStream in, long n) throws IOException {
        long toSkip = n;
        while (n > 0) {
            long amt = in.skip(n);
            if (amt != 0) {
                n -= amt;
            } else if (in.read() == -1) {
                throw new EOFException("reached end of stream after skipping " + (toSkip - n) + " bytes; " + toSkip + " bytes expected");
            } else {
                n--;
            }
        }
    }

    public static <T> T readBytes(InputSupplier<? extends InputStream> supplier, ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkNotNull(processor);
        Closer closer = Closer.create();
        try {
            T readBytes = readBytes((InputStream) closer.register((Closeable) supplier.getInput()), (ByteProcessor) processor);
            closer.close();
            return readBytes;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static <T> T readBytes(InputStream input, ByteProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(input);
        Preconditions.checkNotNull(processor);
        byte[] buf = new byte[4096];
        int read;
        do {
            read = input.read(buf);
            if (read == -1) {
                break;
            }
        } while (processor.processBytes(buf, 0, read));
        return processor.getResult();
    }

    @Deprecated
    public static long getChecksum(InputSupplier<? extends InputStream> supplier, final Checksum checksum) throws IOException {
        Preconditions.checkNotNull(checksum);
        return ((Long) readBytes((InputSupplier) supplier, new ByteProcessor<Long>() {
            public boolean processBytes(byte[] buf, int off, int len) {
                checksum.update(buf, off, len);
                return true;
            }

            public Long getResult() {
                long result = checksum.getValue();
                checksum.reset();
                return Long.valueOf(result);
            }
        })).longValue();
    }

    public static HashCode hash(InputSupplier<? extends InputStream> supplier, HashFunction hashFunction) throws IOException {
        return asByteSource((InputSupplier) supplier).hash(hashFunction);
    }

    public static int read(InputStream in, byte[] b, int off, int len) throws IOException {
        Preconditions.checkNotNull(in);
        Preconditions.checkNotNull(b);
        if (len < 0) {
            throw new IndexOutOfBoundsException("len is negative");
        }
        int total = 0;
        while (total < len) {
            int result = in.read(b, off + total, len - total);
            if (result == -1) {
                break;
            }
            total += result;
        }
        return total;
    }

    public static InputSupplier<InputStream> slice(InputSupplier<? extends InputStream> supplier, long offset, long length) {
        return asInputSupplier(asByteSource((InputSupplier) supplier).slice(offset, length));
    }

    public static InputSupplier<InputStream> join(final Iterable<? extends InputSupplier<? extends InputStream>> suppliers) {
        Preconditions.checkNotNull(suppliers);
        return new InputSupplier<InputStream>() {
            public InputStream getInput() throws IOException {
                return new MultiInputStream(suppliers.iterator());
            }
        };
    }

    public static InputSupplier<InputStream> join(InputSupplier<? extends InputStream>... suppliers) {
        return join(Arrays.asList(suppliers));
    }

    static <S extends InputStream> InputSupplier<S> asInputSupplier(final ByteSource source) {
        Preconditions.checkNotNull(source);
        return new InputSupplier<S>() {
            public S getInput() throws IOException {
                return source.openStream();
            }
        };
    }

    static <S extends OutputStream> OutputSupplier<S> asOutputSupplier(final ByteSink sink) {
        Preconditions.checkNotNull(sink);
        return new OutputSupplier<S>() {
            public S getOutput() throws IOException {
                return sink.openStream();
            }
        };
    }

    static ByteSource asByteSource(final InputSupplier<? extends InputStream> supplier) {
        Preconditions.checkNotNull(supplier);
        return new ByteSource() {
            public InputStream openStream() throws IOException {
                return (InputStream) supplier.getInput();
            }
        };
    }

    static ByteSink asByteSink(final OutputSupplier<? extends OutputStream> supplier) {
        Preconditions.checkNotNull(supplier);
        return new ByteSink() {
            public OutputStream openStream() throws IOException {
                return (OutputStream) supplier.getOutput();
            }
        };
    }
}
