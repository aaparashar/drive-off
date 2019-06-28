package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.OutputStream;

@Deprecated
@Beta
public final class NullOutputStream extends OutputStream {
    public void write(int b) {
    }

    public void write(byte[] b, int off, int len) {
        Preconditions.checkNotNull(b);
    }
}
