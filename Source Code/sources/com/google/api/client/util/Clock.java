package com.google.api.client.util;

public interface Clock {
    public static final Clock SYSTEM = new C03011();

    /* renamed from: com.google.api.client.util.Clock$1 */
    static class C03011 implements Clock {
        C03011() {
        }

        public long currentTimeMillis() {
            return System.currentTimeMillis();
        }
    }

    long currentTimeMillis();
}
