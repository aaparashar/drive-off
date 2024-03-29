package com.google.common.io;

import com.google.common.annotations.Beta;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

@Beta
public final class Resources {

    private static final class UrlByteSource extends ByteSource {
        private final URL url;

        private UrlByteSource(URL url) {
            this.url = (URL) Preconditions.checkNotNull(url);
        }

        public InputStream openStream() throws IOException {
            return this.url.openStream();
        }

        public String toString() {
            return "Resources.newByteSource(" + this.url + ")";
        }
    }

    private Resources() {
    }

    public static InputSupplier<InputStream> newInputStreamSupplier(URL url) {
        return ByteStreams.asInputSupplier(asByteSource(url));
    }

    public static ByteSource asByteSource(URL url) {
        return new UrlByteSource(url);
    }

    public static InputSupplier<InputStreamReader> newReaderSupplier(URL url, Charset charset) {
        return CharStreams.asInputSupplier(asCharSource(url, charset));
    }

    public static CharSource asCharSource(URL url, Charset charset) {
        return asByteSource(url).asCharSource(charset);
    }

    public static byte[] toByteArray(URL url) throws IOException {
        return asByteSource(url).read();
    }

    public static String toString(URL url, Charset charset) throws IOException {
        return asCharSource(url, charset).read();
    }

    public static <T> T readLines(URL url, Charset charset, LineProcessor<T> callback) throws IOException {
        return CharStreams.readLines(newReaderSupplier(url, charset), (LineProcessor) callback);
    }

    public static List<String> readLines(URL url, Charset charset) throws IOException {
        return CharStreams.readLines(newReaderSupplier(url, charset));
    }

    public static void copy(URL from, OutputStream to) throws IOException {
        asByteSource(from).copyTo(to);
    }

    public static URL getResource(String resourceName) {
        boolean z;
        URL url = Resources.class.getClassLoader().getResource(resourceName);
        if (url != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "resource %s not found.", resourceName);
        return url;
    }

    public static URL getResource(Class<?> contextClass, String resourceName) {
        boolean z;
        URL url = contextClass.getResource(resourceName);
        if (url != null) {
            z = true;
        } else {
            z = false;
        }
        Preconditions.checkArgument(z, "resource %s relative to %s not found.", resourceName, contextClass.getName());
        return url;
    }
}
