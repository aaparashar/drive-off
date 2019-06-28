package com.google.common.cache;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Random;
import sun.misc.Unsafe;

abstract class Striped64 extends Number {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final Unsafe UNSAFE;
    private static final long baseOffset;
    private static final long busyOffset;
    static final ThreadHashCode threadHashCode = new ThreadHashCode();
    volatile transient long base;
    volatile transient int busy;
    volatile transient Cell[] cells;

    /* renamed from: com.google.common.cache.Striped64$1 */
    static class C01731 implements PrivilegedExceptionAction<Unsafe> {
        C01731() {
        }

        public Unsafe run() throws Exception {
            Class<Unsafe> k = Unsafe.class;
            for (Field f : k.getDeclaredFields()) {
                f.setAccessible(true);
                Object x = f.get(null);
                if (k.isInstance(x)) {
                    return (Unsafe) k.cast(x);
                }
            }
            throw new NoSuchFieldError("the Unsafe");
        }
    }

    static final class Cell {
        private static final Unsafe UNSAFE;
        private static final long valueOffset;
        volatile long p0;
        volatile long p1;
        volatile long p2;
        volatile long p3;
        volatile long p4;
        volatile long p5;
        volatile long p6;
        volatile long q0;
        volatile long q1;
        volatile long q2;
        volatile long q3;
        volatile long q4;
        volatile long q5;
        volatile long q6;
        volatile long value;

        Cell(long x) {
            this.value = x;
        }

        final boolean cas(long cmp, long val) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, cmp, val);
        }

        static {
            try {
                UNSAFE = Striped64.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));
            } catch (Exception e) {
                throw new Error(e);
            }
        }
    }

    static final class HashCode {
        static final Random rng = new Random();
        int code;

        HashCode() {
            int h = rng.nextInt();
            if (h == 0) {
                h = 1;
            }
            this.code = h;
        }
    }

    static final class ThreadHashCode extends ThreadLocal<HashCode> {
        ThreadHashCode() {
        }

        public HashCode initialValue() {
            return new HashCode();
        }
    }

    abstract long fn(long j, long j2);

    static {
        try {
            UNSAFE = getUnsafe();
            Class<?> sk = Striped64.class;
            baseOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("base"));
            busyOffset = UNSAFE.objectFieldOffset(sk.getDeclaredField("busy"));
        } catch (Exception e) {
            throw new Error(e);
        }
    }

    Striped64() {
    }

    final boolean casBase(long cmp, long val) {
        return UNSAFE.compareAndSwapLong(this, baseOffset, cmp, val);
    }

    final boolean casBusy() {
        return UNSAFE.compareAndSwapInt(this, busyOffset, 0, 1);
    }

    final void retryUpdate(long x, HashCode hc, boolean wasUncontended) {
        int h = hc.code;
        boolean collide = false;
        while (true) {
            long v;
            Cell[] rs;
            Cell[] as = this.cells;
            if (as != null) {
                int n = as.length;
                if (n > 0) {
                    Cell a = as[(n - 1) & h];
                    if (a != null) {
                        if (wasUncontended) {
                            v = a.value;
                            if (a.cas(v, fn(v, x))) {
                                break;
                            } else if (n >= NCPU || this.cells != as) {
                                collide = false;
                            } else if (!collide) {
                                collide = true;
                            } else if (this.busy == 0 && casBusy()) {
                                try {
                                    if (this.cells == as) {
                                        rs = new Cell[(n << 1)];
                                        for (int i = 0; i < n; i++) {
                                            rs[i] = as[i];
                                        }
                                        this.cells = rs;
                                    }
                                    this.busy = 0;
                                    collide = false;
                                } catch (Throwable th) {
                                    this.busy = 0;
                                }
                            }
                        } else {
                            wasUncontended = true;
                        }
                    } else {
                        if (this.busy == 0) {
                            Cell cell = new Cell(x);
                            if (this.busy == 0 && casBusy()) {
                                boolean created = false;
                                try {
                                    rs = this.cells;
                                    if (rs != null) {
                                        int m = rs.length;
                                        if (m > 0) {
                                            int j = (m - 1) & h;
                                            if (rs[j] == null) {
                                                rs[j] = cell;
                                                created = true;
                                            }
                                        }
                                    }
                                    this.busy = 0;
                                    if (created) {
                                        break;
                                    }
                                } catch (Throwable th2) {
                                    this.busy = 0;
                                }
                            }
                        }
                        collide = false;
                    }
                    h ^= h << 13;
                    h ^= h >>> 17;
                    h ^= h << 5;
                }
            }
            if (this.busy == 0 && this.cells == as && casBusy()) {
                boolean init = false;
                try {
                    if (this.cells == as) {
                        rs = new Cell[2];
                        rs[h & 1] = new Cell(x);
                        this.cells = rs;
                        init = true;
                    }
                    this.busy = 0;
                    if (init) {
                        break;
                    }
                } catch (Throwable th3) {
                    this.busy = 0;
                }
            } else {
                v = this.base;
                if (casBase(v, fn(v, x))) {
                    break;
                }
            }
        }
        hc.code = h;
    }

    final void internalReset(long initialValue) {
        Cell[] as = this.cells;
        this.base = initialValue;
        if (as != null) {
            for (Cell a : as) {
                if (a != null) {
                    a.value = initialValue;
                }
            }
        }
    }

    private static Unsafe getUnsafe() {
        Unsafe unsafe;
        try {
            unsafe = Unsafe.getUnsafe();
        } catch (SecurityException e) {
            try {
                unsafe = (Unsafe) AccessController.doPrivileged(new C01731());
            } catch (PrivilegedActionException e2) {
                throw new RuntimeException("Could not initialize intrinsics", e2.getCause());
            }
        }
        return unsafe;
    }
}
