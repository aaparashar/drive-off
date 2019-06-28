package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.AbstractIterator;
import com.google.common.collect.ImmutableList;
import java.io.Closeable;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

@Beta
public final class CharStreams {
    private static final int BUF_SIZE = 2048;

    private static final class StringCharSource extends CharSource {
        private static final Splitter LINE_SPLITTER = Splitter.on(Pattern.compile("\r\n|\n|\r"));
        private final String string;

        /* renamed from: com.google.common.io.CharStreams$StringCharSource$1 */
        class C02351 implements Iterable<String> {

            /* renamed from: com.google.common.io.CharStreams$StringCharSource$1$1 */
            class C06411 extends AbstractIterator<String> {
                Iterator<String> lines = StringCharSource.LINE_SPLITTER.split(StringCharSource.this.string).iterator();

                C06411() {
                }

                protected String computeNext() {
                    if (this.lines.hasNext()) {
                        String next = (String) this.lines.next();
                        if (this.lines.hasNext() || !next.isEmpty()) {
                            return next;
                        }
                    }
                    return (String) endOfData();
                }
            }

            C02351() {
            }

            public Iterator<String> iterator() {
                return new C06411();
            }
        }

        private StringCharSource(String string) {
            this.string = (String) Preconditions.checkNotNull(string);
        }

        public Reader openStream() {
            return new StringReader(this.string);
        }

        public String read() {
            return this.string;
        }

        private Iterable<String> lines() {
            return new C02351();
        }

        public String readFirstLine() {
            Iterator<String> lines = lines().iterator();
            return lines.hasNext() ? (String) lines.next() : null;
        }

        public ImmutableList<String> readLines() {
            return ImmutableList.copyOf(lines());
        }

        public String toString() {
            return "CharStreams.asCharSource(" + (this.string.length() <= 15 ? this.string : this.string.substring(0, 12) + "...") + ")";
        }
    }

    private CharStreams() {
    }

    public static InputSupplier<StringReader> newReaderSupplier(String value) {
        return asInputSupplier(asCharSource(value));
    }

    public static CharSource asCharSource(String string) {
        return new StringCharSource(string);
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(InputSupplier<? extends InputStream> in, Charset charset) {
        return asInputSupplier(ByteStreams.asByteSource((InputSupplier) in).asCharSource(charset));
    }

    public static OutputSupplier<OutputStreamWriter> newWriterSupplier(OutputSupplier<? extends OutputStream> out, Charset charset) {
        return asOutputSupplier(ByteStreams.asByteSink(out).asCharSink(charset));
    }

    public static <W extends Appendable & Closeable> void write(CharSequence from, OutputSupplier<W> to) throws IOException {
        asCharSink(to).write(from);
    }

    public static <R extends Readable & Closeable, W extends Appendable & Closeable> long copy(InputSupplier<R> from, OutputSupplier<W> to) throws IOException {
        return asCharSource((InputSupplier) from).copyTo(asCharSink(to));
    }

    public static <R extends Readable & Closeable> long copy(InputSupplier<R> from, Appendable to) throws IOException {
        return asCharSource((InputSupplier) from).copyTo(to);
    }

    public static long copy(Readable from, Appendable to) throws IOException {
        Preconditions.checkNotNull(from);
        Preconditions.checkNotNull(to);
        CharBuffer buf = CharBuffer.allocate(2048);
        long total = 0;
        while (from.read(buf) != -1) {
            buf.flip();
            to.append(buf);
            total += (long) buf.remaining();
            buf.clear();
        }
        return total;
    }

    public static String toString(Readable r) throws IOException {
        return toStringBuilder(r).toString();
    }

    public static <R extends Readable & Closeable> String toString(InputSupplier<R> supplier) throws IOException {
        return asCharSource((InputSupplier) supplier).read();
    }

    private static StringBuilder toStringBuilder(Readable r) throws IOException {
        Appendable sb = new StringBuilder();
        copy(r, sb);
        return sb;
    }

    public static <R extends Readable & Closeable> String readFirstLine(InputSupplier<R> supplier) throws IOException {
        return asCharSource((InputSupplier) supplier).readFirstLine();
    }

    public static <R extends Readable & Closeable> List<String> readLines(InputSupplier<R> supplier) throws IOException {
        Closer closer = Closer.create();
        try {
            List<String> readLines = readLines((Readable) closer.register((Closeable) supplier.getInput()));
            closer.close();
            return readLines;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static List<String> readLines(Readable r) throws IOException {
        List<String> result = new ArrayList();
        LineReader lineReader = new LineReader(r);
        while (true) {
            String line = lineReader.readLine();
            if (line == null) {
                return result;
            }
            result.add(line);
        }
    }

    public static <T> T readLines(Readable readable, LineProcessor<T> processor) throws IOException {
        Preconditions.checkNotNull(readable);
        Preconditions.checkNotNull(processor);
        LineReader lineReader = new LineReader(readable);
        String line;
        do {
            line = lineReader.readLine();
            if (line == null) {
                break;
            }
        } while (processor.processLine(line));
        return processor.getResult();
    }

    public static <R extends Readable & Closeable, T> T readLines(InputSupplier<R> supplier, LineProcessor<T> callback) throws IOException {
        Preconditions.checkNotNull(supplier);
        Preconditions.checkNotNull(callback);
        Closer closer = Closer.create();
        try {
            T readLines = readLines((Readable) closer.register((Closeable) supplier.getInput()), (LineProcessor) callback);
            closer.close();
            return readLines;
        } catch (Throwable th) {
            closer.close();
        }
    }

    public static InputSupplier<Reader> join(final Iterable<? extends InputSupplier<? extends Reader>> suppliers) {
        Preconditions.checkNotNull(suppliers);
        return new InputSupplier<Reader>() {
            public Reader getInput() throws IOException {
                return new MultiReader(suppliers.iterator());
            }
        };
    }

    public static InputSupplier<Reader> join(InputSupplier<? extends Reader>... suppliers) {
        return join(Arrays.asList(suppliers));
    }

    public static void skipFully(Reader reader, long n) throws IOException {
        Preconditions.checkNotNull(reader);
        while (n > 0) {
            long amt = reader.skip(n);
            if (amt != 0) {
                n -= amt;
            } else if (reader.read() == -1) {
                throw new EOFException();
            } else {
                n--;
            }
        }
    }

    public static Writer asWriter(Appendable target) {
        if (target instanceof Writer) {
            return (Writer) target;
        }
        return new AppendableWriter(target);
    }

    static <R extends Readable & Closeable> Reader asReader(final R readable) {
        Preconditions.checkNotNull(readable);
        if (readable instanceof Reader) {
            return (Reader) readable;
        }
        return new Reader() {
            public int read(char[] cbuf, int off, int len) throws IOException {
                return read(CharBuffer.wrap(cbuf, off, len));
            }

            public int read(CharBuffer target) throws IOException {
                return readable.read(target);
            }

            public void close() throws IOException {
                ((Closeable) readable).close();
            }
        };
    }

    static <R extends Reader> InputSupplier<R> asInputSupplier(final CharSource source) {
        Preconditions.checkNotNull(source);
        return new InputSupplier<R>() {
            public R getInput() throws IOException {
                return source.openStream();
            }
        };
    }

    static <W extends Writer> OutputSupplier<W> asOutputSupplier(final CharSink sink) {
        Preconditions.checkNotNull(sink);
        return new OutputSupplier<W>() {
            public W getOutput() throws IOException {
                return sink.openStream();
            }
        };
    }

    static <R extends Readable & Closeable> CharSource asCharSource(final InputSupplier<R> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSource() {
            public Reader openStream() throws IOException {
                return CharStreams.asReader((Readable) supplier.getInput());
            }
        };
    }

    static <W extends Appendable & Closeable> CharSink asCharSink(final OutputSupplier<W> supplier) {
        Preconditions.checkNotNull(supplier);
        return new CharSink() {
            public Writer openStream() throws IOException {
                return CharStreams.asWriter((Appendable) supplier.getOutput());
            }
        };
    }
}
