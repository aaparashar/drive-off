package com.google.api.client.util.escape;

public abstract class UnicodeEscaper extends Escaper {
    private static final int DEST_PAD = 32;

    public abstract String escape(String str);

    protected abstract char[] escape(int i);

    protected abstract int nextEscapeIndex(CharSequence charSequence, int i, int i2);

    protected final String escapeSlow(String s, int index) {
        int charsSkipped;
        int end = s.length();
        char[] dest = Platform.charBufferFromThreadLocal();
        int destIndex = 0;
        int unescapedChunkStart = 0;
        while (index < end) {
            int cp = codePointAt(s, index, end);
            if (cp < 0) {
                throw new IllegalArgumentException("Trailing high surrogate at end of input");
            }
            char[] escaped = escape(cp);
            int nextIndex = index + (Character.isSupplementaryCodePoint(cp) ? 2 : 1);
            if (escaped != null) {
                charsSkipped = index - unescapedChunkStart;
                int sizeNeeded = (destIndex + charsSkipped) + escaped.length;
                if (dest.length < sizeNeeded) {
                    dest = growBuffer(dest, destIndex, ((sizeNeeded + end) - index) + 32);
                }
                if (charsSkipped > 0) {
                    s.getChars(unescapedChunkStart, index, dest, destIndex);
                    destIndex += charsSkipped;
                }
                if (escaped.length > 0) {
                    System.arraycopy(escaped, 0, dest, destIndex, escaped.length);
                    destIndex += escaped.length;
                }
                unescapedChunkStart = nextIndex;
            }
            index = nextEscapeIndex(s, nextIndex, end);
        }
        charsSkipped = end - unescapedChunkStart;
        if (charsSkipped > 0) {
            int endIndex = destIndex + charsSkipped;
            if (dest.length < endIndex) {
                dest = growBuffer(dest, destIndex, endIndex);
            }
            s.getChars(unescapedChunkStart, end, dest, destIndex);
            destIndex = endIndex;
        }
        return new String(dest, 0, destIndex);
    }

    protected static int codePointAt(CharSequence seq, int index, int end) {
        if (index < end) {
            int index2 = index + 1;
            char c1 = seq.charAt(index);
            if (c1 < '?' || c1 > '?') {
                return c1;
            }
            if (c1 > '?') {
                throw new IllegalArgumentException("Unexpected low surrogate character '" + c1 + "' with value " + c1 + " at index " + (index2 - 1));
            } else if (index2 == end) {
                return -c1;
            } else {
                char c2 = seq.charAt(index2);
                if (Character.isLowSurrogate(c2)) {
                    return Character.toCodePoint(c1, c2);
                }
                throw new IllegalArgumentException("Expected low surrogate but got char '" + c2 + "' with value " + c2 + " at index " + index2);
            }
        }
        throw new IndexOutOfBoundsException("Index exceeds specified range");
    }

    private static char[] growBuffer(char[] dest, int index, int size) {
        char[] copy = new char[size];
        if (index > 0) {
            System.arraycopy(dest, 0, copy, 0, index);
        }
        return copy;
    }
}
