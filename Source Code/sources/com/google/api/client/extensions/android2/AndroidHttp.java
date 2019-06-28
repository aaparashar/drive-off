package com.google.api.client.extensions.android2;

import android.os.Build.VERSION;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.apache.ApacheHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;

public class AndroidHttp {
    private static final int GINGERBREAD = 9;

    public static HttpTransport newCompatibleTransport() {
        return isGingerbreadOrHigher() ? new NetHttpTransport() : new ApacheHttpTransport();
    }

    public static boolean isGingerbreadOrHigher() {
        return VERSION.SDK_INT >= 9;
    }
}
