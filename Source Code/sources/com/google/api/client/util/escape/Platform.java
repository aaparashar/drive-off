package com.google.api.client.util.escape;

final class Platform {
    private static final ThreadLocal<char[]> DEST_TL = new C00101();

    /* renamed from: com.google.api.client.util.escape.Platform$1 */
    static class C00101 extends ThreadLocal<char[]> {
        C00101() {
        }

        protected char[] initialValue() {
            return new char[1024];
        }
    }

    private Platform() {
    }

    static char[] charBufferFromThreadLocal() {
        return (char[]) DEST_TL.get();
    }
}
