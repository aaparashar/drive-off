package com.google.common.hash;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;

@Beta
public final class Funnels {

    private static class SinkAsStream extends OutputStream {
        final PrimitiveSink sink;

        SinkAsStream(PrimitiveSink sink) {
            this.sink = (PrimitiveSink) Preconditions.checkNotNull(sink);
        }

        public void write(int b) {
            this.sink.putByte((byte) b);
        }

        public void write(byte[] bytes) {
            this.sink.putBytes(bytes);
        }

        public void write(byte[] bytes, int off, int len) {
            this.sink.putBytes(bytes, off, len);
        }

        public String toString() {
            return "Funnels.asOutputStream(" + this.sink + ")";
        }
    }

    private enum ByteArrayFunnel implements Funnel<byte[]> {
        INSTANCE;

        public void funnel(byte[] from, PrimitiveSink into) {
            into.putBytes(from);
        }

        public String toString() {
            return "Funnels.byteArrayFunnel()";
        }
    }

    private enum IntegerFunnel implements Funnel<Integer> {
        INSTANCE;

        public void funnel(Integer from, PrimitiveSink into) {
            into.putInt(from.intValue());
        }

        public String toString() {
            return "Funnels.integerFunnel()";
        }
    }

    private enum LongFunnel implements Funnel<Long> {
        INSTANCE;

        public void funnel(Long from, PrimitiveSink into) {
            into.putLong(from.longValue());
        }

        public String toString() {
            return "Funnels.longFunnel()";
        }
    }

    private enum StringFunnel implements Funnel<CharSequence> {
        INSTANCE;

        public void funnel(CharSequence from, PrimitiveSink into) {
            into.putString(from);
        }

        public String toString() {
            return "Funnels.stringFunnel()";
        }
    }

    private Funnels() {
    }

    public static Funnel<byte[]> byteArrayFunnel() {
        return ByteArrayFunnel.INSTANCE;
    }

    public static Funnel<CharSequence> stringFunnel() {
        return StringFunnel.INSTANCE;
    }

    public static Funnel<Integer> integerFunnel() {
        return IntegerFunnel.INSTANCE;
    }

    public static Funnel<Long> longFunnel() {
        return LongFunnel.INSTANCE;
    }

    public static OutputStream asOutputStream(PrimitiveSink sink) {
        return new SinkAsStream(sink);
    }
}
