package org.acra.log;

import android.util.Log;

public final class AndroidLogDelegate implements ACRALog {
    /* renamed from: v */
    public int mo904v(String tag, String msg) {
        return Log.v(tag, msg);
    }

    /* renamed from: v */
    public int mo905v(String tag, String msg, Throwable tr) {
        return Log.v(tag, msg, tr);
    }

    /* renamed from: d */
    public int mo897d(String tag, String msg) {
        return Log.d(tag, msg);
    }

    /* renamed from: d */
    public int mo898d(String tag, String msg, Throwable tr) {
        return Log.d(tag, msg, tr);
    }

    /* renamed from: i */
    public int mo902i(String tag, String msg) {
        return Log.i(tag, msg);
    }

    /* renamed from: i */
    public int mo903i(String tag, String msg, Throwable tr) {
        return Log.i(tag, msg, tr);
    }

    /* renamed from: w */
    public int mo906w(String tag, String msg) {
        return Log.w(tag, msg);
    }

    /* renamed from: w */
    public int mo907w(String tag, String msg, Throwable tr) {
        return Log.w(tag, msg, tr);
    }

    /* renamed from: w */
    public int mo908w(String tag, Throwable tr) {
        return Log.w(tag, tr);
    }

    /* renamed from: e */
    public int mo899e(String tag, String msg) {
        return Log.e(tag, msg);
    }

    /* renamed from: e */
    public int mo900e(String tag, String msg, Throwable tr) {
        return Log.e(tag, msg, tr);
    }

    public String getStackTraceString(Throwable tr) {
        return Log.getStackTraceString(tr);
    }
}
