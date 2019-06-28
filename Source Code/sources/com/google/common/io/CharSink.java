package com.google.common.io;

import com.google.common.base.Preconditions;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;

public abstract class CharSink {
    public abstract Writer openStream() throws IOException;

    public BufferedWriter openBufferedStream() throws IOException {
        Writer writer = openStream();
        return writer instanceof BufferedWriter ? (BufferedWriter) writer : new BufferedWriter(writer);
    }

    public void write(CharSequence charSequence) throws IOException {
        Preconditions.checkNotNull(charSequence);
        Closer closer = Closer.create();
        try {
            ((Writer) closer.register(openStream())).append(charSequence);
            closer.close();
        } catch (Throwable th) {
            closer.close();
        }
    }

    public void writeLines(Iterable<? extends CharSequence> lines) throws IOException {
        writeLines(lines, System.getProperty("line.separator"));
    }

    public void writeLines(Iterable<? extends CharSequence> lines, String lineSeparator) throws IOException {
        Preconditions.checkNotNull(lines);
        Preconditions.checkNotNull(lineSeparator);
        Closer closer = Closer.create();
        try {
            BufferedWriter out = (BufferedWriter) closer.register(openBufferedStream());
            for (CharSequence line : lines) {
                out.append(line).append(lineSeparator);
            }
            closer.close();
        } catch (Throwable th) {
            closer.close();
        }
    }

    public long writeFrom(Readable readable) throws IOException {
        Preconditions.checkNotNull(readable);
        Closer closer = Closer.create();
        try {
            long copy = CharStreams.copy(readable, (Writer) closer.register(openStream()));
            closer.close();
            return copy;
        } catch (Throwable th) {
            closer.close();
        }
    }
}
