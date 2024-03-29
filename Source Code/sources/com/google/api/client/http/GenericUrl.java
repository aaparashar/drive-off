package com.google.api.client.http;

import com.google.api.client.util.GenericData;
import com.google.api.client.util.escape.CharEscapers;
import com.google.api.client.util.escape.Escaper;
import com.google.api.client.util.escape.PercentEscaper;
import com.google.common.base.Preconditions;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

public class GenericUrl extends GenericData {
    private static final Escaper URI_FRAGMENT_ESCAPER = new PercentEscaper("=&-_.!~*'()@:$,;/?:", false);
    private String fragment;
    private String host;
    private List<String> pathParts;
    private int port = -1;
    private String scheme;

    public GenericUrl(String encodedUrl) {
        try {
            URI uri = new URI(encodedUrl);
            this.scheme = uri.getScheme().toLowerCase();
            this.host = uri.getHost();
            this.port = uri.getPort();
            this.pathParts = toPathParts(uri.getRawPath());
            this.fragment = uri.getFragment();
            String query = uri.getRawQuery();
            if (query != null) {
                UrlEncodedParser.parse(query, (Object) this);
            }
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public int hashCode() {
        return build().hashCode();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || !(obj instanceof GenericUrl)) {
            return false;
        }
        return build().equals(((GenericUrl) obj).toString());
    }

    public String toString() {
        return build();
    }

    public GenericUrl clone() {
        GenericUrl result = (GenericUrl) super.clone();
        if (this.pathParts != null) {
            result.pathParts = new ArrayList(this.pathParts);
        }
        return result;
    }

    public final String getScheme() {
        return this.scheme;
    }

    public final void setScheme(String scheme) {
        this.scheme = (String) Preconditions.checkNotNull(scheme);
    }

    public String getHost() {
        return this.host;
    }

    public final void setHost(String host) {
        this.host = (String) Preconditions.checkNotNull(host);
    }

    public int getPort() {
        return this.port;
    }

    public final void setPort(int port) {
        Preconditions.checkArgument(port >= -1, "expected port >= -1");
        this.port = port;
    }

    public List<String> getPathParts() {
        return this.pathParts;
    }

    public void setPathParts(List<String> pathParts) {
        this.pathParts = pathParts;
    }

    public String getFragment() {
        return this.fragment;
    }

    public final void setFragment(String fragment) {
        this.fragment = fragment;
    }

    public final String build() {
        return buildAuthority() + buildRelativeUrl();
    }

    public final String buildAuthority() {
        StringBuilder buf = new StringBuilder();
        buf.append((String) Preconditions.checkNotNull(this.scheme));
        buf.append("://");
        buf.append((String) Preconditions.checkNotNull(this.host));
        int port = this.port;
        if (port != -1) {
            buf.append(':').append(port);
        }
        return buf.toString();
    }

    public final String buildRelativeUrl() {
        StringBuilder buf = new StringBuilder();
        if (this.pathParts != null) {
            appendRawPathFromParts(buf);
        }
        addQueryParams(entrySet(), buf);
        String fragment = this.fragment;
        if (fragment != null) {
            buf.append('#').append(URI_FRAGMENT_ESCAPER.escape(fragment));
        }
        return buf.toString();
    }

    public Object getFirst(String name) {
        Collection<Object> value = get(name);
        if (!(value instanceof Collection)) {
            return value;
        }
        Iterator<Object> iterator = value.iterator();
        return iterator.hasNext() ? iterator.next() : null;
    }

    public Collection<Object> getAll(String name) {
        Collection<Object> value = get(name);
        if (value == null) {
            return Collections.emptySet();
        }
        if (value instanceof Collection) {
            return Collections.unmodifiableCollection(value);
        }
        return Collections.singleton(value);
    }

    public String getRawPath() {
        if (this.pathParts == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder();
        appendRawPathFromParts(buf);
        return buf.toString();
    }

    public void setRawPath(String encodedPath) {
        this.pathParts = toPathParts(encodedPath);
    }

    public void appendRawPath(String encodedPath) {
        if (encodedPath != null && encodedPath.length() != 0) {
            List<String> appendedPathParts = toPathParts(encodedPath);
            if (this.pathParts == null || this.pathParts.isEmpty()) {
                this.pathParts = appendedPathParts;
                return;
            }
            int size = this.pathParts.size();
            this.pathParts.set(size - 1, ((String) this.pathParts.get(size - 1)) + ((String) appendedPathParts.get(0)));
            this.pathParts.addAll(appendedPathParts.subList(1, appendedPathParts.size()));
        }
    }

    public static List<String> toPathParts(String encodedPath) {
        if (encodedPath == null || encodedPath.length() == 0) {
            return null;
        }
        List<String> result = new ArrayList();
        int cur = 0;
        boolean notDone = true;
        while (notDone) {
            String sub;
            int slash = encodedPath.indexOf(47, cur);
            notDone = slash != -1;
            if (notDone) {
                sub = encodedPath.substring(cur, slash);
            } else {
                sub = encodedPath.substring(cur);
            }
            result.add(CharEscapers.decodeUri(sub));
            cur = slash + 1;
        }
        return result;
    }

    private void appendRawPathFromParts(StringBuilder buf) {
        int size = this.pathParts.size();
        for (int i = 0; i < size; i++) {
            String pathPart = (String) this.pathParts.get(i);
            if (i != 0) {
                buf.append('/');
            }
            if (pathPart.length() != 0) {
                buf.append(CharEscapers.escapeUriPath(pathPart));
            }
        }
    }

    static void addQueryParams(Set<Entry<String, Object>> entrySet, StringBuilder buf) {
        boolean first = true;
        for (Entry<String, Object> nameValueEntry : entrySet) {
            Collection<?> value = nameValueEntry.getValue();
            if (value != null) {
                String name = CharEscapers.escapeUriQuery((String) nameValueEntry.getKey());
                if (value instanceof Collection) {
                    for (Object repeatedValue : value) {
                        first = appendParam(first, buf, name, repeatedValue);
                    }
                } else {
                    first = appendParam(first, buf, name, value);
                }
            }
        }
    }

    private static boolean appendParam(boolean first, StringBuilder buf, String name, Object value) {
        if (first) {
            first = false;
            buf.append('?');
        } else {
            buf.append('&');
        }
        buf.append(name);
        String stringValue = CharEscapers.escapeUriQuery(value.toString());
        if (stringValue.length() != 0) {
            buf.append('=').append(stringValue);
        }
        return first;
    }
}
